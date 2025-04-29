package com.example.mate.chat.application;

import com.example.mate.chat.application.dto.ChatMessageInfoDto;
import com.example.mate.chat.application.dto.ChatMessageResponseDto;
import com.example.mate.chat.application.dto.ChatRoomListDto;
import com.example.mate.chat.application.dto.ChatRoomTokenDto;
import com.example.mate.chat.domain.ChatMessage;
import com.example.mate.chat.domain.ChatRoom;
import com.example.mate.chat.domain.ChatUser;
import com.example.mate.chat.domain.repository.ChatMessageRepository;
import com.example.mate.chat.domain.repository.ChatReadRepository;
import com.example.mate.chat.domain.repository.ChatRoomRepository;
import com.example.mate.chat.domain.repository.ChatUserRepository;
import com.example.mate.chat.exception.ChatException;
import com.example.mate.product.application.ProductService;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.event.ProductCreateEvent;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.mate.chat.exception.ChatExceptionType.INVALID_CHAT_USER_ID_EXCEPTION;
import static com.example.mate.chat.exception.ChatExceptionType.INVALID_MAKE_CHAT_ROOM_USER_ID_EXCEPTION;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final String MESSAGE_SORT_FIELD_CREATED_AT = "createdAt";
    private static final int RECENT_MESSAGES_COUNT = 1;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductService productService;
    private final ChatReadRepository chatReadRepository;
    private final ChatMessageCountPublisher chatMessageCountPublisher;

    public ChatRoomTokenDto createChatRoom(Long productId, Long custromerId) {
        Product product = productService.findProductById(productId);

        if (product.getUser().getId().equals(custromerId)) {
            throw new ChatException(INVALID_MAKE_CHAT_ROOM_USER_ID_EXCEPTION);
        }

        ChatRoom findChatRoom = chatRoomRepository.findChatRoomByUser1UdAndUser2Id(product.getUser().getId(), custromerId, productId);

        if (findChatRoom == null) {
            eventPublisher.publishEvent(new ProductCreateEvent(product, custromerId));

            ChatRoom chatRoomToken = chatRoomRepository.findChatRoomByUser1UdAndUser2Id(product.getUser().getId(), custromerId, productId);

            return new ChatRoomTokenDto(chatRoomToken.getRoomToken());
        }
        return new ChatRoomTokenDto(findChatRoom.getRoomToken());
    }

    public ChatMessageResponseDto getMessagesByRoomToken(String roomToken, Long userId, ObjectId cursorId, int limit) {
        chatReadRepository.deleteByRoomToken(roomToken);
        ChatRoom chatRoomInfo = chatRoomRepository.findChatRoomBytokenId(roomToken);
        Long otherUser;
        if (userId == chatRoomInfo.getUser1Id()) {
            otherUser = chatRoomInfo.getUser2Id();
        } else {
            otherUser = chatRoomInfo.getUser1Id();
        }
        Long messageCount = chatReadRepository.countByRoomTokenAndSenderId(roomToken, otherUser);
        chatMessageCountPublisher.execute(otherUser, roomToken, messageCount);

        List<ChatMessage> pagedChatMessageList = getPagedChatMessages(roomToken, cursorId, limit);
        boolean hasNext = checkHasNextPage(pagedChatMessageList, limit);
        List<ChatMessage> chatMessageList = getChatMessagesToLimit(pagedChatMessageList, hasNext, limit);
        Map<Long, ChatUser> userInfoMap = getUserInfoForMessages(chatMessageList);
        return mapMessagesToDto(chatMessageList, userInfoMap, hasNext);
    }

    public ChatMessageResponseDto getRecentMessagesByRoomToken(String roomToken) {
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByRoomTokenOrderByCreatedAtDesc(roomToken)
                .stream()
                .limit(RECENT_MESSAGES_COUNT)
                .toList();
        Map<Long, ChatUser> userInfoMap = getUserInfoForMessages(chatMessageList);
        return mapMessagesToDto(chatMessageList, userInfoMap, false);
    }

    public List<ChatRoomListDto> getChatRoomList(Long userId) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findChatRoomListByUserId(userId);

        List<ChatRoomListDto> chatRoomListInfo = chatRoomList.stream()
                .map(chatRoomInfo -> {
                    Long otherUser;
                    if (chatRoomInfo.getUser1Id().equals(userId)) {
                        otherUser = chatRoomInfo.getUser2Id();
                    } else {
                        otherUser = chatRoomInfo.getUser1Id();
                    }

                    ChatUser otherUserInfo = chatUserRepository.findByUserId(otherUser)
                            .orElseThrow(() -> new ChatException(INVALID_CHAT_USER_ID_EXCEPTION));

                    return ChatRoomListDto.of(
                            chatRoomInfo,
                            otherUserInfo
                    );
                })
                .collect(Collectors.toList());


        return chatRoomListInfo;
    }

    private List<ChatMessage> getPagedChatMessages(String roomToken, ObjectId cursorId, int limit) {
        PageRequest pageRequest = PageRequest.of(
                0,
                limit + 1,
                Sort.by(Sort.Direction.DESC, MESSAGE_SORT_FIELD_CREATED_AT)
        );

        if (cursorId == null) {
            return chatMessageRepository.findByRoomToken(roomToken, pageRequest).getContent();
        }
        return chatMessageRepository.findByRoomTokenAndIdLessThan(roomToken, cursorId, pageRequest).getContent();
    }

    private boolean checkHasNextPage(List<ChatMessage> chatMessages, int limit) {
        return chatMessages.size() > limit;
    }

    private List<ChatMessage> getChatMessagesToLimit(List<ChatMessage> chatMessages, boolean hasNext, int limit) {
        if (hasNext) {
            return chatMessages.subList(0, limit);
        }
        return chatMessages;
    }

    private Map<Long, ChatUser> getUserInfoForMessages(List<ChatMessage> chatMessages) {
        Set<Long> userIdSet = chatMessages.stream()
                .map(ChatMessage::getSenderId)
                .collect(Collectors.toSet());
        List<ChatUser> chatUserList = chatUserRepository.findAllByUserIdIn(userIdSet);
        return chatUserList.stream()
                .collect(Collectors.toMap(ChatUser::getUserId, Function.identity()));
    }

    private ChatMessageResponseDto mapMessagesToDto(
            List<ChatMessage> chatMessages,
            Map<Long, ChatUser> userInfos,
            boolean hasNext
    ) {
        List<ChatMessageInfoDto> chatMessageInfoDtoList = chatMessages.stream()
                .map(chatMessage -> {
                    ChatUser chatUser = userInfos.get(chatMessage.getSenderId());
                    return ChatMessageInfoDto.of(chatMessage, chatUser);
                })
                .sorted(Comparator.comparing(ChatMessageInfoDto::createdAt).reversed())
                .toList();
        return ChatMessageResponseDto.of(chatMessageInfoDtoList, hasNext);
    }
}
