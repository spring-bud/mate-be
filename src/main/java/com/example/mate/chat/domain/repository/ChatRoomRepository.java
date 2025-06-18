package com.example.mate.chat.domain.repository;

import com.example.mate.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("""
            SELECT cr FROM ChatRoom cr
            LEFT JOIN FETCH cr.product p
            WHERE cr.user1Id = :user1Id
              AND cr.user2Id = :user2Id
              AND cr.product.id = :productId
              AND p.status != 'DELETED'
            """)
    ChatRoom findChatRoomByUser1UdAndUser2Id(Long user1Id, Long user2Id, Long productId);

    @Query("""
            SELECT cr FROM ChatRoom cr
            LEFT JOIN FETCH cr.product p
            WHERE cr.user1Id = :user1Id
              AND cr.user2Id = :user2Id
              AND cr.product.id = :productId
              AND cr.user1Active = true
              AND cr.user2Active = true
              AND p.status != 'DELETED'
            """)
    ChatRoom findActiveChatRoomByUser1UdAndUser2Id(Long user1Id, Long user2Id, Long productId);

    @Query("""
            SELECT cr FROM ChatRoom cr
            LEFT JOIN FETCH cr.product p
            WHERE (cr.user1Id = :userId AND cr.user1Active = true)
              OR (cr.user2Id = :userId AND cr.user2Active = true)
              AND p.status != 'DELETED'
            """)
    List<ChatRoom> findChatRoomListByUserId(Long userId);

    @Query("""
            SELECT cr FROM ChatRoom cr
            LEFT JOIN FETCH cr.product p
            WHERE cr.roomToken = :roomToken
              AND p.status != 'DELETED'
            """)
    ChatRoom findChatRoomBytokenId(String roomToken);

    @Modifying
    @Query("""
            UPDATE ChatRoom cr
             SET cr.user1Active = false
             WHERE cr.user1Id = :userId
              AND cr.roomToken = :roomToken
            """)
    void leaveChatRoomUser1(Long userId, String roomToken);

    @Modifying
    @Query("""
            UPDATE ChatRoom cr
             SET cr.user2Active = false
             WHERE cr.user2Id = :userId
              AND cr.roomToken = :roomToken
            """)
    void leaveChatRoomUser2(Long userId, String roomToken);

    @Modifying
    @Query("""
        DELETE FROM ChatRoom cr
        WHERE cr.roomToken = :roomToken
        """)
    void deleteByRoomToken(String roomToken);
}
