package com.example.mate.user.application.dto;

import java.util.List;

public record UserSrchRequestDto(
        String jobtype,
        String nickname,
        List<String> stacks,
        int size,
        int page
) {
}
