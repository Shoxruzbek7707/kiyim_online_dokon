package uz.pdp.kiyim_online_dokon.dto;

import lombok.Data;

@Data
public class PasswordResetConfirm {
    private String email;
    private String verificationCode;
    private String newPassword;
}
