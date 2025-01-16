package com.example.mate.review.domain.repository;

import com.example.mate.review.domain.Review;
import com.example.mate.user.application.dto.PopularityUserReviewResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("""
            SELECT r FROM Review r
             LEFT JOIN FETCH r.product p
             LEFT JOIN FETCH r.user u
             WHERE p.user.id = :userId
             AND r.status != 'DELETED'
             ORDER BY r.createdAt DESC  
            """)
    List<Review> findByUserId(Long userId);

    @Query("""
            SELECT r FROM Review r
             LEFT JOIN FETCH r.product p
             LEFT JOIN FETCH r.user u
             WHERE p.id = :productId
             AND r.status != 'DELETED'
             ORDER BY r.createdAt DESC
            """)
    List<Review> findByProductId(Long productId);

    Optional<Review> findById(Long reviewId);

    @Query("""
            SELECT COUNT(r) FROM Review r
             WHERE r.product.id = :productId
               AND r.status != 'DELETED'
            """)
    Long countReviewByProductId(Long productId);

    @Query("""
            SELECT new com.example.mate.user.application.dto.PopularityUserReviewResponseDto(COUNT(r), AVG(r.star)) FROM Review r
             LEFT JOIN r.product p
             WHERE p.user.id = :userId
             AND r.status != 'DELETED'
            """)
    PopularityUserReviewResponseDto findByUserIdReviewStats(Long userId);

    @Query("""
            UPDATE Review r
             SET r.status = 'DELETED'
             WHERE r.product.id = :productId
            """)
    void updateStatusdByProductId(Long productId);
}
