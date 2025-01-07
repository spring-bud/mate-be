package com.example.mate.review.domain.repository;

import com.example.mate.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("""
            SELECT r FROM Review r
             LEFT JOIN FETCH r.product p
             WHERE p.user.id = :userId
             ORDER BY r.createdAt DESC
            """)
    List<Review> findByUserId(Long userId);

    @Query("""
            SELECT r FROM Review r
             LEFT JOIN FETCH r.product p
             WHERE p.id = :productId
             ORDER BY r.createdAt DESC
            """)
    List<Review> findByProductId(Long productId);

    Optional<Review> findByIdAndUserId(Long reviewId, Long userId);
}
