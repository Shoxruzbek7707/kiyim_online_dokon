package uz.pdp.kiyim_online_dokon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "11. Admin - Orders", description = "Admin: Buyurtmalarni boshqarish")
@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final AdminOrderService orderService;

    @Operation(summary = "Barcha buyurtmalar")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderAdminDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) OrderStatus status) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getAll(page, status)));
    }

    @Operation(summary = "Buyurtma statusini o'zgartirish")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderAdminDto>> updateStatus(
            @PathVariable String orderId,
            @RequestBody UpdateOrderStatusRequest dto) {
        return ResponseEntity.ok(ApiResponse.success(orderService.updateStatus(orderId, dto.getStatus())));
    }

    @Operation(summary = "Bitta buyurtma (admin uchun)")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderAdminDetailDto>> getById(@PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getDetail(orderId)));
    }
}