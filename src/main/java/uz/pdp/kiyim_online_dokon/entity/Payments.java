package uz.pdp.kiyim_online_dokon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payments {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne @JoinColumn(name = "order_id", unique = true)
    private Orders order;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private OrderStatus.PaymentStatus status = OrderStatus.PaymentStatus.PENDING;

    private String transactionId; // providerdan kelgan id

    @CreationTimestamp
    private LocalDateTime createdAt;
}
