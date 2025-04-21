package com.example.mate.product.infrastructure.querydsl;

import com.example.mate.product.application.dto.ProductSrchRequestDto;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.ProductCategory;
import com.example.mate.product.domain.repository.ProductSrchRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mate.product.domain.QProduct.product;

@Repository
@RequiredArgsConstructor
public class QueryDslProductSearchRepository implements ProductSrchRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findWithUserAndTags(ProductSrchRequestDto request) {
        return queryFactory.selectFrom(product)
                .where(
                        categoryYn(request.category()),
                        titleYn(request.title()),
                        tagYn(request.tag())
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
