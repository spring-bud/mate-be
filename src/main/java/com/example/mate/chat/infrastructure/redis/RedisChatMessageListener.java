package com.example.mate.chat.infrastructure.redis;

import com.example.mate.chat.application.ChatMessagePublisher;
import com.example.mate.chat.application.dto.ChatMessageInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RedisChatMessageListener implements MessageListener {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final ChatMessagePublisher chatMessagePublisher;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ChatMessageInfoDto payload = deserializeFromBytes(message.getBody());
        chatMessagePublisher.execute(payload);
    }

    private ChatMessageInfoDto deserializeFromBytes(byte[] bytes) {
        try {
            return objectMapper.readValue(bytes, ChatMessageInfoDto.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
