package com.example.mate.product.presentation;

import com.example.mate.common.response.ApiResponse;
import com.example.mate.product.application.ProductService;
import com.example.mate.product.application.dto.ProductCreateRequestDto;
import com.example.mate.product.application.dto.ProductDetailResponseDto;
import com.example.mate.product.application.dto.ProductIdResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mate.auth.presentation.support.OptionalUser;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductIdResponseDto>> createProduct(
            @AuthenticationPrincipal Long userId,
            @RequestBody ProductCreateRequestDto request
    ) {
        ProductIdResponseDto productIdResponse = productService.createProduct(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(productIdResponse));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailResponseDto>> getProductById(
            @OptionalUser Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                productService.getProductById(productId))
        );
    }
}
