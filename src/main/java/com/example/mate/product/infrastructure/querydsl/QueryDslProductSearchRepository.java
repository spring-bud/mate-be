package com.example.mate.product.infrastructure.querydsl;

import com.example.mate.product.application.dto.ProductSrchRequestDto;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.ProductCategory;
import com.example.mate.product.domain.repository.ProductSrchRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mate.product.domain.QProduct.product;

@Repository
@RequiredArgsConstructor
public class QueryDslProductSearchRepository implements ProductSrchRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findWithUserAndTags(ProductSrchRequestDto request, Pageable pageable) {
        JPAQuery<Product> result = queryFactory.selectFrom(product)
                .where(
                        categoryYn(request.category()),
                        titleYn(request.title()),
                        tagYn(request.tag())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Product> results = result.fetch();

        // 전체 개수 조회 (페이징 위해 필요)
        Long total = queryFactory.select(product.count())
                .from(product)
                .where(
                        categoryYn(request.category()),
                        titleYn(request.title()),
                        tagYn(request.tag())
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
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
