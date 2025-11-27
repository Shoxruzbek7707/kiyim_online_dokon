package uz.pdp.kiyim_online_dokon.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import uz.pdp.kiyim_online_dokon.dto.ProductImageDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;
import uz.pdp.kiyim_online_dokon.entity.ProductImage;

import java.util.List;

public interface ProductsService {
    ProductsDTO createProduct(ProductsDTO dto);
    ProductsDTO updateProduct(Integer id, ProductsDTO  dto);
    void deleteProduct(Integer id);
    ProductsDTO  getProduct(Integer id);
    List<ProductsDTO > getAllProducts();

    ProductsDTO getProductById(Integer id);

    ProductImageDTO uploadImage(Integer productId, MultipartFile file, boolean isMain);

    ProductImageDTO getImageById(Integer imageId);

    void deleteImage(Integer imageId);
}
