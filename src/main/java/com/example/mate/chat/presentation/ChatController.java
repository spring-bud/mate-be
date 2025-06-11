package com.example.mate.chat.presentation;

import com.example.mate.auth.infrastructure.jwt.JwtExtractor;
import com.example.mate.chat.application.ChatMessageService;
import com.example.mate.chat.application.ChatService;
import com.example.mate.chat.application.dto.ChatMessageDto;
import com.example.mate.chat.application.dto.ChatMessageResponseDto;
import com.example.mate.chat.application.dto.ChatRoomListDto;
import com.example.mate.chat.application.dto.ChatRoomTokenDto;
import com.example.mate.common.response.ApiResponse;
import com.example.mate.product.application.ProductService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ProductService productService;
    private final ChatService chatService;
    private final ChatMessageService chatMessageService;
    private final JwtExtractor jwtExtractor;

    @PostMapping("/greet")
    public ResponseEntity<Void> enterLeaveMessage(
            @AuthenticationPrincipal Long userId,
            @RequestBody ChatMessageDto messageDto
    ) {
        chatMessageService.sendEnterLeaveMessage(userId, messageDto);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/create/room/{productId}")
    public ResponseEntity<ApiResponse<ChatRoomTokenDto>> createRoom(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                        chatService.createChatRoom(productId, userId)
                )
        );
    }

    @MessageMapping("/chat")
    public ResponseEntity<Void> sendMessage(
            @RequestBody ChatMessageDto messageDto,
            @Header("Authorization") String token
    ) {

        Claims parser = jwtExtractor.parseClaim(token.replace("Bearer ", ""));
        Long userId = parser.get("user_id", Long.class);

        chatMessageService.sendChatMessage(userId, messageDto);
        return ResponseEntity.ok(null);
    }


    @GetMapping("/{roomToken}/messages")
    public ResponseEntity<ApiResponse<ChatMessageResponseDto>> getChatMessages(
            @PathVariable String roomToken,
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "cursorId", required = false) ObjectId cursorId,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                        chatService.getMessagesByRoomToken(roomToken, userId, cursorId, limit)
                )
        );
    }

    @GetMapping("/{roomToken}/messages/recent")
    public ResponseEntity<ApiResponse<ChatMessageResponseDto>> getRecentChatMessages(
            @PathVariable String roomToken
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                        chatService.getRecentMessagesByRoomToken(roomToken)
                )
        );
    }

    @GetMapping("/chatRoomList")
    public ResponseEntity<ApiResponse<List<ChatRoomListDto>>> getChatRoomList(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                        chatService.getChatRoomList(userId)
                )
        );
    }
}
