package uz.pdp.kiyim_online_dokon.bot.dto;
import lombok.Data;

@Data
public class CartItemDto {
    private Long productId;
    private String productName;
    private String imageUrl;
    private double price;
    private int quantity;
}
