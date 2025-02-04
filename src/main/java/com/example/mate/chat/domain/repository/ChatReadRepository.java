package com.example.mate.chat.domain.repository;

import com.example.mate.chat.domain.ChatRead;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatReadRepository extends MongoRepository<ChatRead, ObjectId> {
    long countByRoomTokenAndSenderId(String roomToken, Long senderId);

    void deleteByRoomToken(String roomToken);
}
