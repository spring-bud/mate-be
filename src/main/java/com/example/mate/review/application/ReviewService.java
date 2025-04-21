package com.example.mate.review.application;

import com.example.mate.product.application.ProductService;
import com.example.mate.product.domain.Product;
import com.example.mate.review.application.dto.ReviewDeleteResponseDto;
import com.example.mate.review.application.dto.ReviewResponseDto;
import com.example.mate.review.application.dto.ReviewUserIdResponseDto;
import com.example.mate.review.application.dto.ReviewUserResponseDto;
import com.example.mate.review.domain.Review;
import com.example.mate.review.domain.repository.ReviewRepository;
import com.example.mate.review.exception.ReviewException;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.mate.review.domain.ReviewStatus.ACTIVE;
import static com.example.mate.review.exception.ReviewExceptionType.NOT_EXIST_REVIEW;

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
                .status(ACTIVE)
                .build();

        Review saveReview = reviewRepository.save(newReview);

        return ReviewResponseDto.of(saveReview);
    }

    public List<ReviewUserResponseDto> getReviewByProductId(Long ProductId) {
        //TODO: 나중에 필요하면
        //Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt")));
        List<Review> findProductReviews = reviewRepository.findByProductId(ProductId);

        List<ReviewUserResponseDto> reviewAll = findProductReviews.stream()
                .map(review -> {
                    User findUser = userService.getUserById(review.getUser().getId());

                    return ReviewUserResponseDto.of(
                            review,
                            findUser
                    );
                })
                .collect(Collectors.toList());

        return reviewAll;
    }

    public List<ReviewUserResponseDto> getReviewByUserId(ReviewUserIdResponseDto request) {
        //TODO: 나중에 필요하면
        //Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt"))) ;

        List<Review> findUserReviews = reviewRepository.findByUserId(request.userid());

        List<ReviewUserResponseDto> reviewAll = findUserReviews.stream()
                .map(review -> {
                    User findUser = userService.getUserById(review.getUser().getId());

                    return ReviewUserResponseDto.of(
                            review,
                            findUser
                    );
                })
                .collect(Collectors.toList());

        return reviewAll;
    }

    @Transactional
    public ReviewResponseDto updateReviewByIdAndUserId(Long userId, ReviewResponseDto request) {
        Review findReview = findById(request.id());

        findReview.isOwnerOrThrow(userId);

        findReview.updateReviewInfo(
                request.star(),
                request.content()
        );

        Review updateReview = reviewRepository.save(findReview);

        return ReviewResponseDto.of(updateReview);
    }

    @Transactional
    public void deleteReviewByIdAndUserId(ReviewDeleteResponseDto request, Long userId) {
        Review findReview = findById(request.reviewid());


        findReview.isDeleteOwnerOrThrow(userId, request.productownerid());

        findReview.softDelete();

        reviewRepository.save(findReview);
    }

    @Transactional
    public void deleteReviewByProductId(Long productId) {
        reviewRepository.updateStatusdByProductId(productId);
    }

    public Long countReview(Long productId) {
        return getCountReview(productId);
    }

    private Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(NOT_EXIST_REVIEW));
    }

    private Long getCountReview(Long productId) {
        return reviewRepository.countReviewByProductId(productId);
    }
}
