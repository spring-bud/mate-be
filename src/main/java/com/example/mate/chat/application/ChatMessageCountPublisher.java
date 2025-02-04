package com.example.mate.chat.application;

public interface ChatMessageCountPublisher {

    void execute(Long senderId, String roomToken, Long messageCount);
}
