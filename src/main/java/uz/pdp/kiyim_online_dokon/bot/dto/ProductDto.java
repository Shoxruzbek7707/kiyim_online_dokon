package uz.pdp.kiyim_online_dokon.bot.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
}
