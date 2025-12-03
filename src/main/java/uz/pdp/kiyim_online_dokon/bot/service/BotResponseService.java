
package uz.pdp.kiyim_online_dokon.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.pdp.kiyim_online_dokon.bot.client.BackendClient;
import uz.pdp.kiyim_online_dokon.bot.dto.CartItemDto;
import uz.pdp.kiyim_online_dokon.bot.dto.OrderResponse;
import uz.pdp.kiyim_online_dokon.bot.dto.ProductDto;
import uz.pdp.kiyim_online_dokon.bot.util.MessagerBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BotResponseService {

    private final BackendClient backendClient;

    public SendMessage getProductsMessage(String chatId) {
        try {
            List<ProductDto> products = backendClient.getProducts();
            if (products.isEmpty()) {
                return MessagerBuilder.text(chatId, "❗ Hozircha mahsulot yo‘q.");
            }

            StringBuilder sb = new StringBuilder("🛍 *Mahsulotlar ro‘yxati:*\n\n");
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

            for (ProductDto p : products) {
                sb.append("📌 *").append(p.getName()).append("*\n")
                        .append("Narxi: ").append(p.getPrice()).append(" so‘m\n\n");

                InlineKeyboardButton button = InlineKeyboardButton.builder()
                        .text(p.getName() + " (" + p.getPrice() + " so‘m)")
                        .callbackData("product:" + p.getId())
                        .build();

                keyboard.add(List.of(button));
            }

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(keyboard);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(sb.toString())
                    .parseMode("Markdown")
                    .replyMarkup(markup)
                    .build();
        } catch (Exception e) {
            return MessagerBuilder.text(chatId, "❌ Mahsulotlarni yuklashda xatolik: " + e.getMessage());
        }
    }

    public SendMessage getProductDetailMessage(String chatId, Long id) {
        try {
            ProductDto p = backendClient.getProductById(id);
            return MessagerBuilder.productDetail(chatId, p);
        } catch (Exception e) {
            return MessagerBuilder.text(chatId, "❌ Mahsulot topilmadi: " + e.getMessage());
        }
    }

    public SendMessage addToCartMessage(String chatId, String token, Long productId) {
        try {
            backendClient.addToCart(token, productId);
            return MessagerBuilder.text(chatId, "🛒 Mahsulot savatchaga qo‘shildi!");
        } catch (Exception e) {
            return MessagerBuilder.text(chatId, "❌ Qo‘shishda xatolik: " + e.getMessage());
        }
    }

    public SendMessage getCartMessage(String chatId, String token) {
        try {
            List<CartItemDto> items = backendClient.getCart(token);
            if (items.isEmpty()) {
                return MessagerBuilder.text(chatId, "🛒 Savatcha bo‘sh.");
            }

            StringBuilder sb = new StringBuilder("🛒 *Savatchadagi mahsulotlar:*\n\n");
            double total = 0;
            for (CartItemDto item : items) {
                double sum = item.getPrice() * item.getQuantity();
                sb.append("• ").append(item.getProductName()).append(" x ").append(item.getQuantity())
                        .append(" = ").append(sum).append(" so‘m\n");
                total += sum;
            }
            sb.append("\n**Jami: **").append(total).append(" so‘m");

            InlineKeyboardButton checkoutButton = InlineKeyboardButton.builder()
                    .text("💳 Buyurtma berish")
                    .callbackData("checkout")
                    .build();

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(List.of(checkoutButton)));

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(sb.toString())
                    .parseMode("Markdown")
                    .replyMarkup(markup)
                    .build();
        } catch (Exception e) {
            return MessagerBuilder.text(chatId, "❌ Savatchani yuklashda xatolik: " + e.getMessage());
        }
    }

    public SendMessage checkoutMessage(String chatId, String token) {
        try {
            OrderResponse order = backendClient.checkout(token);
            String text = "✅ Buyurtma muvaffaqiyatli yaratildi!\n\n" +
                    "Buyurtma ID: " + order.getOrderId() + "\n" +
                    "Jami summa: " + order.getTotalPrice() + " so‘m\n" +
                    "Holati: " + order.getStatus() + "\n" +
                    (order.getMessage() != null ? order.getMessage() : "");
            return MessagerBuilder.text(chatId, text);
        } catch (Exception e) {
            return MessagerBuilder.text(chatId, "❌ Buyurtma berishda xatolik: " + e.getMessage());
        }
    }
}