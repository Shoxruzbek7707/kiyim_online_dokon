package uz.pdp.kiyim_online_dokon.dto.userdto;

import uz.pdp.kiyim_online_dokon.entity.Role;

public record UserProfileDto(Long id, String fullName, String email, String phoneNumber, Role role) {}