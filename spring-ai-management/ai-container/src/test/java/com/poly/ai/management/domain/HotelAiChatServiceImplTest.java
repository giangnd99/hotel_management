package com.poly.ai.management.domain.service.rag;

import com.poly.ai.chat.client.ChatClient;
import com.poly.ai.chat.messages.AssistantMessage;
import com.poly.ai.chat.messages.Message;
import com.poly.ai.document.Document;
import com.poly.ai.management.domain.entity.ChatSession;
import com.poly.ai.management.domain.port.output.repository.ChatSessionRepository;
import com.poly.ai.vectorstore.SearchRequest;
import com.poly.ai.vectorstore.VectorStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class HotelAiChatServiceImplTest {

    @Autowired
    private HotelAiChatServiceImpl hotelAiChatService;

    @MockBean
    private ChatClient chatClient;

    @MockBean
    private VectorStore vectorStore;

    @MockBean
    private ChatSessionRepository chatSessionRepository;

    @Test
    void testAskLlama3WithRAG_NewSession() {
        String sessionId = "test-session-id";
        String userQuery = "What are the top hotels in New York?";
        ChatSession newChatSession = new ChatSession(sessionId);

        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of(new Document("Hotel 1 details"), new Document("Hotel 2 details")));
        Message expectedResponseMessage = new AssistantMessage("Here are the top hotels in New York: Hotel 1, Hotel 2.");
        when(chatClient.prompt(any())).thenReturn(() -> () -> () -> () -> expectedResponseMessage);
        when(chatSessionRepository.save(any())).thenReturn(newChatSession);

        String response = hotelAiChatService.askLlama3WithRAG(sessionId, userQuery);

        assertEquals("Here are the top hotels in New York: Hotel 1, Hotel 2.", response);

        verify(chatSessionRepository, times(1)).save(newChatSession);
        verify(vectorStore, times(1)).similaritySearch(any(SearchRequest.class));
        verify(chatClient, times(1)).prompt(any());
    }

    @Test
    void testAskLlama3WithRAG_ExistingSession() {
        String sessionId = "test-session-id";
        String userQuery = "Tell me about room availability.";
        ChatSession existingChatSession = new ChatSession(sessionId);
        existingChatSession.addUserMessage("Previous question");

        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.of(existingChatSession));
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of(new Document("Room details")));
        Message expectedResponseMessage = new AssistantMessage("Rooms are available. Here are the details.");
        when(chatClient.prompt(any())).thenReturn(() -> () -> () -> () -> expectedResponseMessage);
        when(chatSessionRepository.save(any())).thenReturn(existingChatSession);

        String response = hotelAiChatService.askLlama3WithRAG(sessionId, userQuery);

        assertEquals("Rooms are available. Here are the details.", response);

        verify(chatSessionRepository, times(1)).save(existingChatSession);
        verify(vectorStore, times(1)).similaritySearch(any(SearchRequest.class));
        verify(chatClient, times(1)).prompt(any());
    }

    @Test
    void testAskLlama3WithRAG_NoRelevantDocuments() {
        String sessionId = "test-session-id";
        String userQuery = "What are the best amenities available?";

        ChatSession newChatSession = new ChatSession(sessionId);

        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of());
        Message expectedResponseMessage = new AssistantMessage("Sorry, I couldn’t find any details.");
        when(chatClient.prompt(any())).thenReturn(() -> () -> () -> () -> expectedResponseMessage);
        when(chatSessionRepository.save(any())).thenReturn(newChatSession);

        String response = hotelAiChatService.askLlama3WithRAG(sessionId, userQuery);

        assertEquals("Sorry, I couldn’t find any details.", response);

        verify(chatSessionRepository, times(1)).save(newChatSession);
        verify(vectorStore, times(1)).similaritySearch(any(SearchRequest.class));
        verify(chatClient, times(1)).prompt(any());
    }

    @Test
    void testAskLlama3WithRAG_ExceptionInChatClient() {
        String sessionId = "test-session-id";
        String userQuery = "What are the top resorts in Hawaii?";
        ChatSession newChatSession = new ChatSession(sessionId);

        when(chatSessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of(new Document("Resort 1 details")));
        when(chatClient.prompt(any())).thenThrow(new RuntimeException("Chat client error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                hotelAiChatService.askLlama3WithRAG(sessionId, userQuery));

        assertEquals("Chat client error", exception.getMessage());

        verify(chatSessionRepository, times(0)).save(any());
        verify(vectorStore, times(1)).similaritySearch(any(SearchRequest.class));
        verify(chatClient, times(1)).prompt(any());
    }
}