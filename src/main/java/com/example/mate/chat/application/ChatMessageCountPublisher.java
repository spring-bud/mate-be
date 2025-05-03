package com.example.mate.chat.application;

import com.example.mate.chat.application.dto.ChatLastMessageAndCountDto;

public interface ChatMessageCountPublisher {

    void execute(Long senderId, String roomToken, ChatLastMessageAndCountDto chatLastMessageAndCountDto);
}
