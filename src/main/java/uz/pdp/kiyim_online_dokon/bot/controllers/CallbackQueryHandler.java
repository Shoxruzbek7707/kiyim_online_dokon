package uz.pdp.kiyim_online_dokon.bot.controllers;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.pdp.kiyim_online_dokon.bot.XaridBot;
import uz.pdp.kiyim_online_dokon.bot.session.UserSession;
import uz.pdp.kiyim_online_dokon.bot.utils.KeyboardFactory;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;
import uz.pdp.kiyim_online_dokon.service.interfaces.ProductsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CallbackQueryHandler {
    private final XaridBot bot;
    private final Map<Long, UserSession> sessions;
    private final ProductsService productsService;
    private final Map<String, Integer> tempQuantities = new HashMap<>();

    public CallbackQueryHandler(XaridBot bot, Map<Long, UserSession> sessions, ProductsService productsService) {
        this.bot = bot;
        this.sessions = sessions;
        this.productsService = productsService;
    }

    public static ReplyKeyboardMarkup createLocationKeyboard() {
        KeyboardButton locationButton = new KeyboardButton("📍 Lokatsiyani yuborish");
        locationButton.setRequestLocation(true);

        KeyboardRow row = new KeyboardRow();
        row.add(locationButton);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .selective(false)  // Buni qo'shing
                .build();
    }

    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String data = callbackQuery.getData();
        Integer messageId = callbackQuery.getMessage().getMessageId();

        UserSession session = sessions.get(chatId);
        if (session == null) {
            bot.answerCallbackQuery(callbackQuery.getId(), "❌ Sessiya yo'qolgan. /start bosing");
            return;
        }

        String[] parts = data.split(":");
        String action = parts[0];
        Integer productId = parts.length > 1 ? Integer.parseInt(parts[1]) : null;

        switch (action) {
            case "+" -> handleIncrement(callbackQuery, chatId, messageId, productId);
            case "-" -> handleDecrement(callbackQuery, chatId, messageId, productId);
            case "add_to_cart" -> handleAddToCart(callbackQuery, chatId, session, productId);
            case "count" -> bot.answerCallbackQuery(callbackQuery.getId(), "Miqdor: " + getTempQuantity(chatId, productId));
            default -> bot.answerCallbackQuery(callbackQuery.getId(), "❌ Noma'lum amal");
        }
    }

    private void handleIncrement(CallbackQuery cq, Long chatId, Integer msgId, Integer productId) {
        String key = chatId + ":" + productId;
        int qty = tempQuantities.getOrDefault(key, 1) + 1;
        tempQuantities.put(key, qty);
        updateQuantityButtons(chatId, msgId, productId, qty);
        bot.answerCallbackQuery(cq.getId(), "✅ " + qty);
    }

    private void handleDecrement(CallbackQuery cq, Long chatId, Integer msgId, Integer productId) {
        String key = chatId + ":" + productId;
        int qty = tempQuantities.getOrDefault(key, 1);
        if (qty > 1) {
            qty--;
            tempQuantities.put(key, qty);
            updateQuantityButtons(chatId, msgId, productId, qty);
            bot.answerCallbackQuery(cq.getId(), "✅ " + qty);
        } else {
            bot.answerCallbackQuery(cq.getId(), "⚠️ Minimal 1 ta");
        }
    }

    private void handleAddToCart(CallbackQuery cq, Long chatId, UserSession session, Integer productId) {
        try {
            ProductsDTO product = productsService.getProductById(productId);
            if (product == null) {
                bot.answerCallbackQuery(cq.getId(), "❌ Mahsulot topilmadi");
                return;
            }

            String key = chatId + ":" + productId;
            int qty = tempQuantities.getOrDefault(key, 1);

            for (int i = 0; i < qty; i++) {
                session.addToCart(product);
            }
            tempQuantities.remove(key);

            bot.answerCallbackQuery(cq.getId(), "✅ Savatga qo'shildi!");
            bot.sendMessage(chatId, """
                    ✅ %s (%d ta) savatga qo'shildi!
                    🛒 Jami: %d ta | 💰 %,.0f so'm
                    """.formatted(product.getName(), qty, session.getTotalItems(), session.getTotalPrice()),
                    KeyboardFactory.createMainMenuKeyboard());

        } catch (Exception e) {
            bot.answerCallbackQuery(cq.getId(), "❌ Xatolik");
            e.printStackTrace();
        }
    }

    private void updateQuantityButtons(Long chatId, Integer messageId, Integer productId, int quantity) {
        EditMessageReplyMarkup edit = new EditMessageReplyMarkup();
        edit.setChatId(chatId.toString());
        edit.setMessageId(messageId);
        edit.setReplyMarkup(createUpdatedKeyboard(productId, quantity));
        bot.editMessageReplyMarkup(edit);
    }

    private InlineKeyboardMarkup createUpdatedKeyboard(Integer productId, int quantity) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(btn("➖", "-:" + productId));
        row1.add(btn(String.valueOf(quantity), "count:" + productId));
        row1.add(btn("➕", "+:" + productId));

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(btn("🛒 Savatga qo'shish", "add_to_cart:" + productId));

        rows.add(row1);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardButton btn(String text, String callback) {
        InlineKeyboardButton b = new InlineKeyboardButton(text);
        b.setCallbackData(callback);
        return b;
    }

    private int getTempQuantity(Long chatId, Integer productId) {
        return tempQuantities.getOrDefault(chatId + ":" + productId, 1);
    }
}