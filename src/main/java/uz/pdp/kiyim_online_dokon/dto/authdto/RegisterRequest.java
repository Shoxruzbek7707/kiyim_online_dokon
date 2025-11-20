package uz.pdp.kiyim_online_dokon.dto.authdto;

public record RegisterRequest(String fullName, String email, String phoneNumber, String password) {}