package com.example.mate.user.presentation;

import com.example.mate.common.response.ApiResponse;
import com.example.mate.user.application.UserService;
import com.example.mate.user.application.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ApiResponse<UserInfoResponseDto>> updateOUserInfo(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserInfoRequestDto request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                userService.updateUser(request, userId)
        ));
    }

    @GetMapping("/popularity")
    public ResponseEntity<ApiResponse<List<PopularityUserInfoResponseDto>>> getPopularityUserInfo(
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                userService.getPopularityUsersInfo())
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserWithdrawRequestDto request
    ) {
        userService.deleteUser(userId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reasontype")
    public ResponseEntity<ApiResponse<List<ReasonTypeDto>>> getReasonTypeList(
    ) {
        return ResponseEntity.ok(new ApiResponse<>(userService.getReasonTypeList()));
    }
}
