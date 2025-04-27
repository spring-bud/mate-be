package com.example.mate.product.application;

import com.example.mate.product.application.dto.ProductCreateRequestDto;
import com.example.mate.product.application.dto.ProductIdResponseDto;
import com.example.mate.product.application.dto.ProductUpdateRequestDto;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.Tag;
import com.example.mate.product.domain.repository.ProductRepository;
import com.example.mate.product.exception.ProductException;
import com.example.mate.review.domain.repository.ReviewRepository;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.mate.product.exception.ProductExceptionType.PRODUCT_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final UserService userService;
    private final TagService tagService;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

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

    @Transactional
    public ProductUpdateRequestDto updateProduct(Long userId, Long productId, ProductCreateRequestDto request) {

        Product findProduct = findProductById(productId);

        findProduct.isOwnerOrThrow(userId);

        findProduct.updateProductInfoAll(
                request.title(),
                request.category(),
                request.content(),
                request.thumbnailUrl()
        );

        List<Tag> tags = tagService.findOrCreateTags(request.tags());

        findProduct.syncTag(tags);

        Product savedProduct = productRepository.save(findProduct);

        return ProductUpdateRequestDto.of(savedProduct);
    }

    // External Service
    public Product findProductById(Long productId) {
        return productRepository.findByIdWithUserAndTags(productId)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND_EXCEPTION));
    }

    @Transactional
    public void deleteProduct(Long userId, Long productId) {
        Product findProduct = findProductById(productId);
        findProduct.softDelete(userId);
        reviewRepository.updateStatusdByProductId(productId);
    }
}
