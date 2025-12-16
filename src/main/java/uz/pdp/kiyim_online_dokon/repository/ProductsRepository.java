package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.Products;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Integer> {
    List<Products> findAllByNameContainingIgnoreCase(String name);
    Products findByName(String name);
    @EntityGraph(attributePaths = {"images"})
    Optional<Products> findById(Integer id);
    @EntityGraph(attributePaths = {"images"})
    List<Products> findAll();

}
