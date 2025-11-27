package uz.pdp.kiyim_online_dokon.service.interfaces;

import uz.pdp.kiyim_online_dokon.dto.CategoryDTO;
import uz.pdp.kiyim_online_dokon.dto.ProductsDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO dto);
    CategoryDTO updateCategory(Integer id, CategoryDTO dto);
    void deleteCategory(Integer id);
    CategoryDTO getCategory(Integer id);
    List<CategoryDTO> getAllCategories();

    List<ProductsDTO> getProductsByCategoryId(Integer categoryId);

    CategoryDTO getCategoryById(Integer id);
}
