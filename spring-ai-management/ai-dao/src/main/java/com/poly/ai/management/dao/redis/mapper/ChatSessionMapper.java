package com.poly.ai.management.dao.redis.mapper;

import com.poly.ai.management.dao.redis.model.RedisChatSession;
import com.poly.ai.management.domain.dto.RedisMessageDto;
import com.poly.ai.management.domain.entity.ChatSession;
import com.poly.ai.management.domain.mapper.RedisMessageMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ChatSessionMapper {
    // Chuyển đổi từ Domain ChatSession sang RedisChatSession
    public static RedisChatSession fromDomainChatSession(ChatSession domainChatSession) {
        List<RedisMessageDto> redisMessages = domainChatSession.getMessages().stream()
                .map(RedisMessageMapper::fromDomainMessage)
                .collect(Collectors.toList());
        return new RedisChatSession(domainChatSession.getId().getValue(), redisMessages);
    }

    // Chuyển đổi từ RedisChatSession sang Domain ChatSession
    public static ChatSession toDomainChatSession(RedisChatSession redisChatSession) {
        ChatSession domainChatSession = new ChatSession(redisChatSession.getId()); // Sử dụng constructor có ID
        domainChatSession.setMessages(redisChatSession.getMessages().stream()
                .map(RedisMessageMapper::toDomainMessage)
                .collect(Collectors.toList()));
        return domainChatSession;
    }
}
