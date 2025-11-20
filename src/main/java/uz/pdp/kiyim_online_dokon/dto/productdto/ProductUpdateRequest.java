package uz.pdp.kiyim_online_dokon.dto.productdto;

public record ProductUpdateRequest(String name, String description, Double price, Integer stock, String brand, Long categoryId) {}