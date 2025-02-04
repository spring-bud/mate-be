package com.example.mate.chat.application;

import com.example.mate.chat.application.dto.ChatMessageDto;
import com.example.mate.chat.application.dto.ChatMessageInfoDto;
import com.example.mate.chat.domain.ChatMessage;
import com.example.mate.chat.domain.ChatRead;
import com.example.mate.chat.domain.ChatUser;
import com.example.mate.chat.domain.repository.ChatMessageRepository;
import com.example.mate.chat.domain.repository.ChatReadRepository;
import com.example.mate.chat.domain.repository.ChatUserRepository;
import com.example.mate.chat.exception.ChatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.mate.chat.domain.MessageType.TALK;
import static com.example.mate.chat.exception.ChatExceptionType.INVALID_CHAT_USER_ID_EXCEPTION;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatReadRepository chatReadRepository;
    private final ChatMessageEventPublisher messageEventPublisher;
    private final ChatMessageCountPublisher chatMessageCountPublisher;

    public void sendChatMessage(Long userId, ChatMessageDto message) {
        ChatMessageInfoDto messageInfoDto = createAndSaveMessage(userId, message);
        Long messageCount = chatReadRepository.countByRoomTokenAndSenderId(message.roomToken(), userId);
        chatMessageCountPublisher.execute(messageInfoDto.senderId(), messageInfoDto.roomToken(), messageCount);
        messageEventPublisher.execute(messageInfoDto);
    }

    //TODO: 보류 처리 미사용시 삭제
//    public void sendEnterLeaveMessage(Long userId, ChatMessageDto message) {
//        ChatUser findUser = chatUserRepository.findByUserId(userId)
//                .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));
//
//        String formattedMessage = formatEnterLeaveMessage(message.type(), findUser.getNickname());
//        EnterAndLeaveMessage messageInfoDto = createEnterLeaveMessage(message, formattedMessage);
//        messageEventPublisher.execute(messageInfoDto);
//    }

    private ChatMessageInfoDto createAndSaveMessage(Long userId, ChatMessageDto message) {
        ChatUser findUser = chatUserRepository.findByUserId(userId)
                .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));
        ChatMessage chatMessage = ChatMessage.builder()
                .type(TALK)
                .roomToken(message.roomToken())
                .senderId(userId)
                .message(message.message())
                .build();
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);
        ChatRead chatRead = ChatRead.builder()
                .roomToken(message.roomToken())
                .messageId(savedChatMessage.getId())
                .senderId(userId)
                .build();
        chatReadRepository.save(chatRead);
        return ChatMessageInfoDto.of(savedChatMessage, findUser);
    }

    //TODO: 보류 처리 미사용시 삭제
//    private String formatEnterLeaveMessage(MessageType type, String nickname) {
//        return (type.equals(ENTER) ? ENTER.getMessage() : LEAVE.getMessage())
//                .formatted(nickname);
//    }
//
//    private EnterAndLeaveMessage createEnterLeaveMessage(ChatMessageDto messageDto, String message) {
//        return EnterAndLeaveMessage.of(
//                messageDto.type().name(),
//                messageDto.roomToken(),
//                message,
//                LocalDateTime.now()
//        );
//    }
}
