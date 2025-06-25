package com.example.mate.user.application;


import com.example.mate.auth.infrastructure.jwt.JwtProvider;
import com.example.mate.product.application.LikeService;
import com.example.mate.product.application.ProductService;
import com.example.mate.product.application.dto.*;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.repository.LikeProductSrchRepository;
import com.example.mate.product.domain.repository.ProductRepository;
import com.example.mate.product.domain.repository.ProductSrchRepository;
import com.example.mate.review.application.ReviewService;
import com.example.mate.user.application.dto.UserInfoPageResponseDto;
import com.example.mate.user.application.dto.UserInfoResponseDto;
import com.example.mate.user.application.dto.UserSrchRequestDto;
import com.example.mate.user.domain.User;
import com.example.mate.user.domain.repository.UserSrchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserSearchService {

    private final UserSrchRepository userSrchRepository;

    @Transactional
    public UserInfoPageResponseDto getFreeLancer(
            UserSrchRequestDto request
    ) {
        //TODO: 나중에 필요하면
        Pageable pageable = PageRequest.of(request.page(), request.size());

        List<User> findUsers = userSrchRepository.findWithUserAndTags(request);

        List<UserInfoResponseDto> userInfoAll = findUsers.stream()
                .map(UserInfoResponseDto::of)
                .collect(Collectors.toList());

        int start = request.page() * request.size();
        int end = Math.min(start + request.size(), userInfoAll.size());
        List<UserInfoResponseDto> pagedList = userInfoAll.subList(start, end);

        return new UserInfoPageResponseDto(pagedList, request.page(), end < userInfoAll.size());
    }
}
