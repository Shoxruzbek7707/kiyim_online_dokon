package uz.pdp.kiyim_online_dokon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Reviews {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Products product;

    @Column(nullable = false)
    private Integer rating; // 1-5

    private String comment;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
