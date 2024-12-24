package com.example.mate.user.application;

import com.example.mate.user.application.dto.UserInfoResponseDto;
import com.example.mate.user.domain.User;
import com.example.mate.user.domain.repository.UserRepository;
import com.example.mate.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.mate.user.exception.UserExceptionType.NOT_EXIST_USER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponseDto getUserInfoWithId(Long userId) {
        User findUser = userRepository.findByIdAndStatusIsNotDeleted(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        return UserInfoResponseDto.of(findUser);
    }

    @Transactional
    public User getOrRegisterByOAuthId(String oAuthId) {
        return userRepository.findByKakaoId(oAuthId)
                .map(this::activateIfDeleted)
                .orElseGet(() -> registerNewUserAndPublish(oAuthId));
    }

    private User activateIfDeleted(User user) {
        if (user.isDeletedUser()) {
            user.updateActiveStatus();
        }
        return user;
    }

    private User registerNewUserAndPublish(String oAuthId) {
        User newUser = User.builder().kakaoId(oAuthId).build();
        User saveUser = userRepository.save(newUser);
        return saveUser;
    }
}
