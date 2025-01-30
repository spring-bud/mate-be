package com.example.mate.chat.application;

import com.example.mate.chat.domain.ChatRoom;
import com.example.mate.chat.domain.repository.ChatRoomRepository;
import com.example.mate.product.domain.event.ProductCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomEventListener {

    private final ChatRoomRepository chatRoomRepository;

    @EventListener
    public void handleChatRoomCreated(ProductCreateEvent event) {
        ChatRoom newChatRoom = new ChatRoom(event.product(), event.custromerId());
        chatRoomRepository.save(newChatRoom);
    }
}
