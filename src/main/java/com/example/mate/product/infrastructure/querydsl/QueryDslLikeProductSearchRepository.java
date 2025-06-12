package com.example.mate.product.infrastructure.querydsl;

import com.example.mate.product.application.dto.ProductSrchRequestDto;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.ProductCategory;
import com.example.mate.product.domain.ProductStatus;
import com.example.mate.product.domain.repository.LikeProductSrchRepository;
import com.example.mate.user.domain.UserStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mate.product.domain.QLike.like;
import static com.example.mate.product.domain.QProduct.product;
import static com.example.mate.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class QueryDslLikeProductSearchRepository implements LikeProductSrchRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findLikeProducts(ProductSrchRequestDto request, Long userId) {
        return queryFactory.selectFrom(product)
                .innerJoin(like).on(like.product.id.eq(product.id))
                .leftJoin(product.user, user).fetchJoin()
                .where(
                        categoryYn(request.category()),
                        titleYn(request.title()),
                        tagYn(request.tag()),
                        like.user.id.eq(userId),
                        user.status.ne(UserStatus.valueOf("DELETED")),
                        product.status.ne(ProductStatus.valueOf("DELETED"))
                )
                .fetch();
    }

    private BooleanExpression categoryYn(String category) {
        return category == null || category.isEmpty() ? null : product.category.eq(ProductCategory.valueOf(category));
    }

    // title 조건을 처리
    private BooleanExpression titleYn(String title) {
        return title == null || title.isEmpty() ? null : product.title.contains(title);
    }

    private BooleanExpression tagYn(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }

        // 여러 개의 태그가 하나라도 포함되면 조건을 만족하는 OR 조건 생성
        BooleanExpression expression = product.productTags.any().tag.name.contains(tags.get(0));

        for (int i = 1; i < tags.size(); i++) {
            expression = expression.or(product.productTags.any().tag.name.contains(tags.get(i)));
        }

        return expression;
    }
}
