package uz.pdp.kiyim_online_dokon.bot.controllers;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage; // SendMessage import qilindi
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
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
    private final OrderService orderService;
    private final TelegramUserService telegramUserService;

    public MessageHandler(XaridBot bot,
                          Map<Long, UserSession> sessions,
                          ProductsService productsService,
                          CategoryService categoryService,
                          OrderService orderService,
                          TelegramUserService telegramUserService) {
        this.bot = bot;
        this.sessions = sessions;
        this.productsService = productsService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.telegramUserService = telegramUserService;
    }

    public void handleMessage(Message message) {
        Long chatId = message.getChatId();
        UserSession session = sessions.computeIfAbsent(chatId, UserSession::new);

        if (message.hasLocation() && session.getState() == UserState.WAITING_FOR_LOCATION) {
            handleLocationReceived(chatId, message.getLocation(), session);
            return;
        }

        String text = message.getText();

        if (text != null && text.startsWith("/")) {
            handleCommand(chatId, text, session, message);
            return;
        }

        if (session.getState() == UserState.SEARCHING) {
            handleSearch(chatId, text, session);
            return;
        }

        if (session.getState() == UserState.CONFIRMING_ORDER) {
            handleOrderConfirmation(chatId, text, session);
            return;
        }

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
            if (session.getUserId() == null) {
                registerUser(chatId, session, message);
            } else {
                showMainMenu(chatId, session);
            }
        } else {
            bot.sendMessage(chatId, "❌ Noma'lum buyruq", KeyboardFactory.createMainMenuKeyboard());
        }
    }

    private void registerUser(Long chatId, UserSession session, Message message) {
        try {
            String firstName = message.getFrom().getFirstName();
            String lastName = message.getFrom().getLastName() != null ? message.getFrom().getLastName() : "";
            String username = message.getFrom().getUserName();

            Integer userId = telegramUserService.createOrGetUser(chatId, firstName, lastName, username);
            session.setUserId(userId);

            bot.sendMessage(chatId, """
                    🎉 Xush kelibsiz, %s!
                    
                    ✅ Muvaffaqiyatli ro'yxatdan o'tdingiz.
                    🛍 Endi mahsulotlarni ko'rib, xarid qilishingiz mumkin.
                    """.formatted(firstName), KeyboardFactory.createMainMenuKeyboard());

            showMainMenu(chatId, session);
        } catch (Exception e) {
            bot.sendMessage(chatId, "❌ Ro'yxatdan o'tishda xatolik yuz berdi.", KeyboardFactory.createMainMenuKeyboard());
        }
    }

    private void showMainMenu(Long chatId, UserSession session) {
        session.setState(UserState.MAIN_MENU);
        bot.sendMessage(chatId, "🏪 Bosh menyudasiz:", KeyboardFactory.createMainMenuKeyboard());
    }

    private void showCategories(Long chatId, UserSession session) {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            bot.sendMessage(chatId, "❌ Hozircha kategoriyalar mavjud emas.", KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        List<String> names = categories.stream().map(CategoryDTO::getName).collect(Collectors.toList());
        bot.sendMessage(chatId, "📦 Kategoriyani tanlang:", KeyboardFactory.createDynamicKeyboard(names, true));
    }

    private void showProductsByCategory(Long chatId, UserSession session, Integer categoryId, String categoryName) {
        List<ProductsDTO> products = categoryService.getProductsByCategoryId(categoryId);
        if (products.isEmpty()) {
            bot.sendMessage(chatId, "❌ Bu kategoriyada mahsulotlar yo'q.", KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        session.setLastDisplayedProducts(products);
        session.setState(UserState.SELECTING_PRODUCT);

        List<String> names = products.stream().map(ProductsDTO::getName).collect(Collectors.toList());
        bot.sendMessage(chatId, "🛍 " + categoryName + " bo'limidagi mahsulotlar:",
                KeyboardFactory.createDynamicKeyboard(names, true));
    }

    private void handleDynamicSelection(Long chatId, String text, UserSession session) {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        boolean isCategory = categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(text.trim()));

        if (isCategory) {
            CategoryDTO category = categories.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(text.trim()))
                    .findFirst()
                    .orElse(null);

            if (category != null) {
                showProductsByCategory(chatId, session, category.getId(), category.getName());
            }
            return;
        }

        List<ProductsDTO> lastProducts = session.getLastDisplayedProducts();
        if (lastProducts == null || lastProducts.isEmpty()) {
            bot.sendMessage(chatId, "❌ Mahsulot tanlashda xatolik.", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
            return;
        }

        ProductsDTO selected = lastProducts.stream()
                .filter(p -> p.getName().equalsIgnoreCase(text.trim()))
                .findFirst()
                .orElse(null);

        if (selected != null) {
            showProductDetails(chatId, selected, session);
        } else {
            bot.sendMessage(chatId, "❌ Bunday mahsulot topilmadi.", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
        }
    }

    private void showProductDetails(Long chatId, ProductsDTO product, UserSession session) {

        String caption = """
            📦 %s
            
            💰 Narxi: %,.0f so'm
            📝 %s
            
            Miqdorni tanlang va savatga qo'shing!
            """.formatted(
                product.getName(),
                product.getPrice(),
                product.getDescription() != null ? product.getDescription() : "Tavsif mavjud emas"
        );

        try {
            if (product.getMainImageUrl() != null && !product.getMainImageUrl().isBlank()) {

                String filename = product.getMainImageUrl()
                        .replace("/api/products/images/display/", "");

                java.io.File imageFile =
                        new java.io.File("uploads/product_images/" + filename);

                if (imageFile.exists()) {
                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId.toString());
                    sendPhoto.setPhoto(new InputFile(imageFile));
                    sendPhoto.setCaption(caption);
                    sendPhoto.setReplyMarkup(
                            KeyboardFactory.createProductDetailKeyboard(product.getId())
                    );

                    bot.execute(sendPhoto);
                    return;
                }
            }

        } catch (Exception e) {
            System.err.println("Rasm yuborishda xatolik: " + e.getMessage());
        }

        bot.sendMessage(
                chatId,
                "📷 Rasm mavjud emas\n\n" + caption,
                KeyboardFactory.createProductDetailKeyboard(product.getId())
        );
    }


    private void startSearch(Long chatId, UserSession session) {
        session.setState(UserState.SEARCHING);
        bot.sendMessage(chatId, "🔍 Mahsulot nomini yozing:", KeyboardFactory.createBackToMenuKeyboard());
    }

    private void handleSearch(Long chatId, String query, UserSession session) {
        List<ProductsDTO> results = productsService.searchProducts(query);
        if (results.isEmpty()) {
            bot.sendMessage(chatId, "❌ Hech narsa topilmadi.", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
        } else {
            session.setLastDisplayedProducts(results);
            session.setState(UserState.SEARCHING_RESULTS);

            List<String> names = results.stream().map(ProductsDTO::getName).collect(Collectors.toList());
            bot.sendMessage(chatId, "🔍 Qidiruv natijalari (" + results.size() + " ta):",
                    KeyboardFactory.createDynamicKeyboard(names, true));
        }
    }

    private void showAbout(Long chatId) {
        bot.sendMessage(chatId, """
                ℹ️ Biz haqimizda:
                
                🏪 Kiyim-online - sifatli kiyimlar onlayn do'koni
                📦 Keng tanlov va tez yetkazib berish
                🚚 O'zbekiston bo'ylab yetkazib berish
                💳 Naqd va karta orqali to'lov
                
                📞 Aloqa: +998 95 898 45 55
                """, KeyboardFactory.createMainMenuKeyboard());
    }

    private void showOrders(Long chatId, UserSession session) {
        if (session.getUserId() == null) {
            bot.sendMessage(chatId, "❌ Iltimos, /start buyrug'ini yuboring.", KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        List<OrderDTO> orders = orderService.getOrdersByTelegramUserId(session.getUserId());
        if (orders.isEmpty()) {
            bot.sendMessage(chatId, "📋 Sizda hali buyurtma yo'q.", KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        StringBuilder sb = new StringBuilder("📋 Sizning buyurtmalaringiz:\n\n");
        for (OrderDTO order : orders) {
            sb.append("🆔 Buyurtma #").append(order.getId()).append("\n")
                    .append("💰 Summa: ").append(String.format("%,.0f", order.getTotalPrice())).append(" so'm\n")
                    .append("📊 Holati: ").append(getStatusEmoji(order.getStatus())).append(" ").append(order.getStatus()).append("\n")
                    .append("📅 Sana: ").append(order.getCreatedAt()).append("\n")
                    .append("━━━━━━━━━━━━━━━\n\n");
        }

        bot.sendMessage(chatId, sb.toString(), KeyboardFactory.createMainMenuKeyboard());
    }

    private void showCart(Long chatId, UserSession session) {
        if (session.isCartEmpty()) {
            bot.sendMessage(chatId, "🛒 Savatingiz bo'sh!", KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        StringBuilder sb = new StringBuilder("🛒 Savatingiz:\n\n");
        int totalItems = 0;
        for (UserSession.CartItem item : session.getCartItems()) {
            ProductsDTO p = item.getProduct();
            sb.append("• ").append(p.getName()).append("\n")
                    .append("  💰 ").append(String.format("%,.0f", p.getPrice())).append(" so'm x ")
                    .append(item.getQuantity()).append(" = ")
                    .append(String.format("%,.0f", item.getTotalPrice())).append(" so'm\n\n");
            totalItems += item.getQuantity();
        }
        sb.append("━━━━━━━━━━━━━━━\n")
                .append("📦 Jami mahsulot: ").append(totalItems).append(" ta\n")
                .append("💰 Umumiy summa: ").append(String.format("%,.0f", session.getTotalPrice())).append(" so'm");

        bot.sendMessage(chatId, sb.toString(), KeyboardFactory.createCartActionsKeyboard());
        session.setState(UserState.VIEWING_CART);
    }

    private void clearCart(Long chatId, UserSession session) {
        session.clearCart();
        bot.sendMessage(chatId, "🗑 Savat tozalandi!", KeyboardFactory.createMainMenuKeyboard());
        session.setState(UserState.MAIN_MENU);
    }

    private void startOrderProcess(Long chatId, UserSession session) {
        if (session.isCartEmpty()) {
            bot.sendMessage(chatId, "❌ Savat bo'sh! Avval mahsulot qo'shing.", KeyboardFactory.createMainMenuKeyboard());
            return;
        }

        bot.sendMessage(chatId, """
                ✅ Buyurtmani tasdiqlaysizmi?
                
                📦 Mahsulotlar: %d ta
                💰 Jami summa: %,.0f so'm
                
                "✅ Ha" yoki "❌ Yo'q" deb yozing.
                """.formatted(session.getTotalItems(), session.getTotalPrice()),
                KeyboardFactory.createConfirmationKeyboard());

        session.setState(UserState.CONFIRMING_ORDER);
    }

    private void handleOrderConfirmation(Long chatId, String text, UserSession session) {
        if (text.equalsIgnoreCase("Ha") || text.equalsIgnoreCase("✅ Ha")) {
            createOrder(chatId, session);
        } else if (text.equalsIgnoreCase("Yo'q") || text.equalsIgnoreCase("❌ Yo'q")) {
            bot.sendMessage(chatId, "❌ Buyurtma bekor qilindi.", KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
        } else {
            bot.sendMessage(chatId, "❓ Iltimos, faqat 'Ha' yoki 'Yo'q' deb javob bering.", KeyboardFactory.createConfirmationKeyboard());
        }
    }

    private void createOrder(Long chatId, UserSession session) {
        try {
            int itemsCount = session.getTotalItems();
            double totalPrice = session.getTotalPrice();
            List<ProductsDTO> cartItems = session.getCart();

            orderService.createOrderForTelegramUser(session.getUserId(), cartItems, totalPrice);

            session.clearCart();

            bot.sendMessage(chatId, """
                    🎉 Buyurtmangiz muvaffaqiyatli qabul qilindi!
                    
                    💰 Jami summa: %,.0f so'm
                    📦 Mahsulotlar soni: %d ta
                    
                    🚚 Yetkazib berish uchun joriy joylashuvingizni yuboring:
                    """.formatted(totalPrice, itemsCount),
                    KeyboardFactory.createMainMenuKeyboard());

            requestLocation(chatId, session);

        } catch (Exception e) {
            System.err.println("Buyurtma yaratishda xatolik: " + e.getMessage());
            e.printStackTrace();
            bot.sendMessage(chatId, "❌ Buyurtma yaratishda xatolik yuz berdi.",
                    KeyboardFactory.createMainMenuKeyboard());
            session.setState(UserState.MAIN_MENU);
        }
    }

    private void requestLocation(Long chatId, UserSession session) {
        session.setState(UserState.WAITING_FOR_LOCATION);

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText("📍 Iltimos, quyidagi tugmani bosib lokatsiyangizni yuboring:");
        msg.setReplyMarkup(KeyboardFactory.createLocationKeyboard());

        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleLocationReceived(Long chatId, Location location, UserSession session) {
        session.setState(UserState.MAIN_MENU);

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText("✅ Rahmat!\n\n🏪 Bosh menyu:");
        msg.setReplyMarkup(KeyboardFactory.createMainMenuKeyboard());

        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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