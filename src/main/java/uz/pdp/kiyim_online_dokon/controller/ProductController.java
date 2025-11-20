package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "5. Products", description = "Mahsulotlar: homepage, qidiruv, detail")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Yangiliklar (New Arrivals)")
    @GetMapping("/new-arrivals")
    public ResponseEntity<ApiResponse<List<ProductCardDto>>> getNewArrivals() {
        return ResponseEntity.ok(ApiResponse.success(productService.getNewArrivals()));
    }

    @Operation(summary = "Eng ko'p sotilganlar")
    @GetMapping("/top-selling")
    public ResponseEntity<ApiResponse<List<ProductCardDto>>> getTopSelling() {
        return ResponseEntity.ok(ApiResponse.success(productService.getTopSelling()));
    }

    @Operation(summary = "Mahsulot qidiruv")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductCardDto>>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(productService.search(q, page, size)));
    }

    @Operation(summary = "Bitta mahsulot (detail)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailDetailDto>> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getDetail(id)));
    }

    @Operation(summary = "O'xshash mahsulotlar")
    @GetMapping("/{id}/related")
    public ResponseEntity<ApiResponse<List<ProductCardDto>>> getRelated(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getRelated(id)));
    }
}