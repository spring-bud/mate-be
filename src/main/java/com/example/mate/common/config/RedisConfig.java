package com.example.mate.common.config;

import com.example.mate.chat.infrastructure.redis.RedisChatMessageListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private static final String CHATTING_CHANNEL_TOPIC = "chatting:channel";

    private final RedisChatMessageListener chatMessageListener;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        chatSubscribe(container);
        return container;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            ObjectMapper objectMapper,
            RedisConnectionFactory redisConnectionFactory
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));

        return template;
    }

    private void chatSubscribe(RedisMessageListenerContainer container) {
        container.addMessageListener(
                chatMessageListener,
                new ChannelTopic(CHATTING_CHANNEL_TOPIC)
        );
    }
}
