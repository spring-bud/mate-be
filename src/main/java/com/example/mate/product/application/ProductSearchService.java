package com.example.mate.product.application;


import com.example.mate.product.application.dto.ProductAllResponseDto;
import com.example.mate.product.application.dto.ProductDetailResponseDto;
import com.example.mate.product.application.dto.ProductLikeReviewDto;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.repository.ProductRepository;
import com.example.mate.review.application.ReviewService;
import com.example.mate.review.application.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ReviewService reviewService;
    private final LikeService likeService;
    private final ProductService productService;
    private final ProductRepository productRepository;


    @Transactional
    public ProductDetailResponseDto getProductById(
            Long productId,
            Long userId
    ) {

        Product findProduct = productService.findProductById(productId);
        ProductLikeReviewDto likeReviewInfo = likeReviewInfo(productId, userId);
        List<ReviewResponseDto> reviewList = reviewService.getReviewByProductId(productId);

        return ProductDetailResponseDto.of(
                findProduct,
                likeReviewInfo.likeCount(),
                likeReviewInfo.reviewCount(),
                likeReviewInfo.likeStatus(),
                reviewList
        );
    }

    @Transactional
    public List<ProductAllResponseDto> getProduct(
            Long userId
    ) {
        //TODO: 나중에 필요하면
        //Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt")));

        List<Product> findProducts = productRepository.findWithUserAndTags();

        List<ProductAllResponseDto> productAll = findProducts.stream()
                .map(product -> {
                    ProductLikeReviewDto likeReviewInfo = likeReviewInfo(product.getId(), userId);

                    return ProductAllResponseDto.of(
                            product,
                            likeReviewInfo.likeCount(),
                            likeReviewInfo.reviewCount(),
                            likeReviewInfo.likeStatus()
                    );
                })
                .collect(Collectors.toList());

        return productAll;
    }

    private ProductLikeReviewDto likeReviewInfo(Long productId, Long userId) {
        Long likeCount = likeService.countLike(productId);
        Long reviewCount = reviewService.countReview(productId);
        boolean likeStatus = likeService.getLikeStatus(productId, userId);

        return ProductLikeReviewDto.of(likeCount, reviewCount, likeStatus);
    }
}
