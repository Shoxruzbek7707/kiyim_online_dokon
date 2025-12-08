package uz.pdp.kiyim_online_dokon.bot.controllers;

import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.kiyim_online_dokon.bot.XaridBot;
import uz.pdp.kiyim_online_dokon.bot.session.UserSession;
import uz.pdp.kiyim_online_dokon.bot.session.UserState;
import uz.pdp.kiyim_online_dokon.bot.utils.KeyboardFactory;
import uz.pdp.kiyim_online_dokon.dto.CategoryDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;
import uz.pdp.kiyim_online_dokon.service.interfaces.CategoryService;
import uz.pdp.kiyim_online_dokon.service.interfaces.ProductsService;
import uz.pdp.kiyim_online_dokon.service.interfaces.CartService;
import uz.pdp.kiyim_online_dokon.service.interfaces.OrderService;
import uz.pdp.kiyim_online_dokon.service.interfaces.UsersService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// CallbackQueryHandler nomi MessageHandlerga o'zgartirildi
public class MessageHandler {
    private final XaridBot bot;
    private final Map<Long, UserSession> sessions;
    private final ProductsService productsService;
    private final CategoryService categoryService;
    // Boshqa servicelarni ehtiyojga qarab foydalanamiz
    private final CartService cartService;
    private final OrderService orderService;
    private final UsersService usersService;

    public MessageHandler(XaridBot bot, Map<Long, UserSession> sessions,
                          ProductsService productsService, CategoryService categoryService,
                          CartService cartService, OrderService orderService, UsersService usersService) {
        this.bot = bot;
        this.sessions = sessions;
        this.productsService = productsService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.usersService = usersService;
    }

    public void handleMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        UserSession session = sessions.computeIfAbsent(chatId, UserSession::new);

        // Buyruqlar (Commands)
        if (text.startsWith("/")) {
            handleCommand(chatId, text, session);
            return;
        }

        // Holatga bog'liq matnlar
        if (session.getState() == UserState.SEARCHING) {
            handleSearch(chatId, text, session);
            return;
        }

