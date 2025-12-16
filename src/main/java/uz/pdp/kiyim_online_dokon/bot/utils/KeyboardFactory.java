package uz.pdp.kiyim_online_dokon.bot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public static ReplyKeyboardMarkup createMainMenuKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("📦 Mahsulotlar"));
        row1.add(new KeyboardButton("🛒 Savatim"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("📋 Buyurtmalarim"));
        row2.add(new KeyboardButton("ℹ️ Biz haqimizda"));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("🔍 Qidirish"));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboard)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)  // Har doim ko'rinib turadi
                .build();
    }

    public static ReplyKeyboardMarkup createBackToMenuKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("🏠 Bosh menu"));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .resizeKeyboard(true)
                .build();
    }

    public static ReplyKeyboardMarkup createCartActionsKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("✅ Buyurtma berish"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("🗑 Savatni tozalash"));
        row2.add(new KeyboardButton("🏠 Bosh menu"));

        keyboard.add(row1);
        keyboard.add(row2);

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboard)
                .resizeKeyboard(true)
                .build();
    }

    public static ReplyKeyboardMarkup createConfirmationKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("✅ Ha"));
        row1.add(new KeyboardButton("❌ Yo'q"));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("🏠 Bosh menu"));

        keyboard.add(row1);
        keyboard.add(row2);

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboard)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .build();
    }

    public static ReplyKeyboardMarkup createDynamicKeyboard(List<String> buttonNames, boolean includeBackButton) {
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (int i = 0; i < buttonNames.size(); i += 2) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(buttonNames.get(i)));
            if (i + 1 < buttonNames.size()) {
                row.add(new KeyboardButton(buttonNames.get(i + 1)));
            }
            keyboard.add(row);
        }

        if (includeBackButton) {
            KeyboardRow backRow = new KeyboardRow();
            backRow.add(new KeyboardButton("🏠 Bosh menu"));
            keyboard.add(backRow);
        }

        return ReplyKeyboardMarkup.builder()
                .keyboard(keyboard)
                .resizeKeyboard(true)
                .build();
    }

    public static InlineKeyboardMarkup createProductDetailKeyboard(Integer productId) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> quantityRow = new ArrayList<>();
        quantityRow.add(createInlineButton("➖", "-:" + productId));
        quantityRow.add(createInlineButton("1", "count:" + productId));
        quantityRow.add(createInlineButton("➕", "+:" + productId));

        List<InlineKeyboardButton> cartRow = new ArrayList<>();
        cartRow.add(createInlineButton("🛒 Savatga qo'shish", "add_to_cart:" + productId));

        keyboard.add(quantityRow);
        keyboard.add(cartRow);

        return new InlineKeyboardMarkup(keyboard);
    }

    private static InlineKeyboardButton createInlineButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData);
        return button;
    }

    public static ReplyKeyboardMarkup createLocationKeyboard() {
        KeyboardButton locationButton = new KeyboardButton("📍 Lokatsiyani yuborish");
        locationButton.setRequestLocation(true);

        KeyboardRow row = new KeyboardRow();
        row.add(locationButton);

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)  // Lokatsiya yuborilgandan keyin yashiriladi
                .build();
    }
}