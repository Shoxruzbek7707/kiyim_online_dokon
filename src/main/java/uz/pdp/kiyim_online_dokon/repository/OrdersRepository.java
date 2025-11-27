package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Orders;

import java.util.Collection;
import java.util.List;

public interface OrdersRepository  extends JpaRepository<Orders,Integer> {
    List<Orders> findByUserId(Integer userId);
}
