package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Transactions;

import java.util.Collection;
import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions,Integer> {
    List<Transactions> findByOrderId(Integer orderId);

    List<Transactions> findByUserId(Integer userId);
}
