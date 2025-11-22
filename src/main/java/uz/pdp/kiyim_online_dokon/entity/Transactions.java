package uz.pdp.kiyim_online_dokon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;
import uz.pdp.kiyim_online_dokon.entity.enums.OrderStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Orders order;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus.PaymentStatus status = OrderStatus.PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(unique = true)
    private String providerTransactionId;

    @CreationTimestamp
    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    @Column(columnDefinition = "TEXT")
    private String rawResponse;
}