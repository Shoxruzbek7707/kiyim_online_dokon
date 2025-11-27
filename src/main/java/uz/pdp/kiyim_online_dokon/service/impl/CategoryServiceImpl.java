package uz.pdp.kiyim_online_dokon.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.kiyim_online_dokon.dto.CategoryDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;
import uz.pdp.kiyim_online_dokon.entity.Category;
import uz.pdp.kiyim_online_dokon.entity.Products;
import uz.pdp.kiyim_online_dokon.repository.CategoryRepository;
import uz.pdp.kiyim_online_dokon.service.interfaces.CategoryService;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }



    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {
       return toDTO(categoryRepository.save(toEntity(dto)));
    }

    @Override
    public CategoryDTO updateCategory(Integer id, CategoryDTO dto) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow();

        category.setName(dto.getName());
        Category updatedCategory = categoryRepository.save(category);
        return toDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO getCategory(Integer id) {
       return  toDTO(categoryRepository
               .findById(id)
               .orElseThrow());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.
                findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductsDTO> getProductsByCategoryId(Integer categoryId) {
        Category  category = categoryRepository
                .findById(categoryId)
                .orElseThrow();

        List<Products> products = category.getProducts();

        List<ProductsDTO> productsDTOS = new ArrayList<>();

        for (Products p : products) {
            ProductsDTO dto = new ProductsDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setDescription(p.getDescription());
            dto.setPrice(p.getPrice());
            dto.setStock(p.getStock());
            dto.setBrand(p.getBrand());
            dto.setCategoryId(categoryId);
            dto.setCreatedAt(p.getCreatedAt());
            productsDTOS.add(dto);
        }
        return productsDTOS;
    }

    @Override
    public CategoryDTO getCategoryById(Integer id) {
        return toDTO(categoryRepository.findById(id).orElseThrow());
    }
}
