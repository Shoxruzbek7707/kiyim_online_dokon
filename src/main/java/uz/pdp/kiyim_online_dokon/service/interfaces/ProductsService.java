package uz.pdp.kiyim_online_dokon.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import uz.pdp.kiyim_online_dokon.dto.ProductImageDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;
import org.springframework.core.io.Resource; // ✅ Fayl tizimiga o'tilgani uchun qo'shildi

import java.util.List;

public interface ProductsService {

    // --- Products CRUD Operations ---
    ProductsDTO findByName(String name);
    ProductsDTO createProduct(ProductsDTO dto);
    ProductsDTO updateProduct(Integer id, ProductsDTO dto);
    void deleteProduct(Integer id);
    ProductsDTO getProduct(Integer id);
    List<ProductsDTO> getAllProducts();
    ProductsDTO getProductById(Integer id);
    List<ProductsDTO> searchProducts(String query);

    // --- Image Upload and Management ---

    /**
     * Rasm faylini diskka saqlash va uning manzilini DBga yozish
     */
    ProductImageDTO uploadImage(Integer productId, MultipartFile file, boolean isMain);

    /**
     * Rasmning metadata ma'lumotlarini olish (DBdan)
     */
    ProductImageDTO getImageById(Integer imageId);

    /**
     * Mahsulotga tegishli barcha rasmlarning metadata ma'lumotlarini olish
     */
    List<ProductImageDTO> getImagesByProductId(Integer productId); // ✅ Qo'shilgan metod

    /**
     * Rasm yozuvini DBdan va faylni diskdan o'chirish
     */
    void deleteImage(Integer imageId);

    // --- File System Access (Controller uchun yordamchi) ---

    /**
     * Server diskidagi rasmni Resource sifatida o'qib berish.
     * Bu metod Controller orqali chaqiriladi (rasm faylini ko'rsatish uchun).
     */
    Resource loadAsResource(String filename);
}