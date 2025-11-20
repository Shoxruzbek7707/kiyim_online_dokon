package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "10. Admin - Products", description = "Admin: Mahsulot boshqaruvi")
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final AdminProductService productService;

    @Operation(summary = "Yangi mahsulot qo'shish")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductAdminDto>> create(
            @Valid @RequestPart("data") ProductCreateRequest dto,
            @RequestPart("images") List<MultipartFile> images) {
        return ResponseEntity.ok(ApiResponse.success(productService.create(dto, images)));
    }

    @Operation(summary = "Mahsulotni tahrirlash")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductAdminDto>> update(
            @PathVariable Long id,
            @Valid @RequestPart("data") ProductUpdateRequest dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return ResponseEntity.ok(ApiResponse.success(productService.update(id, dto, images)));
    }

    @Operation(summary = "Mahsulotni o'chirish")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted"));
    }

    @Operation(summary = "Barcha mahsulotlar (admin uchun)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductAdminDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(productService.getAll(page, size)));
    }

    @Operation(summary = "Stock yangilash")
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<String>> updateStock(@PathVariable Long id, @RequestBody UpdateStockRequest dto) {
        productService.updateStock(id, dto.getStock());
        return ResponseEntity.ok(ApiResponse.success("Stock updated"));
    }
}