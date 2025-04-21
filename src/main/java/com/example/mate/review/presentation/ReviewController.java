package com.example.mate.review.presentation;

import com.example.mate.common.response.ApiResponse;
import com.example.mate.review.application.ReviewService;
import com.example.mate.review.application.dto.ReviewDeleteResponseDto;
import com.example.mate.review.application.dto.ReviewResponseDto;
import com.example.mate.review.application.dto.ReviewUserIdResponseDto;
import com.example.mate.review.application.dto.ReviewUserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createProposal(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId,
            @RequestBody ReviewResponseDto request
    ) {
        ReviewResponseDto reviewResponseDto = reviewService.createReview(userId, productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(reviewResponseDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<ReviewUserResponseDto>>> getUserReview(
            @RequestBody ReviewUserIdResponseDto request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                reviewService.getReviewByUserId(request))
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewUserResponseDto>>> getProductReview(
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                reviewService.getReviewByProductId(productId))
        );
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReviewById(
            @AuthenticationPrincipal Long userId,
            @RequestBody ReviewResponseDto request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                reviewService.updateReviewByIdAndUserId(userId, request))
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReviewById(
            @AuthenticationPrincipal Long userId,
            @RequestBody ReviewDeleteResponseDto request
    ) {
        reviewService.deleteReviewByIdAndUserId(request, userId);
        return ResponseEntity.ok(null);
    }
}
