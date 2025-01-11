package com.example.mate.product.domain.repository;

import com.example.mate.product.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndProductId(Long userId, Long productId);

    @Query("""
            SELECT COUNT(l) FROM Like l
             WHERE l.product.id = :productId
            """)
    Long countLikeByProductId(Long productId);
}
