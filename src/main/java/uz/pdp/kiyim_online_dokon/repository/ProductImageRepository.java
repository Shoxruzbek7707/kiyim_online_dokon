package uz.pdp.kiyim_online_dokon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.kiyim_online_dokon.entity.ProductImage;
import uz.pdp.kiyim_online_dokon.entity.Products;


public interface ProductImageRepository extends JpaRepository<ProductImage,Integer> {
}
