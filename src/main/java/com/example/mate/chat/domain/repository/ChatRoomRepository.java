package com.example.mate.chat.domain.repository;

import com.example.mate.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
            SELECT cr FROM ChatRoom cr
            WHERE cr.product.id = :productId
            """)
    Optional<ChatRoom> findChatRoomTokenByProductId(@Param("productId") Long productId);

    @Query("""
            SELECT cr FROM ChatRoom cr
            WHERE cr.user1Id = :user1Id
              AND cr.user2Id = :user2Id
              AND cr.product.id = :productId
            """)
    ChatRoom findChatRoomByUser1UdAndUser2Id(Long user1Id, Long user2Id, Long productId);
}
