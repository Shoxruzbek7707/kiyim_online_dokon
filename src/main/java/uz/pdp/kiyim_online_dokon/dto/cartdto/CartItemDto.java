package uz.pdp.kiyim_online_dokon.dto.cartdto;

public record CartItemDto(Long productId, String name, String image, Double price, Integer quantity) {}