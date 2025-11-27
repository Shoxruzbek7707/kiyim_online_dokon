package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String username;
    private boolean enabled;

    private Set<Integer> roleIds;
    private Set<Integer> permissionIds;

    private Integer cartId;

    public UserDTO() {

    }
}
