package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Reviews;

import java.util.Collection;
import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews,Integer> {
    List<Reviews> findByProductId(Integer productId);

    List<Reviews> findByUserId(Integer userId);
}
