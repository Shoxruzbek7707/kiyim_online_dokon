package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.dto.UserDTO;

import java.util.List;

public interface UsersService {
    void updateUser(Integer id, UserDTO dto);
    void deleteUser(Integer id);
    UserDTO getUser(Integer id);
    List<UserDTO> getAllUsers();
}
