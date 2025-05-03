package com.example.mate.chat.infrastructure.stomp;

import com.example.mate.chat.application.ChatMessageCountPublisher;
import com.example.mate.chat.application.dto.ChatLastMessageAndCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompMessageCountPublisher implements ChatMessageCountPublisher {

    private static final String SUBSCRIBE_URL = "/sub/count/chat-rooms/";

    private final SimpMessageSendingOperations messagingTemplate;

    @Override
    public void execute(Long senderId, String roomToken, ChatLastMessageAndCountDto chatLastMessageAndCountDto) {
        messagingTemplate.convertAndSend(SUBSCRIBE_URL + senderId + "/" + roomToken, chatLastMessageAndCountDto);
    }
}
