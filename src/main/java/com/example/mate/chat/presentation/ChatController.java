package com.example.mate.chat.presentation;

import com.example.mate.chat.application.ChatMessageService;
import com.example.mate.chat.application.ChatService;
import com.example.mate.chat.application.dto.ChatMessageDto;
import com.example.mate.chat.application.dto.ChatMessageResponseDto;
import com.example.mate.chat.application.dto.ChatRoomTokenDto;
import com.example.mate.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ProductService productService;
    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    @PostMapping("/create/room/{productId}")
    public ResponseEntity<ChatRoomTokenDto> createRoom(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId,
            @RequestBody ChatMessageDto messageDto
    ) {
        return ResponseEntity.ok(chatService.createChatRoom(productId, userId, messageDto));
    }

    @PostMapping("/chat")
    public ResponseEntity<Void> sendMessage(
            @AuthenticationPrincipal Long userId,
            @RequestBody ChatMessageDto messageDto
    ) {
        chatMessageService.sendChatMessage(userId, messageDto);
        return ResponseEntity.ok(null);
    }


    @GetMapping("/{roomToken}/messages")
    public ResponseEntity<ChatMessageResponseDto> getChatMessages(
            @PathVariable String roomToken,
            @RequestParam(value = "cursorId", required = false) ObjectId cursorId,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(chatService.getMessagesByRoomToken(roomToken, cursorId, limit));
    }

    @GetMapping("/{roomToken}/messages/recent")
    public ResponseEntity<ChatMessageResponseDto> getRecentChatMessages(
            @PathVariable String roomToken
    ) {
        return ResponseEntity.ok(chatService.getRecentMessagesByRoomToken(roomToken));
    }
}
