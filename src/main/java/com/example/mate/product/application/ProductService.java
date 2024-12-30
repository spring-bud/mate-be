package com.example.mate.product.application;

import com.example.mate.product.application.dto.ProductCreateRequestDto;
import com.example.mate.product.application.dto.ProductIdResponseDto;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.Tag;
import com.example.mate.product.domain.repository.ProductRepository;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserService userService;
    private final TagService tagService;
    private final ProductRepository productRepository;

    @Transactional
    public ProductIdResponseDto createProduct(Long userId, ProductCreateRequestDto request) {
        User findUser = userService.getUserById(userId);

        Product newProduct = Product.builder()
                .user(findUser)
                .title(request.title())
                .category(request.category())
                .content(request.content())
                .thumbnailUrl(request.thumbnailUrl())
                .build();

        List<Tag> tags = tagService.findOrCreateTags(request.tags());
        tags.forEach(newProduct::addTag);

        Product savedProduct = productRepository.save(newProduct);

        return new ProductIdResponseDto(savedProduct.getId());
    }
}
