package uz.pdp.kiyim_online_dokon.service;

import uz.pdp.kiyim_online_dokon.entity.Users;

import java.util.List;

public interface UsersService {
    void deleteById(Integer id);
    void save(Users user);
    Users findById(Integer id);
    List<Users> findAll();
}
