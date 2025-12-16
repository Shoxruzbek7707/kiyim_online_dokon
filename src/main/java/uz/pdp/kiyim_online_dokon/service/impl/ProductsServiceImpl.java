package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 🔥 barcha READ methodlar uchun
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    private final Path rootLocation = Paths.get("uploads/product_images");

    // ===================== MAPPING =====================

    private ProductsDTO toDTO(Products product) {
        ProductsDTO dto = new ProductsDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCreatedAt(product.getCreatedAt());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product.getImages() != null && !product.getImages().isEmpty()) {
            ProductImage mainImage = product.getImages().stream()
                    .filter(img -> Boolean.TRUE.equals(img.getIsMain()))
                    .findFirst()
                    .orElse(product.getImages().get(0));

            dto.setMainImageUrl("/api/products/images/display/" + mainImage.getFilePath());

            dto.setImageIds(
                    product.getImages()
                            .stream()
                            .map(ProductImage::getId)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private Products toEntity(ProductsDTO dto) {
        Products product = new Products();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCreatedAt(dto.getCreatedAt());
        return product;
    }

    private ProductImageDTO toImageDTO(ProductImage image) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(image.getId());
        dto.setImageUrl("/api/products/images/display/" + image.getFilePath());
        dto.setIsMain(image.getIsMain());
        dto.setProductId(image.getProduct().getId());
        return dto;
    }

    // ===================== PRODUCTS =====================

    @Transactional
    @Override
    public ProductsDTO createProduct(ProductsDTO dto) {
        Products product = toEntity(dto);
        product.setCreatedAt(LocalDateTime.now());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryId()));
            product.setCategory(category);
        }

        return toDTO(productsRepository.save(product));
    }

    @Transactional
    @Override
    public ProductsDTO updateProduct(Integer id, ProductsDTO dto) {
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + dto.getCategoryId()));
            product.setCategory(category);
        }

        return toDTO(productsRepository.save(product));
    }

    @Transactional
    @Override
    public void deleteProduct(Integer id) {
        productsRepository.deleteById(id);
    }

    @Override
    public ProductsDTO getProductById(Integer id) {
        return productsRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Override
    public List<ProductsDTO> getAllProducts() {
        return productsRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductsDTO> searchProducts(String query) {
        return productsRepository
                .findAllByNameContainingIgnoreCase(query.trim())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductsDTO getProduct(Integer id) {
        return getProductById(id);
    }

    @Override
    public ProductsDTO findByName(String name) {
        Products product = productsRepository.findByName(name);
        return product != null ? toDTO(product) : null;
    }

    // ===================== IMAGES =====================

    @Transactional
    @Override
    public ProductImageDTO uploadImage(Integer productId, MultipartFile file, boolean isMain) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            String ext = file.getOriginalFilename() != null && file.getOriginalFilename().contains(".")
                    ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
                    : "";

            String filename = UUID.randomUUID() + ext;
            Files.copy(file.getInputStream(), rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

            if (isMain) {
                product.getImages().forEach(img -> img.setIsMain(false));
            }

            ProductImage image = new ProductImage();
            image.setProduct(product);
            image.setIsMain(isMain);
            image.setFilePath(filename);

            return toImageDTO(productImageRepository.save(image));

        } catch (IOException e) {
            throw new RuntimeException("Image upload error", e);
        }
    }

    @Override
    public ProductImageDTO getImageById(Integer imageId) {
        return productImageRepository.findById(imageId)
                .map(this::toImageDTO)
                .orElseThrow(() -> new RuntimeException("Image not found: " + imageId));
    }

    @Override
    public List<ProductImageDTO> getImagesByProductId(Integer productId) {
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        return product.getImages()
                .stream()
                .map(this::toImageDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteImage(Integer imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found: " + imageId));

        try {
            Files.deleteIfExists(rootLocation.resolve(image.getFilePath()));
        } catch (IOException ignored) {}

        productImageRepository.delete(image);
    }

    // ===================== FILE LOAD =====================

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new RuntimeException("File not readable: " + filename);

        } catch (MalformedURLException e) {
            throw new RuntimeException("File error: " + filename, e);
        }
    }
}
