package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.cartdto.AddToCartRequest;
import uz.pdp.kiyim_online_dokon.dto.cartdto.CartDto;
import uz.pdp.kiyim_online_dokon.dto.cartdto.UpdateCartItemRequest;

import java.security.Principal;

@Tag(name = "7. Cart", description = "Savat operatsiyalari")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Savatni ko'rish")
    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(cartService.getCart(principal)));
    }

    @Operation(summary = "Savatga qo'shish")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartDto>> add(@Valid @RequestBody AddToCartRequest dto, Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(cartService.addToCart(dto, principal)));
    }

    @Operation(summary = "Miqdor o'zgartirish")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartDto>> update(@Valid @RequestBody UpdateCartItemRequest dto, Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(cartService.updateQuantity(dto, principal)));
    }

    @Operation(summary = "Savatdan o'chirish")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> remove(@PathVariable Long productId, Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(cartService.removeItem(productId, principal)));
    }
}