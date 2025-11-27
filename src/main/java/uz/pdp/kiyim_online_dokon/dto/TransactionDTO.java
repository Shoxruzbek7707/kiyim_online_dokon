package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentStatus;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class TransactionDTO {
    private Integer id;
    private Integer orderId;
    private Integer userId;
    private Double amount;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;
    private String providerTransactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String rawResponse;
}
