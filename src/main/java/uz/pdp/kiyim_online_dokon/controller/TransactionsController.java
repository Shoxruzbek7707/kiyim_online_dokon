package uz.pdp.kiyim_online_dokon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.kiyim_online_dokon.dto.TransactionDTO;
import uz.pdp.kiyim_online_dokon.service.interfaces.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionsController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(transactionService.createTransaction(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getTransaction(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Integer id,
                                                            @RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(transactionService.getTransactionsByOrder(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId));
    }
}
