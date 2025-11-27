package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Carts;

public interface CartsRepository extends JpaRepository<Carts,Integer> {

    Carts findByUserId(Integer userId);
}
