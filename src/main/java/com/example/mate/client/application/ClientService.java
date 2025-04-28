package com.example.mate.client.application;

import com.example.mate.client.application.dto.ClientCreateRequestDto;
import com.example.mate.client.application.dto.ClientIdResponseDto;
import com.example.mate.client.domain.Client;
import com.example.mate.client.domain.Ctag;
import com.example.mate.client.domain.repository.ClientRepository;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final UserService userService;
    private final CtagService ctagService;
    private final ClientRepository clientRepository;

    @Transactional
    public ClientIdResponseDto createClient(Long userId, ClientCreateRequestDto request) {

        User findUser = userService.getUserById(userId);

        Client newClient = Client.builder()
                .user(findUser)
                .title(request.title())
                .category(request.category())
                .content(request.content())
                .thumbnailUrl(request.thumbnailUrl())
                .build();

        List<Ctag> ctags = ctagService.findOrCreateTags(request.ctags());
        ctags.forEach(newClient::addTag);

        Client savedClient = clientRepository.save(newClient);

        return new ClientIdResponseDto(savedClient.getId());
    }
}
