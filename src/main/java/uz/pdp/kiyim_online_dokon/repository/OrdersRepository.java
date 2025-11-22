package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Orders;

public interface OrdersRepository  extends JpaRepository<Orders,Integer> {
}
