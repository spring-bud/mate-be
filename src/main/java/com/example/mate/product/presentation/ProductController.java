package com.example.mate.product.presentation;

import com.example.mate.common.response.ApiResponse;
import com.example.mate.product.application.LikeService;
import com.example.mate.product.application.ProductSearchService;
import com.example.mate.product.application.ProductService;
import com.example.mate.product.application.dto.ProductAllResponseDto;
import com.example.mate.product.application.dto.ProductCreateRequestDto;
import com.example.mate.product.application.dto.ProductDetailResponseDto;
import com.example.mate.product.application.dto.ProductIdResponseDto;
import com.example.mate.proposal.application.dto.ProposalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final LikeService likeService;
    private final ProductSearchService productSearchService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductIdResponseDto>> createProduct(
            @AuthenticationPrincipal Long userId,
            @RequestBody ProductCreateRequestDto request
    ) {
        ProductIdResponseDto productIdResponse = productService.createProduct(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(productIdResponse));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<ProductAllResponseDto>>> getProducts(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(productSearchService.getProduct(userId)));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<ProductAllResponseDto>>> getProductsByUserID(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(productSearchService.getProductByUserId(userId)));
    }

    @GetMapping("/{productId}/{userId}")
    public ResponseEntity<ApiResponse<ProductDetailResponseDto>> getProductById(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(new ApiResponse<>(productSearchService.getProductById(productId, userId)));
    }

    @PostMapping("/like/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> createOrDeleteLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                likeService.createOrDeleteLike(userId, productId))
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProposalResponseDto>> deleteProduct(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId
    ) {
        productService.deleteProduct(userId, productId);
        return ResponseEntity.ok(null);
    }
}
