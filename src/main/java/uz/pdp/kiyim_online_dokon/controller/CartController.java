package uz.pdp.kiyim_online_dokon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.CartItemDTO;
import uz.pdp.kiyim_online_dokon.dto.CartsDTO;
import uz.pdp.kiyim_online_dokon.service.interfaces.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartsDTO> getCartByUser(@PathVariable Integer userId) {
        CartsDTO cartsDTO = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartsDTO);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Integer cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CartsDTO> createCart(@RequestBody CartsDTO dto) {
        CartsDTO createdCart = cartService.createCart(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCart);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartsDTO> updateCart(@PathVariable Integer cartId,
                                               @RequestBody CartsDTO dto) {
        CartsDTO updatedCart = cartService.updateCart(cartId, dto);
        return ResponseEntity.ok(updatedCart);
    }


    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDTO> addItemToCart(@PathVariable Integer cartId, @RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO added = cartService.addItemToCart(cartId,cartItemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(added);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable Integer itemId, @RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO updated = cartService.updateCartItem(itemId,cartItemDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Integer itemId) {
        cartService.deleteCartItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItemDTO>> getCartItems(@PathVariable Integer cartId) {
        List<CartItemDTO> items = cartService.getCartItems(cartId);
        return ResponseEntity.ok(items);
    }
}
