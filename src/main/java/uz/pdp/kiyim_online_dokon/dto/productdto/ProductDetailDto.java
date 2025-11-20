package uz.pdp.kiyim_online_dokon.dto.productdto;

import uz.pdp.kiyim_online_dokon.dto.productdto.ProductCardDto;

import java.util.List;

public record ProductDetailDto(Long id, String name, String description, Double price, Integer stock, String brand,
                               List<String> images, Double rating, List<ReviewDto> reviews, List<ProductCardDto> related) {}