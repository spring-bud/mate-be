package com.example.mate.product.domain.repository;

import com.example.mate.product.application.dto.ProductSrchRequestDto;
import com.example.mate.product.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSrchRepository {

    Page<Product> findWithUserAndTags(ProductSrchRequestDto request, Pageable pageable);
}
