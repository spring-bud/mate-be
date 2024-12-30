package com.example.mate.product.domain;

import static com.example.mate.product.exception.ProductExceptionType.NO_PERMISSIONS_ON_PRODUCT;

import com.example.mate.common.domain.BaseTimeEntity;
import com.example.mate.product.exception.ProductException;
import com.example.mate.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void softDelete(Long userId) {
        isOwnerOrThrow(userId);
        this.status = ProductStatus.DELETED;

    }

    private void isOwnerOrThrow(Long userId) {
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
}
