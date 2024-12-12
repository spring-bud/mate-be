package com.example.mate.user.application;

import static com.example.mate.user.exception.UserExceptionType.NOT_EXIST_USER;

import com.example.mate.user.application.dto.UserInfoResponseDto;
import com.example.mate.user.domain.User;
import com.example.mate.user.domain.repository.UserRepository;
import com.example.mate.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponseDto getUserInfoWithId(Long userId) {
        User findUser = userRepository.findByIdAndStatusIsNotDeleted(userId)
                .orElseThrow(() -> new UserException(NOT_EXIST_USER));
        return UserInfoResponseDto.of(findUser);
    }
}
