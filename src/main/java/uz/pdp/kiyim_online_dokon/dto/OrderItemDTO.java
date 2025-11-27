package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    private Integer id;
    private Integer productId;
    private Double priceAtPurchase;
    private Integer quantity;
}
