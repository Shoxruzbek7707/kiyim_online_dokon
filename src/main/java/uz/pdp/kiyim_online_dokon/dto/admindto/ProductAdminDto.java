package uz.pdp.kiyim_online_dokon.dto.admindto;

public record ProductAdminDto(Long id, String name, Double price, Integer stock, String category, boolean active) {}