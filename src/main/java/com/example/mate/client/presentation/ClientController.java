package com.example.mate.client.presentation;

import com.example.mate.client.application.ClientService;
import com.example.mate.client.application.dto.ClientCreateRequestDto;
import com.example.mate.client.application.dto.ClientIdResponseDto;
import com.example.mate.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClientIdResponseDto>> createClient(
            @AuthenticationPrincipal Long userId,
            @RequestBody ClientCreateRequestDto request
    ) {
        ClientIdResponseDto clientIdResponse = clientService.createClient(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(clientIdResponse));
    }
}
