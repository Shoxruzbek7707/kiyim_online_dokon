package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Addresses;
import uz.pdp.kiyim_online_dokon.entity.Users;

import java.util.List;

public interface AddressesRepository extends JpaRepository<Addresses,Integer> {

    List<Addresses> findAllByUser(Users user);
}
