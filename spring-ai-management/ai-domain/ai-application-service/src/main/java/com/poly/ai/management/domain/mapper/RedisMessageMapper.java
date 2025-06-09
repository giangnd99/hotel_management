package com.poly.ai.management.domain.mapper;

import com.poly.ai.management.domain.dto.RedisMessageDto;
import com.poly.ai.management.domain.entity.DomainMessage;
import com.poly.ai.management.domain.exception.AiDomainException;
import org.springframework.ai.chat.messages.*;

public class RedisMessageMapper {

    public static RedisMessageDto fromDomainMessage(DomainMessage domainMessage) {
        return new RedisMessageDto(domainMessage.getType().name(),
                domainMessage.getContent());
    }

    public static DomainMessage toDomainMessage(RedisMessageDto redisMessageDto) {
        return new DomainMessage(DomainMessage.Type.valueOf(redisMessageDto.getType()),
                redisMessageDto.getContent());
    }

    public static RedisMessageDto fromSpringAIMessage(Message springAiMessage) {
        return new RedisMessageDto(springAiMessage.getMessageType().name(),
                springAiMessage.getContent());
    }

    public static Message toSpringAIMessage(RedisMessageDto redisMessageDto) {
        MessageType messageType = MessageType.valueOf(redisMessageDto.getType());
        return switch (messageType) {
            case USER -> new UserMessage(redisMessageDto.getContent());
            case ASSISTANT -> new AssistantMessage(redisMessageDto.getContent());
            case SYSTEM -> new SystemMessage(redisMessageDto.getContent());
            default -> throw new AiDomainException("Unexpected value: " + messageType);
        };
    }
}
