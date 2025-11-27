package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
