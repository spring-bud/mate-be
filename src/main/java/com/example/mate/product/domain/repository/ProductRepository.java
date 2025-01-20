package com.example.mate.product.domain.repository;

import com.example.mate.product.domain.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.user u " +
            "LEFT JOIN FETCH p.productTags pt " +
            "LEFT JOIN FETCH pt.tag " +
            "WHERE p.id = :productId " +
            "AND u.status != 'DELETED'" +
            "AND p.status != 'DELETED'")
    Optional<Product> findByIdWithUserAndTags(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.user u " +
            "LEFT JOIN FETCH p.productTags pt " +
            "LEFT JOIN FETCH pt.tag " +
            "WHERE u.status != 'DELETED'" +
            "AND p.status != 'DELETED'")
    List<Product> findWithUserAndTags();

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.user u " +
            "LEFT JOIN FETCH p.productTags pt " +
            "LEFT JOIN FETCH pt.tag " +
            "WHERE p.user.id = :userId " +
            "AND u.status != 'DELETED'" +
            "AND p.status != 'DELETED'")
    List<Product> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("""
            UPDATE Product p
             SET p.status = 'DELETED'
             WHERE p.user.id = :userId
            """)
    void updateStatusdByUsertId(Long userId);
}
