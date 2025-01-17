package com.example.mate.review.application;

import com.example.mate.product.application.ProductService;
import com.example.mate.product.domain.Product;
import com.example.mate.review.application.dto.ReviewResponseDto;
import com.example.mate.review.domain.Review;
import com.example.mate.review.domain.repository.ReviewRepository;
import com.example.mate.review.exception.ReviewException;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.mate.review.exception.ReviewExceptionType.NOT_EXIST_Review;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProductService productService;

    @Transactional
    public ReviewResponseDto createReview(Long userId, Long productId, ReviewResponseDto request) {
        User findUser = userService.getUserById(userId);

        Product findProduct = productService.findProductById(productId);

        Review newReview = Review.builder()
                .user(findUser)
                .product(findProduct)
                .star(request.star())
                .content(request.content())
                .build();

        Review saveReview = reviewRepository.save(newReview);

        return ReviewResponseDto.of(saveReview);
    }

    public List<ReviewResponseDto> getReviewByProductId(Long ProductId) {
        //TODO: 나중에 필요하면
        //Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt")));
        List<Review> findProductReviews = reviewRepository.findByProductId(ProductId);

        return ReviewResponseDto.of(findProductReviews);
    }

    public List<ReviewResponseDto> getReviewByUserId(Long userId) {
        //TODO: 나중에 필요하면
        //Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt"))) ;
        List<Review> findUserReviews = reviewRepository.findByUserId(userId);

        return ReviewResponseDto.of(findUserReviews);
    }

    @Transactional
    public ReviewResponseDto updateReviewByIdAndUserId(Long reviewId, Long userId, ReviewResponseDto request) {
        Review findReview = findByIdAndUserId(reviewId, userId);

        findReview.updateReviewInfo(
                request.star(),
                request.content()
        );

        Review updateReview = reviewRepository.save(findReview);

        return ReviewResponseDto.of(updateReview);
    }

    @Transactional
    public void deleteReviewByIdAndUserId(Long reviewId, Long userId) {
        Review findReview = findByIdAndUserId(reviewId, userId);

        reviewRepository.delete(findReview);
    }

    public Long countReview(Long productId) {
        return getCountReview(productId);
    }

    private Review findByIdAndUserId(Long reviewId, Long userId) {
        return reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ReviewException(NOT_EXIST_Review));
    }

    private Long getCountReview(Long productId) {
        return reviewRepository.countReviewByProductId(productId);
    }
}
