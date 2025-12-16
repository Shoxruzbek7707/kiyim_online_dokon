package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.ProductImage;

import java.util.List;
import java.util.Optional;


public interface ProductImageRepository extends JpaRepository<ProductImage,Integer> {
    Optional<ProductImage> findByProductIdAndIsMainTrue(Integer productId);

    Optional<ProductImage> findFirstByProductId(Integer productId);

    List<ProductImage> findByProductId(Integer productId);
}
