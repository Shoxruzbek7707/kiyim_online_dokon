package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartsRepository extends JpaRepository<CartItemRepository,Integer> {
}
