package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.kiyim_online_dokon.dto.ProductImageDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;

import uz.pdp.kiyim_online_dokon.entity.Category;
import uz.pdp.kiyim_online_dokon.entity.ProductImage;
import uz.pdp.kiyim_online_dokon.entity.Products;
import uz.pdp.kiyim_online_dokon.repository.CategoryRepository;
import uz.pdp.kiyim_online_dokon.repository.ProductImageRepository;
import uz.pdp.kiyim_online_dokon.repository.ProductsRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.ProductsService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {
    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    private ProductsDTO toDTO(Products product) {
        ProductsDTO dto = new ProductsDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setBrand(product.getBrand());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        dto.setCreatedAt(product.getCreatedAt());

        if (product.getImages() != null) {
            dto.setImageIds(product.getImages().stream().map(img -> img.getId()).collect(Collectors.toList()));
        }

        return dto;
    }

    @Override
    public List<ProductsDTO> searchProducts(String query) {
        // 1. Qidiruv so'zini kichik harfga o'tkazamiz va bo'shliqlarni olib tashlaymiz
        String normalizedQuery = query.trim();

        // 2. Repository orqali qidiruvni amalga oshirish
        // Quyidagi metod ProductsRepositoryda yaratilgan bo'lishi kerak.
        List<Products> results = productsRepository.findAllByNameContainingIgnoreCase(normalizedQuery);

        // 3. Entity ro'yxatini DTO ro'yxatiga o'girish
        return results.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private Products toEntity(ProductsDTO dto) {
        Products product = new Products();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setBrand(dto.getBrand());
        product.setCreatedAt(dto.getCreatedAt());
        return product;
    }

    private ProductImageDTO toImageDTO(ProductImage image) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(image.getId());
        dto.setProductId(image.getProduct().getId());
        dto.setImageBytes(image.getImageBytes());
        dto.setIsMain(image.getIsMain());
        return dto;
    }

    @Override
    public ProductsDTO createProduct(ProductsDTO dto) {
        Products product = toEntity(dto);
        product.setCreatedAt(LocalDateTime.now());
        Products savedProduct = productsRepository.save(product);
        return toDTO(savedProduct);

    }

    @Override
    public ProductsDTO updateProduct(Integer id, ProductsDTO dto) {
        Products existing = productsRepository.findById(id).orElse(null);

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());
        existing.setBrand(dto.getBrand());

        Category category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        existing.setCategory(category);

        if (dto.getCategoryId() != null) {
            Products updatedProduct = productsRepository.save(existing);
            return toDTO(updatedProduct);
        }

        Products updated = productsRepository.save(existing);
        return toDTO(updated);
    }

    @Override
    public void deleteProduct(Integer id) {
        productsRepository.deleteById(id);

    }

    @Override
    public ProductsDTO getProduct(Integer id) {
        return toDTO(productsRepository.findById(id).orElse(null));
    }

    @Override
    public List<ProductsDTO> getAllProducts() {
        return productsRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductsDTO getProductById(Integer id) {
        Products products = productsRepository.findById(id).orElse(null);
        return toDTO(products);
    }

    @Override
    public ProductImageDTO uploadImage(Integer productId, MultipartFile file, boolean isMain) {
        Products product = productsRepository.findById(productId).orElse(null);

        ProductImage  image = new ProductImage();
        image.setProduct(product);
        try {
            image.setImageBytes(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        image.setIsMain(isMain);

        ProductImage saved;
        saved = productImageRepository.save(image);
        return toImageDTO(saved);


    }

    @Override
    public ProductImageDTO getImageById(Integer imageId) {
        return toImageDTO(productImageRepository.findById(imageId).orElse(null));
    }

    @Override
    public void deleteImage(Integer imageId) {
        productImageRepository.deleteById(imageId);

    }
}
