package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.productdto.ProductCardDto;

import java.util.List;

@Tag(name = "4. Categories", description = "Kategoriyalar va filtrlar")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Barcha kategoriyalar (tree ko'rinishida)")
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<CategoryTreeDto>>> getTree() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getTree()));
    }

    @Operation(summary = "Kategoriyadagi mahsulotlar + filter")
    @GetMapping("/{id}/products")
    public ResponseEntity<ApiResponse<Page<ProductCardDto>>> getProducts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<String> colors) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getProducts(id, page, size, sort, minPrice, maxPrice, colors)));
    }
}