        // Asosiy menyu tugmalari
        switch (text) {
            case "🏠 Bosh menu" -> showMainMenu(chatId, session);
            case "📦 Mahsulotlar" -> showCategories(chatId, session);
            case "🛒 Savatim" -> showCart(chatId, session);
            case "📋 Buyurtmalarim" -> showOrders(chatId, session);
            case "ℹ️ Biz haqimizda" -> showAbout(chatId);
            case "🔍 Qidirish" -> startSearch(chatId, session);
            default -> handleDynamicSelection(chatId, text, session);
        }
    }

    private void handleCommand(Long chatId, String text, UserSession session) {
        if (text.equals("/start")) {
            showMainMenu(chatId, session);
        } else {
            bot.sendMessage(chatId, "❌ Noma’lum buyruq", KeyboardFactory.createMainMenuKeyboard());
        }
    }

    // --- Bosh menyu funksiyalari ---

    private void showMainMenu(Long chatId, UserSession session) {
        session.setState(UserState.MAIN_MENU);
        bot.sendMessage(chatId, "🏪 Xush kelibsiz! Bosh menu:", KeyboardFactory.createMainMenuKeyboard());
    }

    private void showCategories(Long chatId, UserSession session) {
        List<CategoryDTO> categories = categoryService.getAllCategories();

        if (categories.isEmpty()) {
            bot.sendMessage(chatId, "❌ Kategoriyalar topilmadi.", KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        List<String> categoryNames = categories.stream()
                .map(CategoryDTO::getName)
                .collect(Collectors.toList());

        bot.sendMessage(chatId, "📦 Kategoriyani tanlang:",
                KeyboardFactory.createDynamicKeyboard(categoryNames, true));
    }

    private void startSearch(Long chatId, UserSession session) {
        session.setState(UserState.SEARCHING);
        bot.sendMessage(chatId, "🔍 Qidiruvni boshlang. Mahsulot nomini yozing:", KeyboardFactory.createBackToMenuKeyboard());
    }

    private void showAbout(Long chatId) {
        String about = "ℹ️ Biz haqimizda..."; // Sizning 'showAbout' matningizni qo'shing
        bot.sendMessage(chatId, about, KeyboardFactory.createMainMenuKeyboard());
    }

    private void showOrders(Long chatId, UserSession session) {
        // Buyurtmalarni UsersService orqali ID bilan olish (Telegram User ID)
        // User sessiondagi chatId ni ishlatib, bizning DB dagi Users ID'sini topish kerak.
        // Hozirgi kodda bu mantiq yo'q, shuning uchun shartli matn beramiz:
        bot.sendMessage(chatId, "📋 Buyurtmalaringiz ro'yxati hozircha bo'sh (Mantiq OrderService orqali kiritilishi kerak).",
                KeyboardFactory.createMainMenuKeyboard());
    }

    private void showCart(Long chatId, UserSession session) {
        List<ProductsDTO> cartItems = session.getCart();

        if (cartItems.isEmpty()) {
            bot.sendMessage(chatId, "🛒 Savat bo‘sh!", KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        // Savatni hisoblash va ko'rsatish mantiqi (Sizning oldingi kodingizdan olindi)
        StringBuilder sb = new StringBuilder("🛒 Savatingiz:\n\n");
        double total = 0;
        for (ProductsDTO p : cartItems) {
            sb.append("• ").append(p.getName()).append(" - ").append(p.getPrice()).append(" $\n");
            total += p.getPrice();
        }
        sb.append("\n💰 Jami: ").append(total).append(" $");

        bot.sendMessage(chatId, sb.toString(), KeyboardFactory.createMainMenuKeyboard());
        session.setState(UserState.MAIN_MENU);
    }

    // --- Dynamic Tugma tanlovini boshqarish ---

    private void handleDynamicSelection(Long chatId, String text, UserSession session) {
        // 1. Kategoriyani tanlash holatini tekshirish
        List<CategoryDTO> allCategories = categoryService.getAllCategories();
        boolean isCategory = allCategories.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(text.trim()));

        if (isCategory) {
            // Kategoriyadan mahsulotlarni olish va ko'rsatish
            CategoryDTO category = allCategories.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(text.trim()))
                    .findFirst().orElseThrow();

            showProductsByCategory(chatId, session, category.getId(), category.getName());
            return;
        }

        // 2. Mahsulotni tanlash holatini tekshirish (Kategoriya ichida yoki Qidiruv natijasida)
        handleProductSelection(chatId, text, session);
    }


    private void showProductsByCategory(Long chatId, UserSession session, Integer categoryId, String categoryName) {
        List<ProductsDTO> products = categoryService.getProductsByCategoryId(categoryId);

        if (products.isEmpty()) {
            bot.sendMessage(chatId, "❌ Bu kategoriyada mahsulotlar topilmadi.", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
            return;
        }

        session.setLastDisplayedProducts(products); // Mahsulotlar ro'yxatini sessionda saqlash
        session.setState(UserState.SELECTING_PRODUCT);

        List<String> productNames = products.stream()
                .map(ProductsDTO::getName)
                .collect(Collectors.toList());

        bot.sendMessage(chatId, "🛍 '" + categoryName + "' bo'limidagi mahsulotni tanlang:",
                KeyboardFactory.createDynamicKeyboard(productNames, true));
    }

    private void handleProductSelection(Long chatId, String text, UserSession session) {
        List<ProductsDTO> products = session.getLastDisplayedProducts();

        if (products.isEmpty() || (session.getState() != UserState.SELECTING_PRODUCT && session.getState() != UserState.SEARCHING_RESULTS)) {
            // Agar ro'yxat bo'sh bo'lsa yoki noto'g'ri holatda bo'lsa
            bot.sendMessage(chatId, "❌ Noma’lum mahsulot yoki buyruq!", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
            return;
        }

        ProductsDTO selectedProduct = products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(text.trim()))
                .findFirst().orElse(null);

        if (selectedProduct != null) {
            session.addToCart(selectedProduct);
            bot.sendMessage(chatId, "✅ " + selectedProduct.getName() + " savatga qo‘shildi!",
                    KeyboardFactory.createMainMenuKeyboard()); // Bosh menyu tugmalarini qaytaradi
            session.setState(UserState.MAIN_MENU);
            // Eslatma: Mahsulotni tanlagandan keyin foydalanuvchini yana mahsulot ro'yxatida qoldirish yaxshiroq bo'lishi mumkin.
            return;
        }

        bot.sendMessage(chatId, "❌ Bunday mahsulot topilmadi.", KeyboardFactory.createMainMenuKeyboard());
        session.setState(UserState.MAIN_MENU);
    }

    // --- Qidiruv logikasi ---

    private void handleSearch(Long chatId, String query, UserSession session) {
        List<ProductsDTO> results = productsService.searchProducts(query); // Bu metod ProductsService da mavjud bo'lishi kerak

        if (results.isEmpty()) {
            bot.sendMessage(chatId, "❌ Mahsulot topilmadi!", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
        } else {
            session.setLastDisplayedProducts(results);
            session.setState(UserState.SEARCHING_RESULTS);

            List<String> resultNames = results.stream()
                    .map(ProductsDTO::getName)
                    .collect(Collectors.toList());

            bot.sendMessage(chatId, "🔍 Qidiruv natijalari (Mahsulotni tanlang):",
                    KeyboardFactory.createDynamicKeyboard(resultNames, true));
        }
    }
}