package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import uz.pdp.kiyim_online_dokon.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
}
