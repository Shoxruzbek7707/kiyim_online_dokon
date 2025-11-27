package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentsDTO {
    private Integer id;
    private Integer orderId;
    private Double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private Integer transactionId;
    private LocalDateTime createdAt;
}
