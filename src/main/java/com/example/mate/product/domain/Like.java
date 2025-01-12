package com.example.mate.product.domain;

import com.example.mate.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "products_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Like(User user, Product product) {
        this.user = user;
        this.product = product;
        this.createdAt = LocalDateTime.now();
    }
}
