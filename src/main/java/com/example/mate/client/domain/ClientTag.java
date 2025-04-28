package com.example.mate.client.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "clients_tags", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"client_id", "ctag_id"})
})
public class ClientTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Ctag ctag;

    public ClientTag(Client client, Ctag ctag) {
        this.client = client;
        this.ctag = ctag;
    }
}
