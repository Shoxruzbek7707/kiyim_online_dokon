package uz.pdp.kiyim_online_dokon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentMethod;
import uz.pdp.kiyim_online_dokon.entity.enums.PaymentStatus;

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
    private PaymentStatus status = PaymentStatus.PENDING;

    @OneToOne
    @JoinColumn(name = "transaction_id", unique = true)
    private Transactions transaction;


    @CreationTimestamp
    private LocalDateTime createdAt;
}
