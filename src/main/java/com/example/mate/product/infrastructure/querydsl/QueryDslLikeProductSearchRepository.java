package com.example.mate.product.infrastructure.querydsl;

import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.ProductStatus;
import com.example.mate.product.domain.repository.LikeProductSrchRepository;
import com.example.mate.user.domain.UserStatus;
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
    public List<Product> findLikeProducts(Long userId) {
        return queryFactory.selectFrom(product)
                .innerJoin(like).on(like.product.id.eq(product.id))
                .leftJoin(product.user, user).fetchJoin()
                .where(
                        like.user.id.eq(userId),
                        user.status.ne(UserStatus.valueOf("DELETED")),
                        product.status.ne(ProductStatus.valueOf("DELETED"))
                )
                .fetch();
    }


}
