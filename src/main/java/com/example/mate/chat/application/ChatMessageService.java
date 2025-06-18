package com.example.mate.chat.application;

import com.example.mate.chat.application.dto.ChatLastMessageAndCountDto;
import com.example.mate.chat.application.dto.ChatMessageDto;
import com.example.mate.chat.application.dto.ChatMessageInfoDto;
import com.example.mate.chat.application.dto.ChatMessageInfoDto.EnterAndLeaveMessage;
import com.example.mate.chat.application.dto.ChatMessageResponseDto;
import com.example.mate.chat.domain.*;
import com.example.mate.chat.domain.repository.ChatMessageRepository;
import com.example.mate.chat.domain.repository.ChatReadRepository;
import com.example.mate.chat.domain.repository.ChatRoomRepository;
import com.example.mate.chat.domain.repository.ChatUserRepository;
import com.example.mate.chat.exception.ChatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.mate.chat.domain.MessageType.ENTER;
import static com.example.mate.chat.domain.MessageType.LEAVE;
import static com.example.mate.chat.exception.ChatExceptionType.INVALID_CHAT_USER_ID_EXCEPTION;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatReadRepository chatReadRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageEventPublisher messageEventPublisher;
    private final ChatMessageCountPublisher chatMessageCountPublisher;
    private final ChatService chatService;

    public void sendChatMessage(Long userId, ChatMessageDto message) {
        ChatMessageInfoDto messageInfoDto = createAndSaveMessage(userId, message);

        ChatRoom chatRoomInfo = chatRoomRepository.findChatRoomBytokenId(message.roomToken());

        Long recUserId = -1L;
        if (chatRoomInfo.getUser1Id() == userId) {
            recUserId = chatRoomInfo.getUser2Id();
        } else {
            recUserId = chatRoomInfo.getUser1Id();
        }

        ChatMessageResponseDto responseDto = chatService.getRecentMessagesByRoomToken(message.roomToken());
        List<ChatMessageInfoDto> messages = responseDto.messages();
        String latestMessage = messages.get(0).message();
        String createdAt = messages.get(0).createdAt();


        Long messageCount = chatReadRepository.countByRoomTokenAndSenderId(message.roomToken(), userId);
        ChatLastMessageAndCountDto dto = new ChatLastMessageAndCountDto(latestMessage, messageCount, createdAt);
        if (recUserId != null) {
            chatMessageCountPublisher.execute(recUserId, messageInfoDto.roomToken(), dto);
        }

        messageEventPublisher.execute(messageInfoDto);
    }

    @Transactional
    public void sendEnterLeaveMessage(Long userId, ChatMessageDto message) {
        ChatUser findUser = chatUserRepository.findByUserId(userId)
                .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));

        ChatRoom chatRoomInfo = chatRoomRepository.findChatRoomBytokenId(message.roomToken());

        if (message.type().equals(LEAVE)) {
            if (chatRoomInfo.getUser1Id() == userId) {
                chatRoomRepository.leaveChatRoomUser1(userId, message.roomToken());
            } else {
                chatRoomRepository.leaveChatRoomUser2(userId, message.roomToken());
            }
        }
        String formattedMessage = formatEnterLeaveMessage(message.type(), findUser.getNickname());
        EnterAndLeaveMessage messageInfoDto = createEnterLeaveMessage(message, formattedMessage);
        messageEventPublisher.execute(messageInfoDto);

//        ChatRoom changeChatRoomInfo = chatRoomRepository.findChatRoomBytokenId(message.roomToken());
//        if(changeChatRoomInfo.isUser1Active() == false && changeChatRoomInfo.isUser2Active() == false){
//            chatReadRepository.deleteByRoomToken(message.roomToken());
//            chatMessageRepository.deleteByRoomToken(message.roomToken());
//            chatRoomRepository.deleteByRoomToken(message.roomToken());
//        }
    }

    public ChatMessageInfoDto createAndSaveMessage(Long userId, ChatMessageDto message) {
        ChatUser findUser = chatUserRepository.findByUserId(userId)
                .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));
        ChatMessage chatMessage = ChatMessage.builder()
                .type(message.type())
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

        ChatRoom chatRoomInfo = chatRoomRepository.findChatRoomBytokenId(message.roomToken());
        if(chatRoomInfo.isUser1Active() == true && chatRoomInfo.isUser2Active() == true){
            chatReadRepository.save(chatRead);
        }
        return ChatMessageInfoDto.of(savedChatMessage, findUser);
    }

    //TODO: 보류 처리 미사용시 삭제
    private String formatEnterLeaveMessage(MessageType type, String nickname) {
        return (type.equals(ENTER) ? ENTER.getMessage() : LEAVE.getMessage())
                .formatted(nickname);
    }

    private EnterAndLeaveMessage createEnterLeaveMessage(ChatMessageDto messageDto, String message) {
        return EnterAndLeaveMessage.of(
                messageDto.type().name(),
                messageDto.roomToken(),
                message,
                LocalDateTime.now()
        );
    }
}
