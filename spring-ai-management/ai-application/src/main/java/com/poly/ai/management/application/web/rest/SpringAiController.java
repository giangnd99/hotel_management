package com.poly.ai.management.application.web.rest;

import com.poly.ai.management.domain.dto.AIResponse;
import com.poly.ai.management.domain.port.input.service.HotelAiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class SpringAiController {

    private final HotelAiChatService hotelChatService;

    @GetMapping("/ask")
    public AIResponse ask(@RequestParam(value = "query") String query,
                          @RequestParam(value = "sessionId", required = false) String sessionId) {
        String currentSessionId = (sessionId != null && !sessionId.isEmpty()) ? sessionId : UUID.randomUUID().toString();
        return hotelChatService.askLlama3WithRAG(currentSessionId, query);
    }

    @PostMapping("/clear-history")
    public String clearHistory(@RequestParam("sessionId") String sessionId) {
        hotelChatService.clearChatHistory(sessionId);
        return "Lịch sử hội thoại cho phiên '" + sessionId + "' đã được xóa.";
    }
}
