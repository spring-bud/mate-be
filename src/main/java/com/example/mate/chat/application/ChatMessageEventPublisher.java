package com.example.mate.chat.application;

public interface ChatMessageEventPublisher {

    <T> void execute(T message);
}
