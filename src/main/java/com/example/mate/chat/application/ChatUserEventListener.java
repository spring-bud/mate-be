package com.example.mate.chat.application;

import com.example.mate.chat.domain.ChatUser;
import com.example.mate.chat.domain.repository.ChatUserRepository;
import com.example.mate.chat.exception.ChatException;
import com.example.mate.user.domain.event.UserCreateEvent;
import com.example.mate.user.domain.event.UserUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.example.mate.chat.exception.ChatExceptionType.INVALID_CHAT_USER_ID_EXCEPTION;

@Component
@RequiredArgsConstructor
public class ChatUserEventListener {

    private final ChatUserRepository chatUserRepository;

    @EventListener
    public void handleUserCreated(UserCreateEvent event) {
        ChatUser chatUser = createUserIfNotExistsOrUpdate(event);
        chatUserRepository.save(chatUser);
    }

    @EventListener
    public void handleUserUpdated(UserUpdateEvent event) {
        ChatUser findUser = chatUserRepository.findByUserId(event.userId())
                .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));
        findUser.updateUserInfo(event.nickname(), event.profileUrl());
        chatUserRepository.save(findUser);
    }

    private ChatUser createUserIfNotExistsOrUpdate(UserCreateEvent event) {
        return chatUserRepository.findByUserId(event.userId())
                .map(user -> {
                    user.updateUserInfo(event.nickname(), event.profileUrl());
                    return user;
                })
                .orElseGet(() -> ChatUser.builder()
                        .userId(event.userId())
                        .nickname(event.nickname())
                        .profileUrl(event.profileUrl())
                        .build());
    }
}
