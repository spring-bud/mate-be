package com.example.mate.chat.domain.repository;

import com.example.mate.chat.domain.ChatMessage;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, ObjectId> {

    List<ChatMessage> findAllByRoomTokenOrderByCreatedAtDesc(String roomToken);

    Slice<ChatMessage> findByRoomToken(String roomToken, Pageable pageable);

    Slice<ChatMessage> findByRoomTokenAndIdLessThan(String roomToken, ObjectId cursorId, Pageable pageable);

    void deleteByRoomToken(String roomToken);
}
