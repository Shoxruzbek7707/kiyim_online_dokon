package uz.pdp.kiyim_online_dokon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.CUSTOMER;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTRimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Addresses> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reviews> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Cart cart;
}