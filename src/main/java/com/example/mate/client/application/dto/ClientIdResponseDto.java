package com.example.mate.client.application.dto;

import com.example.mate.client.domain.Client;

public record ClientIdResponseDto(
        Long clientId
) {

    public static ClientIdResponseDto of(Client client) {
        return new ClientIdResponseDto(client.getId());
    }
}
