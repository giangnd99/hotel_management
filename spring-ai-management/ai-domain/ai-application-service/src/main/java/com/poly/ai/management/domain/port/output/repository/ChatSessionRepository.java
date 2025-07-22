package com.poly.ai.management.domain.port.output.repository;

import com.poly.ai.management.domain.entity.ChatSession;

import java.util.Optional;

public interface ChatSessionRepository {
    Optional<ChatSession> findById(String sessionId);

    ChatSession save(ChatSession chatSession);

    void deleteById(String sessionId);

    boolean existsById(String sessionId);
}
