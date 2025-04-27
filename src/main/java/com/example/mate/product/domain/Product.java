package com.example.mate.product.domain;

import com.example.mate.common.domain.BaseTimeEntity;
import com.example.mate.product.exception.ProductException;
import com.example.mate.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.mate.product.exception.ProductExceptionType.NO_PERMISSIONS_ON_PRODUCT;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductTag> productTags = new ArrayList<>();

    @Builder
    public Product(User user, String thumbnailUrl, String title, String content, ProductCategory category) {
        this.user = user;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.content = content;
        this.status = ProductStatus.ACTIVE;
    }

    public void updateProductInfoAll(
            String title,
            ProductCategory category,
            String content,
            String thumbnailUrl
    ) {

        if (title != null) {
            this.title = title;
        }

        if (category != null) {
            this.category = category;
        }

        if (content != null) {
            this.content = content;
        }

        if (thumbnailUrl != null) {
            this.thumbnailUrl = thumbnailUrl;
        }
    }

    public void softDelete(Long userId) {
        isOwnerOrThrow(userId);
        this.status = ProductStatus.DELETED;

    }

    public void isOwnerOrThrow(Long userId) {
        if (!user.getId().equals(userId)) {
            throw new ProductException(NO_PERMISSIONS_ON_PRODUCT);
        }
    }

    public void addTag(Tag tag) {
        ProductTag productTag = new ProductTag(this, tag);
        productTags.add(productTag);
    }

    public void removeTag(Tag tag) {
        productTags.removeIf(productTag -> productTag.getTag().equals(tag));
    }

    public void syncTag(List<Tag> newTag) {
        List<Tag> currentTag = productTags.stream()
                .map(ProductTag::getTag)
                .toList();

        List<Tag> toRemove = currentTag.stream()
                .filter(existing -> !newTag.contains(existing))
                .toList();

        List<Tag> toAdd = newTag.stream()
                .filter(newStack -> !currentTag.contains(newStack))
                .toList();

        toRemove.forEach(this::removeTag);
        toAdd.forEach(this::addTag);
    }
}
