package com.example.mate.user.infrastructure.querydsl;

import com.example.mate.user.application.dto.UserSrchRequestDto;
import com.example.mate.user.domain.User;
import com.example.mate.user.domain.UserStatus;
import com.example.mate.user.domain.repository.UserSrchRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.mate.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class QueryDslUserSearchRepository implements UserSrchRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findWithUserAndTags(UserSrchRequestDto request) {
        return queryFactory.selectFrom(user)
                .leftJoin(user.userStacks).fetchJoin()
                .where(
                        jobTypeYn(request.jobtype()),
                        nicknameYn(request.nickname()),
                        stackYn(request.stacks()),
                        user.status.ne(UserStatus.valueOf("DELETED")),
                        user.infoActive.eq(true)
                )
                .fetch();
    }

    private BooleanExpression jobTypeYn(String jobType) {
        return jobType == null || jobType.isEmpty() ? null : user.jobType.eq(jobType);
    }

    private BooleanExpression nicknameYn(String nickname) {
        return nickname == null || nickname.isEmpty() ? null : user.nickname.contains(nickname);
    }

    private BooleanExpression stackYn(List<String> stacks) {
        if (stacks == null || stacks.isEmpty()) {
            return null;
        }

        BooleanExpression expression = user.userStacks.any().stack.name.contains(stacks.get(0));

        for (int i = 1; i < stacks.size(); i++) {
            expression = expression.or(user.userStacks.any().stack.name.contains(stacks.get(i)));
        }

        return expression;
    }
}