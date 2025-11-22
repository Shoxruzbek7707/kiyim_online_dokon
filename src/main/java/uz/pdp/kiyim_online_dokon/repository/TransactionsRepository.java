package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Transactions;

public interface TransactionsRepository extends JpaRepository<Transactions,Integer> {
}
