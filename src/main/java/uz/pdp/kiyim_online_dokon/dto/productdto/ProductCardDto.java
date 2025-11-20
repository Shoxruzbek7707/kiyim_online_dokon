package uz.pdp.kiyim_online_dokon.dto.productdto;

public record ProductCardDto(Long id, String name, Double price, String mainImage, Double rating, Integer reviewCount) {}