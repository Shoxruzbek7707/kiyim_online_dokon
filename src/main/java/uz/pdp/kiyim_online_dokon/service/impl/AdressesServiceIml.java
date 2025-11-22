package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.entity.Users;
import uz.pdp.kiyim_online_dokon.repository.AdressesRepository;
import uz.pdp.kiyim_online_dokon.service.AdressesService;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AdressesServiceIml implements AdressesService {
    private final AdressesRepository adressesRepository;

    @Override
    public void deleteById(Integer id) {
        adressesRepository.deleteById(id);
    }

    @Override
    public void save(Users user) {

    }

    @Override
    public Users findById(Integer id) {
        return null;
    }

    @Override
    public List<Users> findAll() {
        return List.of();
    }
}
