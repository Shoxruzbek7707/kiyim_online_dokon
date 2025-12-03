
package uz.pdp.kiyim_online_dokon.bot.util;

import uz.pdp.kiyim_online_dokon.bot.dto.ProductDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class MessagerBuilder {

    public static SendMessage text(String chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    public static SendMessage markdown(String chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown")
                .build();
    }

    public static SendMessage productDetail(String chatId, ProductDto p) {
        String text = "*" + p.getName() + "*\n\n" +
                p.getDescription() + "\n\n" +
                "Narxi: " + p.getPrice() + " so‘m";

        InlineKeyboardButton addButton = InlineKeyboardButton.builder()
                .text("🛒 Savatchaga qo‘shish")
                .callbackData("addcart:" + p.getId())
                .build();

        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("⬅️ Orqaga")
                .callbackData("products")
                .build();

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(addButton);
        row.add(backButton);

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("Markdown")
                .replyMarkup(markup)
                .build();
    }
}
