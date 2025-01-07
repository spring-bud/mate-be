package com.example.mate.user.presentation;

import com.example.mate.common.response.ApiResponse;
import com.example.mate.user.application.UserService;
import com.example.mate.user.application.dto.UserInfoRequestDto;
import com.example.mate.user.application.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserInfoResponseDto>> getUserInfo(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                userService.getUserInfoWithId(userId)
        ));
    }

    @PatchMapping
    public ResponseEntity<UserInfoResponseDto> updateOUserInfo(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserInfoRequestDto request
    ) {
        return ResponseEntity.ok(userService.updateUser(request, userId));
    }
}
