package uz.pdp.kiyim_online_dokon.dto.authdto;

public record AuthResponse(String accessToken, String refreshToken, UserProfileDto user) {}