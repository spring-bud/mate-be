package com.example.mate.chat.domain;

import com.example.mate.common.domain.BaseTimeEntity;
import com.example.mate.product.domain.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_rooms")
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "room_token", nullable = false, unique = true)
    private String roomToken;

    @Column(name = "user1_id")
    private Long user1Id;

    @Column(name = "user2_id")
    private Long user2Id;

    @Column(name = "user1_active")
    private boolean user1Active;

    @Column(name = "user2_active")
    private boolean user2Active;

    public ChatRoom(Product product, Long custromerId) {
        this.product = product;
        this.roomToken = UUID.randomUUID().toString();
        this.user1Id = product.getUser().getId();
        this.user2Id = custromerId;
        this.user1Active = true;
        this.user2Active = true;
    }
}
