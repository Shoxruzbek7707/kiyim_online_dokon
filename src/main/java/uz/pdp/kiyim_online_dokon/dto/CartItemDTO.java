package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemDTO {
    private Integer id;
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
}

