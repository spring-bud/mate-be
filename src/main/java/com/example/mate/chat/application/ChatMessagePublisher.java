package com.example.mate.chat.application;

import com.example.mate.chat.application.dto.ChatMessageInfoDto;

public interface ChatMessagePublisher {

    void execute(ChatMessageInfoDto chatMessage);
}
