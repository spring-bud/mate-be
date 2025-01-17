package com.example.mate.product.domain.repository;

import com.example.mate.product.domain.Like;
import com.example.mate.user.application.dto.PopularityUserResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndProductId(Long userId, Long productId);

    @Query("""
            SELECT COUNT(l) FROM Like l
             WHERE l.product.id = :productId
            """)
    Long countLikeByProductId(Long productId);

    @Query("""
            SELECT p.user.id as userId, COUNT(*) as count FROM Like l
            LEFT JOIN l.product p
            LEFT JOIN l.user u
            WHERE u.status != 'DELETED'
            GROUP BY p.user.id
            ORDER BY COUNT(*) DESC
            """)
    List<PopularityUserResponseDto> countPopularityUser(Pageable pageRequest);
}
