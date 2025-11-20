package uz.pdp.kiyim_online_dokon.dto.cartdto;

import java.util.List;

public record CartDto(List<CartItemDto> items, Double totalPrice, Integer totalItems) {}