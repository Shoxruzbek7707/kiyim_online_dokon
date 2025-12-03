package uz.pdp.kiyim_online_dokon.bot.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private List<ProductDto> products;
    private double price;
    private String status;
}
