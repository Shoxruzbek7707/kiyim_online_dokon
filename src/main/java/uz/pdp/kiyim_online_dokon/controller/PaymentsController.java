package uz.pdp.kiyim_online_dokon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.PaymentsDTO;
import uz.pdp.kiyim_online_dokon.service.interfaces.PaymentsService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentsService paymentsService;

    @PostMapping
    public ResponseEntity<PaymentsDTO> createPayment(@RequestBody PaymentsDTO dto) {
        return ResponseEntity.ok(paymentsService.createPayment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentsDTO> getPayment(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentsService.getPayment(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentsDTO> updatePayment(@PathVariable Integer id,
                                                     @RequestBody PaymentsDTO dto) {
        return ResponseEntity.ok(paymentsService.updatePayment(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        paymentsService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}
