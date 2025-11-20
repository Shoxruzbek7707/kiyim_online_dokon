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
@Table(name = "transactions") // Jadval nomi 'transactions' deb belgilandi
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Integer o'rniga JPA standarti bo'yicha Long

    @OneToOne // Order bilan One-to-One bog'liqlik
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Orders order;

    // UserID o'rniga Users obyekti
    // Tranzaksiya Orders obyekti orqali foydalanuvchiga bog'langan bo'ladi, lekin uni to'g'ridan-to'g'ri bog'lash ham mumkin:
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
    private String providerTransactionId; // payment_providerdan kelgan tranzaksiya IDsi

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Yangilanish vaqti uchun:
    // @UpdateTimestamp kabi anotatsiya Hibernate dan foydalanishda qo'shilishi mumkin.
    private LocalDateTime updatedAt;

    // Eslatma: `row_response` (xom javob) kabi texnik maydonlar ba'zan oddiy String sifatida saqlanadi.
    @Column(columnDefinition = "TEXT")
    private String rawResponse;
}