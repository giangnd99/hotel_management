package com.poly.ai.management.dao.redis.adapter;

import com.poly.ai.management.dao.redis.mapper.ChatSessionMapper;
import com.poly.ai.management.dao.redis.repository.SpringDataRedisChatSessionRepository;
import com.poly.ai.management.domain.entity.ChatSession;
import com.poly.ai.management.domain.port.output.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatSessionRedisRepositoryAdapter implements ChatSessionRepository {

    private final SpringDataRedisChatSessionRepository repository;

    @Override
    public Optional<ChatSession> findById(String sessionId) {
        return repository.findById(sessionId).map(ChatSessionMapper::toDomainChatSession);
    }

    @Override
    public ChatSession save(ChatSession chatSession) {
        return ChatSessionMapper.toDomainChatSession(
                repository.save(ChatSessionMapper.fromDomainChatSession(chatSession)));
    }

    @Override
    public void deleteById(String sessionId) {
        repository.deleteById(sessionId);
    }

    @Override
    public boolean existsById(String sessionId) {
        return repository.existsById(sessionId);
    }
}
