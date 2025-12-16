package uz.pdp.kiyim_online_dokon.dto;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String username;  // EMAIL emas, USERNAME
    private String oldPassword;
    private String newPassword;
}
