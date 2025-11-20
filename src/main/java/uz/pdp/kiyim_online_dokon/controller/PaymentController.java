package uz.pdp.kiyim_online_dokon.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "9. Payments", description = "To'lov tizimlari")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "To'lov yaratish (Payme, Click, Card)")
    @PostMapping("/create/{orderId}")
    public ResponseEntity<ApiResponse<PaymentLinkDto>> create(@PathVariable String orderId, @RequestBody PaymentCreateRequest dto) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.createPayment(orderId, dto)));
    }

    @Operation(summary = "Payme webhook", hidden = true)
    @PostMapping("/webhook/payme")
    public ResponseEntity<String> paymeWebhook(@RequestBody Map<String, Object> payload) {
        paymentService.handlePayme(payload);
        return ResponseEntity.ok("OK");
    }

    @Operation(summary = "Click webhook", hidden = true)
    @PostMapping("/webhook/click")
    public ResponseEntity<String> clickWebhook(@RequestBody Map<String, Object> payload) {
        paymentService.handleClick(payload);
        return ResponseEntity.ok("OK");
    }
}