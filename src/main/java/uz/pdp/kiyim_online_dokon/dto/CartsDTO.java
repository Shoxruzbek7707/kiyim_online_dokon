package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartsDTO {
    private Integer id;
    private Integer userId;
    private List<CartItemDTO> items;
}
