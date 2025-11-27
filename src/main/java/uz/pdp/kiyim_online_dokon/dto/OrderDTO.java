package uz.pdp.kiyim_online_dokon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Integer id;
    private Integer userId;

    private Double totalPrice;

    private String status;         // OrderStatus
    private String paymentMethod;  // PaymentMethod

    private List<OrderItemDTO> items;

    private LocalDateTime createdAt;

    private Integer paymentId;
}
