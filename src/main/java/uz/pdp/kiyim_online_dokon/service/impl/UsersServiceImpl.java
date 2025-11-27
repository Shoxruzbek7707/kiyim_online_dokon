package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.UserDTO;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.UsersService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private  final UsersRepository usersRepository;



    @Override
    public void updateUser(Integer id, UserDTO dto) {

    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public UserDTO getUser(Integer id) {
        return null;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return List.of();
    }
}
