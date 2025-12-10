package uz.pdp.kiyim_online_dokon.bot.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public static ReplyKeyboardMarkup createMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow(List.of(
                new KeyboardButton("📦 Mahsulotlar"),
                new KeyboardButton("🛒 Savatim")
        ));

        KeyboardRow row2 = new KeyboardRow(List.of(
                new KeyboardButton("📋 Buyurtmalarim"),
                new KeyboardButton("ℹ️ Biz haqimizda")
        ));

        KeyboardRow row3 = new KeyboardRow(List.of(
                new KeyboardButton("🔍 Qidirish")
        ));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2, row3))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public static ReplyKeyboardMarkup createBackToMenuKeyboard() {
        KeyboardRow row = new KeyboardRow(List.of(
                new KeyboardButton("🏠 Bosh menu")
        ));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    // Savat uchun klaviatura
    public static ReplyKeyboardMarkup createCartActionsKeyboard() {
        KeyboardRow row1 = new KeyboardRow(List.of(
                new KeyboardButton("✅ Buyurtma berish")
        ));

        KeyboardRow row2 = new KeyboardRow(List.of(
                new KeyboardButton("🗑 Savatni tozalash"),
                new KeyboardButton("🏠 Bosh menu")
        ));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    // Buyurtma tasdiqlash uchun klaviatura
    public static ReplyKeyboardMarkup createConfirmationKeyboard() {
        KeyboardRow row1 = new KeyboardRow(List.of(
                new KeyboardButton("✅ Ha"),
                new KeyboardButton("❌ Yo'q")
        ));

        KeyboardRow row2 = new KeyboardRow(List.of(
                new KeyboardButton("🏠 Bosh menu")
        ));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2))
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    // Mahsulotlar yoki Kategoriyalar ro'yxati uchun universal keyboard
    public static ReplyKeyboardMarkup createDynamicKeyboard(List<String> buttonNames, boolean includeBackButton) {
        List<KeyboardRow> rows = new ArrayList<>();

        // Har 2 ta elementni bitta qatorga joylash
        for (int i = 0; i < buttonNames.size(); i += 2) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(buttonNames.get(i)));
            if (i + 1 < buttonNames.size()) {
                row.add(new KeyboardButton(buttonNames.get(i + 1)));
            }
            rows.add(row);
        }

        if (includeBackButton) {
            KeyboardRow backRow = new KeyboardRow();
            backRow.add(new KeyboardButton("🏠 Bosh menu"));
            rows.add(backRow);
        }

        return ReplyKeyboardMarkup.builder()
                .keyboard(rows)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
}