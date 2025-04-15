package com.example.mate.product.presentation;

import com.example.mate.common.response.ApiResponse;
import com.example.mate.product.application.LikeService;
import com.example.mate.product.application.ProductSearchService;
import com.example.mate.product.application.ProductService;
import com.example.mate.product.application.dto.*;
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

    private final String COOKIE_ACCESS_TOKEN = "access_token";

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

    @PostMapping("/srch")
    public ResponseEntity<ApiResponse<List<ProductAllResponseDto>>> getProducts(
            @CookieValue(value = COOKIE_ACCESS_TOKEN, required = false) String accessToken,
            @RequestBody ProductSrchRequestDto srchRequest
    ) {
        return ResponseEntity.ok(new ApiResponse<>(productSearchService.getProducts(accessToken, srchRequest)));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<ProductAllResponseDto>>> getProductsByUserID(
            @CookieValue(value = COOKIE_ACCESS_TOKEN, required = false) String accessToken,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(productSearchService.getProductByUserId(userId, accessToken)));
    }

    @GetMapping("/tagName")
    public ResponseEntity<ApiResponse<List<String>>> getProductsByTagName(
            @RequestBody ProductTagRequestDto tagName
    ) {
        return ResponseEntity.ok(new ApiResponse<>(productSearchService.getProductByTagName(tagName)));
    }

    @GetMapping("/mostTag")
    public ResponseEntity<ApiResponse<List<ProductMostTagRequestDto>>> getMostTag(
    ) {
        return ResponseEntity.ok(new ApiResponse<>(productSearchService.getMostTag()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDetailResponseDto>> getProductById(
            @CookieValue(value = COOKIE_ACCESS_TOKEN, required = false) String accessToken,
            @PathVariable Long productId
    ) {

        return ResponseEntity.ok(new ApiResponse<>(productSearchService.getProductById(productId, accessToken)));
    }

    @PostMapping("/like/{productId}")
    public ResponseEntity<ApiResponse<Boolean>> toggleLike(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                likeService.toggleLike(userId, productId))
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId
    ) {
        productService.deleteProduct(userId, productId);
        return ResponseEntity.ok(null);
    }
}
