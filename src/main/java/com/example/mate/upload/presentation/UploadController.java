package com.example.mate.upload.presentation;

import com.example.mate.common.response.ApiResponse;
import com.example.mate.upload.application.UploadService;
import com.example.mate.upload.application.dto.UploadImageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/images")
    public ResponseEntity<ApiResponse<UploadImageResponseDto>> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                uploadService.upload(file)
        ));
    }
}
