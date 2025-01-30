package com.example.mate.chat.infrastructure.stomp;

import com.example.mate.chat.application.ChatMessagePublisher;
import com.example.mate.chat.application.dto.ChatMessageInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompMessagePublisher implements ChatMessagePublisher {

    private static final String SUBSCRIBE_URL = "/sub/chat-rooms/";

    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void execute(ChatMessageInfoDto chatMessage) {
        messagingTemplate.convertAndSend(SUBSCRIBE_URL + chatMessage.roomToken(), chatMessage);
    }
}
