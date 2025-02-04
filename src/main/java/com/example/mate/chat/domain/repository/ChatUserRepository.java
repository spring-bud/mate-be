package com.example.mate.chat.domain.repository;

import com.example.mate.chat.domain.ChatUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatUserRepository extends MongoRepository<ChatUser, Long> {

    Optional<ChatUser> findByUserId(Long userId);

    List<ChatUser> findAllByUserIdIn(Set<Long> userIdSet);
}
