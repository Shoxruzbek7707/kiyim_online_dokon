package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.orderdto.CheckoutRequest;
import uz.pdp.kiyim_online_dokon.dto.orderdto.OrderDetailDto;
import uz.pdp.kiyim_online_dokon.dto.orderdto.OrderDto;

import java.security.Principal;
import java.util.List;

@Tag(name = "8. Orders", description = "Buyurtma berish va tarix")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Buyurtma berish (Checkout)")
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderDto>> checkout(@Valid @RequestBody CheckoutRequest dto, Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(orderService.checkout(dto, principal)));
    }

    @Operation(summary = "Mening buyurtmalarim")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getMyOrders(Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getMyOrders(principal)));
    }

    @Operation(summary = "Buyurtma detallari")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailDto>> getOrder(@PathVariable String orderId, Principal principal) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderDetail(orderId, principal)));
    }
}