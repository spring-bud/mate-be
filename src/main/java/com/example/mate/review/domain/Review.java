package com.example.mate.review.domain;

import com.example.mate.common.domain.BaseTimeEntity;
import com.example.mate.product.domain.Product;
import com.example.mate.review.exception.ReviewException;
import com.example.mate.review.exception.ReviewExceptionType;
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

    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Builder
    public Review(User user, Product product, double star, String content, ReviewStatus status) {
        this.user = user;
        this.product = product;
        this.star = star;
        this.content = content;
        this.status = status;
    }

    public void updateReviewInfo(Double star, String content) {
        if (star != null) {
            this.star = star;
        }
        if (content != null) {
            this.content = content;
        }
    }

    public void isOwnerOrThrow(Long userId) {
        if (!user.getId().equals(userId)) {
            throw new ReviewException(ReviewExceptionType.NO_PERMISSIONS_ON_REVIEW);
        }
    }

    public void softDelete() {
        this.status = ReviewStatus.DELETED;
    }
}
