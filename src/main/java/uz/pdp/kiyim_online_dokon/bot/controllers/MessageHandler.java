package uz.pdp.kiyim_online_dokon.bot.controllers;

import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.kiyim_online_dokon.bot.XaridBot;
import uz.pdp.kiyim_online_dokon.bot.session.UserSession;
import uz.pdp.kiyim_online_dokon.bot.session.UserState;
import uz.pdp.kiyim_online_dokon.bot.utils.KeyboardFactory;
import uz.pdp.kiyim_online_dokon.dto.CategoryDTO;
import uz.pdp.kiyim_online_dokon.dto.OrderDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;
import uz.pdp.kiyim_online_dokon.service.interfaces.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessageHandler {
    private final XaridBot bot;
    private final Map<Long, UserSession> sessions;
    private final ProductsService productsService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final OrderService orderService;
    private final TelegramUserService telegramUserService; // <-- To‘g‘ri service

    public MessageHandler(XaridBot bot,
                          Map<Long, UserSession> sessions,
                          ProductsService productsService,
                          CategoryService categoryService,
                          CartService cartService,
                          OrderService orderService,
                          TelegramUserService telegramUserService) {
        this.bot = bot;
        this.sessions = sessions;
        this.productsService = productsService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.orderService = orderService;
        this.telegramUserService = telegramUserService;
    }

    public void handleMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();
        UserSession session = sessions.computeIfAbsent(chatId, UserSession::new);

        // Buyruqlar (Commands)
        if (text.startsWith("/")) {
            handleCommand(chatId, text, session, message);
            return;
        }

        // Holatga bog'liq matnlar
        if (session.getState() == UserState.SEARCHING) {
            handleSearch(chatId, text, session);
            return;
        }

        if (session.getState() == UserState.CONFIRMING_ORDER) {
            handleOrderConfirmation(chatId, text, session);
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
            case "✅ Buyurtma berish" -> startOrderProcess(chatId, session);
            case "🗑 Savatni tozalash" -> clearCart(chatId, session);
            default -> handleDynamicSelection(chatId, text, session);
        }
    }

    private void handleCommand(Long chatId, String text, UserSession session, Message message) {
        if (text.equals("/start")) {
            // Agar foydalanuvchi hali ro'yxatdan o'tmagan bo'lsa
            if (session.getUserId() == null) {
                registerUser(chatId, session, message);
            } else {
                showMainMenu(chatId, session);
            }
        } else {
            bot.sendMessage(chatId, "❌ Noma'lum buyruq", KeyboardFactory.createMainMenuKeyboard());
        }
    }

    // --- RO'YXATDAN O'TKAZISH FUNKSIYASI ---
    private void registerUser(Long chatId, UserSession session, Message message) {
        try {
            // Telegram ma'lumotlarini olish
            String firstName = message.getFrom().getFirstName();
            String lastName = message.getFrom().getLastName();
            String username = message.getFrom().getUserName();

            // Foydalanuvchini bazaga qo'shish
            Integer userId = telegramUserService.createOrGetUser(
                    chatId,
                    firstName,
                    lastName,
                    username
            );

            // Session'ga userId ni saqlash
            session.setUserId(userId);

            // Xush kelibsiz xabari
            String welcomeMessage = "🎉 Xush kelibsiz, " + firstName + "!\n\n" +
                    "✅ Siz muvaffaqiyatli ro'yxatdan o'tdingiz!\n" +
                    "🛍 Endi siz mahsulotlarni ko'rib, xarid qilishingiz mumkin.";

            bot.sendMessage(chatId, welcomeMessage, KeyboardFactory.createMainMenuKeyboard());
            showMainMenu(chatId, session);

        } catch (Exception e) {
            bot.sendMessage(chatId,
                    "❌ Ro'yxatdan o'tishda xatolik yuz berdi. Iltimos, qaytadan urinib ko'ring.\n" +
                            "Xatolik: " + e.getMessage(),
                    KeyboardFactory.createMainMenuKeyboard());
        }
    }

    // --- Bosh menyu funksiyalari ---

    private void showMainMenu(Long chatId, UserSession session) {
        session.setState(UserState.MAIN_MENU);
        bot.sendMessage(chatId, "🏪 Bosh menu:", KeyboardFactory.createMainMenuKeyboard());
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
        String about = "ℹ️ Biz haqimizda:\n\n" +
                "🏪 Onlayn kiyim do'koni\n" +
                "📦 Keng assortiment\n" +
                "🚚 Tez yetkazib berish\n" +
                "💳 Qulay to'lov usullari\n\n" +
                "📞 Aloqa: +998 XX XXX XX XX";
        bot.sendMessage(chatId, about, KeyboardFactory.createMainMenuKeyboard());
    }

    private void showOrders(Long chatId, UserSession session) {
        Integer userId = session.getUserId();

        if (userId == null) {
            bot.sendMessage(chatId, "❌ Xatolik yuz berdi. Iltimos, /start buyrug'ini qayta yuboring!",
                    KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        List<OrderDTO> orders = orderService.getOrdersByTelegramUserId(userId);

        if (orders.isEmpty()) {
            bot.sendMessage(chatId, "📋 Sizda hali buyurtmalar yo'q.",
                    KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        StringBuilder sb = new StringBuilder("📋 Sizning buyurtmalaringiz:\n\n");

        for (OrderDTO order : orders) {
            sb.append("🆔 Buyurtma #").append(order.getId()).append("\n");
            sb.append("💰 Summa: ").append(order.getTotalPrice()).append(" so'm\n");
            sb.append("📊 Status: ").append(getStatusEmoji(order.getStatus())).append(" ").append(order.getStatus()).append("\n");
            sb.append("📅 Sana: ").append(order.getCreatedAt()).append("\n");
            sb.append("━━━━━━━━━━━━━━━\n");
        }

        bot.sendMessage(chatId, sb.toString(), KeyboardFactory.createMainMenuKeyboard());
    }

    private void showCart(Long chatId, UserSession session) {
        List<ProductsDTO> cartItems = session.getCart();

        if (cartItems.isEmpty()) {
            bot.sendMessage(chatId, "🛒 Savat bo'sh!", KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        StringBuilder sb = new StringBuilder("🛒 Savatingiz:\n\n");
        double total = 0;

        for (ProductsDTO p : cartItems) {
            sb.append("• ").append(p.getName())
                    .append("\n  💰 ").append(p.getPrice()).append(" so'm\n");
            total += p.getPrice();
        }

        sb.append("\n💵 Jami: ").append(total).append(" so'm\n");
        sb.append("📦 Mahsulotlar soni: ").append(cartItems.size());

        bot.sendMessage(chatId, sb.toString(), KeyboardFactory.createCartActionsKeyboard());
        session.setState(UserState.VIEWING_CART);
    }

    private void clearCart(Long chatId, UserSession session) {
        session.clearCart();
        bot.sendMessage(chatId, "🗑 Savat tozalandi!", KeyboardFactory.createMainMenuKeyboard());
        session.setState(UserState.MAIN_MENU);
    }

    private void startOrderProcess(Long chatId, UserSession session) {
        List<ProductsDTO> cartItems = session.getCart();

        if (cartItems.isEmpty()) {
            bot.sendMessage(chatId, "❌ Savat bo'sh! Buyurtma berishdan oldin mahsulot qo'shing.",
                    KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
            return;
        }

        double total = cartItems.stream().mapToDouble(ProductsDTO::getPrice).sum();

        String confirmMessage = "✅ Buyurtmani tasdiqlaysizmi?\n\n" +
                "📦 Mahsulotlar: " + cartItems.size() + " ta\n" +
                "💰 Jami summa: " + total + " so'm\n\n" +
                "Tasdiqlash uchun 'Ha' yoki 'Yo'q' deb yozing.";

        bot.sendMessage(chatId, confirmMessage, KeyboardFactory.createConfirmationKeyboard());
        session.setState(UserState.CONFIRMING_ORDER);
    }

    private void handleOrderConfirmation(Long chatId, String text, UserSession session) {
        if (text.equalsIgnoreCase("Ha") || text.equalsIgnoreCase("✅ Ha")) {
            createOrder(chatId, session);
        } else if (text.equalsIgnoreCase("Yo'q") || text.equalsIgnoreCase("❌ Yo'q")) {
            bot.sendMessage(chatId, "❌ Buyurtma bekor qilindi.", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
        } else {
            bot.sendMessage(chatId, "❓ Iltimos, 'Ha' yoki 'Yo'q' deb javob bering.",
                    KeyboardFactory.createConfirmationKeyboard());
        }
    }

    private void createOrder(Long chatId, UserSession session) {
        Integer telegramUserId = session.getUserId();

        if (telegramUserId == null) {
            bot.sendMessage(chatId, "❌ Xatolik yuz berdi. Iltimos, /start buyrug'ini qayta yuboring!",
                    KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
            return;
        }

        try {
            List<ProductsDTO> cartItems = session.getCart();
            double total = cartItems.stream().mapToDouble(ProductsDTO::getPrice).sum();

            // Telegram foydalanuvchisi uchun buyurtma yaratish
            orderService.createOrderForTelegramUser(telegramUserId, cartItems, total);

            session.clearCart();

            bot.sendMessage(chatId,
                    "✅ Buyurtma muvaffaqiyatli qabul qilindi!\n\n" +
                            "💰 Summa: " + total + " so'm\n" +
                            "📞 Tez orada operatorlarimiz siz bilan bog'lanadi.\n\n" +
                            "Xaridingiz uchun rahmat! 🎉",
                    KeyboardFactory.createMainMenuKeyboard());

            session.setState(UserState.MAIN_MENU);
        } catch (Exception e) {
            bot.sendMessage(chatId, "❌ Buyurtma yaratishda xatolik yuz berdi: " + e.getMessage(),
                    KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
        }
    }

    // --- Dynamic Tugma tanlovini boshqarish ---

    private void handleDynamicSelection(Long chatId, String text, UserSession session) {
        List<CategoryDTO> allCategories = categoryService.getAllCategories();
        boolean isCategory = allCategories.stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(text.trim()));

        if (isCategory) {
            CategoryDTO category = allCategories.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(text.trim()))
                    .findFirst().orElseThrow();

            showProductsByCategory(chatId, session, category.getId(), category.getName());
            return;
        }

        handleProductSelection(chatId, text, session);
    }

    private void showProductsByCategory(Long chatId, UserSession session, Integer categoryId, String categoryName) {
        List<ProductsDTO> products = categoryService.getProductsByCategoryId(categoryId);

        if (products.isEmpty()) {
            bot.sendMessage(chatId, "❌ Bu kategoriyada mahsulotlar topilmadi.", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
            return;
        }

        session.setLastDisplayedProducts(products);
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
            bot.sendMessage(chatId, "❌ Noma'lum mahsulot yoki buyruq!", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
            return;
        }

        ProductsDTO selectedProduct = products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(text.trim()))
                .findFirst().orElse(null);

        if (selectedProduct != null) {
            session.addToCart(selectedProduct);

            String message = "✅ " + selectedProduct.getName() + " savatga qo'shildi!\n\n" +
                    "💰 Narxi: " + selectedProduct.getPrice() + " so'm\n" +
                    "🛒 Savatda: " + session.getCart().size() + " ta mahsulot";

            bot.sendMessage(chatId, message, KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
            return;
        }

        bot.sendMessage(chatId, "❌ Bunday mahsulot topilmadi.", KeyboardFactory.createMainMenuKeyboard());
        session.setState(UserState.MAIN_MENU);
    }

    private void handleSearch(Long chatId, String query, UserSession session) {
        List<ProductsDTO> results = productsService.searchProducts(query);

        if (results.isEmpty()) {
            bot.sendMessage(chatId, "❌ Mahsulot topilmadi!", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
        } else {
            session.setLastDisplayedProducts(results);
            session.setState(UserState.SEARCHING_RESULTS);

            List<String> resultNames = results.stream()
                    .map(ProductsDTO::getName)
                    .collect(Collectors.toList());

            bot.sendMessage(chatId, "🔍 Qidiruv natijalari (" + results.size() + " ta):",
                    KeyboardFactory.createDynamicKeyboard(resultNames, true));
        }
    }

    private String getStatusEmoji(String status) {
        return switch (status) {
            case "PENDING" -> "⏳";
            case "PROCESSING" -> "🔄";
            case "SHIPPED" -> "🚚";
            case "DELIVERED" -> "✅";
            case "CANCELLED" -> "❌";
            default -> "📦";
        };
    }
}