package uz.pdp.kiyim_online_dokon.bot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.pdp.kiyim_online_dokon.bot.dto.ProductDto;

import java.util.List;

public class MessageBuilder {

    // MarkdownV2 da maxsus belgilarni escape qilish
    private static String escape(String text) {
        if (text == null || text.isEmpty()) return "";
        return text.replace("\\", "\\\\")
                .replace("_", "\\_")
                .replace("*", "\\*")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("~", "\\~")
                .replace("`", "\\`")
                .replace(">", "\\>")
                .replace("#", "\\#")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("|", "\\|")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace(".", "\\.")
                .replace("!", "\\!");
    }

    // Oddiy matnli xabar
    public static SendMessage text(String chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    // MarkdownV2 formatdagi xabar (eng chiroyli va zamonaviy)
    public static SendMessage markdown(String chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(escape(text))
                .parseMode("MarkdownV2")
                .build();
    }

    // Mahsulot kartochkasi — juda chiroyli ko‘rinish
    public static SendMessage productDetail(String chatId, ProductDto p) {
        String name = escape(p.getName());
        String desc = escape(p.getDescription());

        String text = """
                *%s*
                                
                %s
                                
                *Narxi:* %,.0f so‘m
                """.formatted(name, desc.isBlank() ? "_Tavsif yo‘q_" : desc, p.getPrice());

        var addToCart = InlineKeyboardButton.builder()
                .text("Savatchaga qo‘shish")
                .callbackData("addcart:" + p.getId())
                .build();

        var back = InlineKeyboardButton.builder()
                .text("Orqaga")
                .callbackData("products")
                .build();

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(addToCart))
                .keyboardRow(List.of(back))
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("MarkdownV2")
                .replyMarkup(keyboard)
                .build();
    }

    // Agar bir qatorga ikkita tugma qo‘ymoqchi bo‘lsangiz
    public static SendMessage productDetailInline(String chatId, ProductDto p) {
        String text = """
                *%s*
                                
                %s
                                
                *Narxi:* %,.0f so‘m
                """.formatted(escape(p.getName()),
                escape(p.getDescription()).isBlank() ? "_Tavsif yo‘q_" : escape(p.getDescription()),
                p.getPrice());

        var addToCart = InlineKeyboardButton.builder()
                .text("Savatchaga qo‘shish")
                .callbackData("addcart:" + p.getId())
                .build();

        var back = InlineKeyboardButton.builder()
                .text("Orqaga")
                .callbackData("products")
                .build();

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(addToCart, back)) // ikkalasi bir qatorda
                .build();

        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("MarkdownV2")
                .replyMarkup(keyboard)
                .build();
    }
}