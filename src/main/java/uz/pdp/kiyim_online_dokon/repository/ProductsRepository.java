package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Products;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
    List<Products> findAllByNameContainingIgnoreCase(String name);

}
