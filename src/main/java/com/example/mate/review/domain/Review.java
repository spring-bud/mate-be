package com.example.mate.review.domain;

import com.example.mate.common.domain.BaseTimeEntity;
import com.example.mate.product.domain.Product;
import com.example.mate.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(name = "star")
    private Double star;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Builder
    public Review(User user, Product product, double star, String content) {
        this.user = user;
        this.product = product;
        this.star = star;
        this.content = content;
    }

    public void updateReviewInfo(Double star, String content) {
        if (star != null) {
            this.star = star;
        }
        if (content != null) {
            this.content = content;
        }
    }
}
