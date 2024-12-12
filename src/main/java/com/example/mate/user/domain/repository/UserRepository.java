package com.example.mate.user.domain.repository;

import com.example.mate.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
            SELECT u FROM User u
             WHERE u.id = :userId
             AND u.status != 'DELETED'
            """)
    Optional<User> findByIdAndStatusIsNotDeleted(Long userId);

    Optional<User> findByKakaoId(String kakao);

    boolean existsByNickname(String nickname);
}
