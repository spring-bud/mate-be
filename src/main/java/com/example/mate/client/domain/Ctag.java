package com.example.mate.client.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ctags")
public class Ctag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ctag_id")
    private Long id;

    @Column(name = "cname", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "ctag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientTag> clientTags = new ArrayList<>();

    public Ctag(String name) {
        this.name = name;
    }
}

