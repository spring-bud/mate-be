package com.example.mate.product.domain.repository;

import com.example.mate.product.domain.Product;

import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.productTags pt " +
            "LEFT JOIN FETCH pt.tag " +
            "WHERE p.id = :productId")
    Optional<Product> findByIdWithUserAndTags(@Param("productId") Long productId);
}
