package com.example.mate.product.application;


import com.example.mate.auth.infrastructure.jwt.JwtProvider;
import com.example.mate.product.application.dto.*;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.repository.ProductRepository;
import com.example.mate.product.domain.repository.ProductSrchRepository;
import com.example.mate.review.application.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ReviewService reviewService;
    private final LikeService likeService;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final JwtProvider jwtProvider;
    private final ProductSrchRepository productSrchRepository;


    @Transactional
    public ProductDetailResponseDto getProductById(
            Long productId,
            String accessToken
    ) {

        Product findProduct = productService.findProductById(productId);
        ProductLikeReviewDto likeReviewInfo = likeReviewInfo(productId);
        Long userId = -1L;
        if (accessToken == null || accessToken == "") {
            userId = -1L;
        } else {
            userId = jwtProvider.getUserIdFromAccessToken(accessToken);
        }

        boolean likeStatus = false;

        if (userId != -1) {
            likeStatus = likeService.getLikeStatus(productId, userId);
        }

        return ProductDetailResponseDto.of(
                findProduct,
                likeReviewInfo.likeCount(),
                likeReviewInfo.reviewCount(),
                likeStatus
        );
    }

    @Transactional
    public List<ProductAllResponseDto> getProductByUserId(
            Long userId,
            String accessToken
    ) {
        //TODO: 나중에 필요하면
        //Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt")));

        Long loginId = -1L;
        if (accessToken == null || accessToken == "") {
            loginId = -1L;
        } else {
            loginId = jwtProvider.getUserIdFromAccessToken(accessToken);
        }

        AtomicLong myVar = new AtomicLong(loginId);

        List<Product> findProducts = productRepository.findByUserId(userId);

        List<ProductAllResponseDto> productAll = findProducts.stream()
                .map(product -> {
                    ProductLikeReviewDto likeReviewInfo = likeReviewInfo(product.getId());
                    boolean likeStatus = likeService.getLikeStatus(product.getId(), myVar.get());
                    return ProductAllResponseDto.of(
                            product,
                            likeReviewInfo.likeCount(),
                            likeReviewInfo.reviewCount(),
                            likeStatus
                    );
                })
                .collect(Collectors.toList());

        return productAll;
    }

    @Transactional
    public List<ProductAllResponseDto> getProducts(
            String accessToken,
            ProductSrchRequestDto request
    ) {
        //TODO: 나중에 필요하면
        //Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt")));
        Long userId = -1L;
        if (accessToken == null || accessToken == "") {
            userId = -1L;
        } else {
            userId = jwtProvider.getUserIdFromAccessToken(accessToken);
        }

        AtomicLong myVar = new AtomicLong(userId);

        List<Product> findProducts = productSrchRepository.findWithUserAndTags(request);

        List<ProductAllResponseDto> productAll = findProducts.stream()
                .map(product -> {
                    ProductLikeReviewDto likeReviewInfo = likeReviewInfo(product.getId());
                    boolean likeStatus = false;

                    if (myVar.get() != -1) {
                        likeStatus = likeService.getLikeStatus(product.getId(), myVar.get());
                    }

                    return ProductAllResponseDto.of(
                            product,
                            likeReviewInfo.likeCount(),
                            likeReviewInfo.reviewCount(),
                            likeStatus
                    );
                })
                .sorted((dto1, dto2) -> {
                    if ("LIKE".equals(request.sort())) {
                        // likeCount 기준으로 정렬
                        return Long.compare(dto2.count().likeCount(), dto1.count().likeCount()); // 내림차순
                    } else if ("CREATE".equals(request.sort())) {
                        // reviewCount 기준으로 정렬
                        return dto2.createdAt().compareTo(dto1.createdAt()); // 내림차순
                    }
                    return 0; // 기본값 (정렬 기준이 없을 경우)
                })
                .collect(Collectors.toList());

        return productAll;
    }

    @Transactional
    public List<String> getProductByTagName(
            ProductTagRequestDto tagName
    ) {

        List<String> findTags = productRepository.findByTag(tagName.tag());

        return findTags;
    }

    @Transactional
    public List<ProductMostTagRequestDto> getMostTag(
    ) {

        List<Object[]> findMostTag = productRepository.mostten();

        return findMostTag.stream()
                .limit(10)
                .map(obj -> new ProductMostTagRequestDto(
                        (String) obj[0],  // 첫 번째 값은 tag 이름 (String)
                        ((Number) obj[1]).intValue()  // 두 번째 값은 tagCount (int)
                ))
                .collect(Collectors.toList());
    }

    private ProductLikeReviewDto likeReviewInfo(Long productId) {
        Long likeCount = likeService.countLike(productId);
        Long reviewCount = reviewService.countReview(productId);

        return ProductLikeReviewDto.of(likeCount, reviewCount);
    }
}
