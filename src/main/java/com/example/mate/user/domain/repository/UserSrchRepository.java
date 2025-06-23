package com.example.mate.user.domain.repository;

import com.example.mate.product.application.dto.ProductSrchRequestDto;
import com.example.mate.product.domain.Product;
import com.example.mate.user.application.dto.UserSrchRequestDto;
import com.example.mate.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSrchRepository {

    List<User> findWithUserAndTags(UserSrchRequestDto request);
}
