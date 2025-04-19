package com.example.mate.upload.application;

import com.example.mate.chat.application.ChatMessageService;
import com.example.mate.chat.application.dto.ChatMessageDto;
import com.example.mate.chat.application.dto.ChatMessageInfoDto;
import com.example.mate.chat.infrastructure.stomp.StompMessagePublisher;
import com.example.mate.upload.application.dto.UploadImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final ImageClient imageClient;
    private final ChatMessageService chatMessageService;
    private final StompMessagePublisher stompMessagePublisher;

    public UploadImageResponseDto upload(MultipartFile file) {
        String objectKey = UUID.randomUUID().toString();
        String imageUrl = imageClient.upload(objectKey, file);
        return UploadImageResponseDto.of(imageUrl);
    }

    public void uploadFile(MultipartFile file, Long userId, ChatMessageDto messageDto) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String fileKey = "chat-files/" + fileName;
        int cacheSeconds = 60 * 60 * 24 * 7; // 7일 캐시

        ChatMessageDto fileMessageDto = ChatMessageDto.of(
                imageClient.uploadFile(fileKey, file, cacheSeconds),
                messageDto.type(),
                messageDto.roomToken()
        );


        ChatMessageInfoDto messageInfoDto = chatMessageService.createAndSaveMessage(userId, fileMessageDto);
        stompMessagePublisher.execute(messageInfoDto);
    }
}
