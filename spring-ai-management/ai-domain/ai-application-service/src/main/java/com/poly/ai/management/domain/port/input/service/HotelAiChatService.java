package com.poly.ai.management.domain.port.input.service;

public interface HotelAiChatService {
    String askLlama3WithRAG(String sessionId, String userQuery);

    void clearChatHistory(String sessionId);
}
