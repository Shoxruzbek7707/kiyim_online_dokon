package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.repository.UsersRepository;
import uz.pdp.kiyim_online_dokon.service.UsersService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;

    @Override
    public void deleteById(Integer id) {
        usersRepository.deleteById(id);
    }

    @Override
    public void save(Users user) {
        usersRepository.save(user);
    }

    @Override
    public Users findById(Integer id) {
        return usersRepository.findById(id).orElse(null);
    }

    @Override
    public List<Users> findAll() {
        return usersRepository.findAll();
    }
}
