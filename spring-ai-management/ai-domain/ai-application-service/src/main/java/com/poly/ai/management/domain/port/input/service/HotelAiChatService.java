package com.poly.ai.management.domain.port.input.service;

import com.poly.ai.management.domain.dto.AIResponse;

public interface HotelAiChatService {
    AIResponse askLlama3WithRAG(String sessionId, String userQuery);

    void clearChatHistory(String sessionId);
}
