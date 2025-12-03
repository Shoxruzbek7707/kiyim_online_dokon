package uz.pdp.kiyim_online_dokon.bot.dto;
import lombok.Data;

@Data
public class OrderResponse {
    private Long orderId;
    private double totalPrice;
    private String status;     // "CREATED", "CONFIRMED", "DELIVERING", ...
    private String message;    // Telegramga chiqarish uchun qulay matn
}
