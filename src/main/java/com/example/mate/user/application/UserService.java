package com.example.mate.user.application;

import com.example.mate.product.domain.repository.LikeRepository;
import com.example.mate.product.domain.repository.ProductRepository;
import com.example.mate.review.domain.repository.ReviewRepository;
import com.example.mate.user.application.dto.*;
import com.example.mate.user.domain.ReasonType;
import com.example.mate.user.domain.Stack;
import com.example.mate.user.domain.User;
import com.example.mate.user.domain.event.UserCreateEvent;
import com.example.mate.user.domain.event.UserUpdateEvent;
import com.example.mate.user.domain.repository.UserRepository;
import com.example.mate.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.mate.user.exception.UserExceptionType.NOT_EXIST_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final StackService stackService;
    private final ApplicationEventPublisher eventPublisher;

    public UserInfoResponseDto getUserInfoWithId(Long userId) {
        User findUser = userRepository.findByIdAndStatusIsNotDeleted(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        return UserInfoResponseDto.of(findUser);
    }

    @Transactional
    public User getOrRegisterByOAuthId(String oAuthId, String kakaoImageUrl) {
        return userRepository.findByKakaoId(oAuthId)
                .map(this::activateIfDeleted)
                .orElseGet(() -> registerNewUserAndPublish(oAuthId, kakaoImageUrl));
    }

    private User activateIfDeleted(User user) {
        if (user.isDeletedUser()) {
            user.updateActiveStatus();
        }
        return user;
    }

    private User registerNewUserAndPublish(String oAuthId, String profileUrl) {
        User newUser = User.builder().kakaoId(oAuthId).profileUrl(profileUrl).build();
        User saveUser = userRepository.save(newUser);
        eventPublisher.publishEvent(UserCreateEvent.of(saveUser));
        return saveUser;
    }

    // External Service
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
    }

    @Transactional
    public UserInfoResponseDto updateUser(UserInfoRequestDto request, Long userId) {
        User findUser = userRepository.findByIdAndStatusIsNotDeleted(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));

        findUser.updateUserInfoBasic(
                request.nickname(),
                request.profileUrl()
        );

        findUser.updateUserInfoAll(
                request.jobType(),
                request.jobYear(),
                request.intro(),
                request.email(),
                request.contact(),
                request.githubUrl(),
                request.blogUrl()
        );

        List<Stack> stacks = stackService.findOrCreateStacks(request.user_stacks());
        stacks.forEach(findUser::addStack);

        User updateUser = userRepository.save(findUser);
        eventPublisher.publishEvent(UserUpdateEvent.of(updateUser));

        return UserInfoResponseDto.of(updateUser);
    }

    @Transactional
    public void deleteUser(Long userId, UserWithdrawRequestDto request) {
        User findUser = getUserById(userId);
        findUser.withdrawWithAddReason(request.reasonTypeList(), request.detail());
        userRepository.save(findUser);
        productRepository.updateStatusdByUsertId(userId);
        reviewRepository.updateStatusdByUsertId(userId);
    }

    private List<PopularityUserResponseDto> getPopularityUsers() {
        Pageable pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("count")));
        return likeRepository.countPopularityUser(pageRequest);
    }

    public List<PopularityUserInfoResponseDto> getPopularityUsersInfo() {

        List<PopularityUserInfoResponseDto> popularityUsersInfo = getPopularityUsers().stream()
                .map(popularityUser -> {
                    User findUser = userRepository.findByIdAndStatusIsNotDeleted(popularityUser.getUserId())
                            .orElseThrow(() -> new UserException(NOT_EXIST_USER));

                    PopularityUserReviewResponseDto userReviewInfo = reviewRepository.findByUserIdReviewStats(popularityUser.getUserId());

                    return PopularityUserInfoResponseDto.of(
                            findUser,
                            userReviewInfo
                    );
                })
                .collect(Collectors.toList());
        return popularityUsersInfo;
    }

    public List<ReasonTypeDto> getReasonTypeList() {
        return Arrays.stream(ReasonType.values())
                .map(reasonType -> new ReasonTypeDto(reasonType.name(), reasonType.description()))
                .collect(Collectors.toList());
    }
}
