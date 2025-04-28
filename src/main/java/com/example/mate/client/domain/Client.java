package com.example.mate.client.domain;

import com.example.mate.common.domain.BaseTimeEntity;
import com.example.mate.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "clients")
public class Client extends BaseTimeEntity {

    @Id
    @Column(name = "client_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientTag> clientTags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ClientCategory category;

    @Builder
    public Client(User user, String thumbnailUrl, String title, String content, ClientCategory category) {
        this.user = user;
        this.category = category;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.content = content;
    }

    public void addTag(Ctag ctag) {
        ClientTag clientTag = new ClientTag(this, ctag);
        clientTags.add(clientTag);
    }

    public void removeTag(Ctag ctag) {
        clientTags.removeIf(clientTag -> clientTag.getCtag().equals(ctag));
    }

    public void syncTag(List<Ctag> newTag) {
        List<Ctag> currentTag = clientTags.stream()
                .map(ClientTag::getCtag)
                .toList();

        List<Ctag> toRemove = currentTag.stream()
                .filter(existing -> !newTag.contains(existing))
                .toList();

        List<Ctag> toAdd = newTag.stream()
                .filter(newStack -> !currentTag.contains(newStack))
                .toList();

        toRemove.forEach(this::removeTag);
        toAdd.forEach(this::addTag);
    }
}
