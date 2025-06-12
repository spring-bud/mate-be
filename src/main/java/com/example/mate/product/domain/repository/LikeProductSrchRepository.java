package com.example.mate.product.domain.repository;

import com.example.mate.product.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeProductSrchRepository {

    List<Product> findLikeProducts(Long userId);
}
