package com.example.mate.chat.application.dto;

import com.example.mate.chat.domain.ChatRoom;
import com.example.mate.chat.domain.ChatUser;

public record ChatRoomListDto(
        Long id,
        String roomToken,
        Long user1Id,
        Long user2Id,
        Long productId,
        String productThumbnailUrl,
        String productTitle,
        String otherUserNickName,
        String otherUserProfileUrl
) {
    public static ChatRoomListDto of(ChatRoom chatRoom, ChatUser otherUser) {
        return new ChatRoomListDto(
                chatRoom.getId(),
                chatRoom.getRoomToken(),
                chatRoom.getUser1Id(),
                chatRoom.getUser2Id(),
                chatRoom.getProduct().getId(),
                chatRoom.getProduct().getThumbnailUrl(),
                chatRoom.getProduct().getTitle(),
                otherUser.getNickname(),
                otherUser.getProfileUrl()
        );
    }
}
