package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Tag(name = "6. Reviews", description = "Mahsulotga sharh qoldirish")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Sharh qo'shish")
    @PostMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<ReviewDto>> add(
            @PathVariable Long productId,
            @Valid @RequestBody ReviewCreateRequest dto,
            Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.add(productId, dto, principal)));
    }

    @Operation(summary = "Mahsulot sharhlari")
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(reviewService.getByProduct(productId)));
    }
}