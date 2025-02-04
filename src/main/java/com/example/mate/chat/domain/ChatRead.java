package com.example.mate.chat.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Document(collection = "chat_read")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRead {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private ObjectId id;

    @Indexed
    @Field("room_token")
    private String roomToken;

    @Field(value = "message_id")
    private ObjectId messageId;

    @Field("sender_id")
    private Long senderId;

    @Builder
    public ChatRead(String roomToken, ObjectId messageId, Long senderId) {
        this.roomToken = roomToken;
        this.messageId = messageId;
        this.senderId = senderId;
    }
}
