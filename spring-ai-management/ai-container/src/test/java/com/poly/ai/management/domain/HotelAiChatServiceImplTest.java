package com.poly.ai.management.domain;

import com.poly.ai.management.domain.entity.ChatSession;
import com.poly.ai.management.domain.port.output.repository.ChatSessionRepository;
import com.poly.ai.management.domain.service.rag.HotelAiChatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AiManagementApplication.class)
class HotelAiChatServiceImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private HotelAiChatServiceImpl hotelAiChatService;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    private String sessionId;

    @BeforeEach
    void setUp() {
        // Tạo một sessionId mới cho mỗi test để tránh xung đột
        sessionId = UUID.randomUUID().toString();
    }

    @Test
    void shouldHandleNewSessionWithHotelQuery() {
        String userQuery = "Cho tôi biết các tiện nghi của khách sạn?";

        String response = hotelAiChatService.askLlama3WithRAG(sessionId, userQuery);

        assertNotNull(response);
        assertFalse(response.isEmpty());

        // Kiểm tra xem session có được lưu không
        assertTrue(chatSessionRepository.findById(sessionId).isPresent());
    }

    @Test
    void shouldHandleExistingSessionWithFollowUpQuery() {
        // First query to create a session
        String firstQuery = "Khách sạn có bao nhiêu phòng?";
        hotelAiChatService.askLlama3WithRAG(sessionId, firstQuery);

        // Follow up query
        String followUpQuery = "Giá phòng như thế nào?";
        String response = hotelAiChatService.askLlama3WithRAG(sessionId, followUpQuery);

        assertNotNull(response);
        assertFalse(response.isEmpty());

        // Kiểm tra xem session có lưu cả 2 messages không
        ChatSession session = chatSessionRepository.findById(sessionId).orElseThrow();
        assertTrue(session.getMessages().size() >= 4); // 2 user messages + 2 assistant messages
    }

    @Test
    void shouldHandleComplexQuery() {
        String complexQuery = "Cho tôi biết chi tiết về các dịch vụ spa, nhà hàng và phòng gym của khách sạn, " +
                "bao gồm cả giờ hoạt động và chi phí?";

        String response = hotelAiChatService.askLlama3WithRAG(sessionId, complexQuery);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.length() > 100); // Expect detailed response
    }

    @Test
    void shouldHandleVietnameseQuery() {
        String vietnameseQuery = "Khách sạn có những loại phòng nào và giá cả ra sao?";

        String response = hotelAiChatService.askLlama3WithRAG(sessionId, vietnameseQuery);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        // Kiểm tra xem response có chứa ký tự tiếng Việt không
        assertTrue(response.matches(".*[àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ].*"));
    }

    @Test
    void shouldHandleMultipleQueriesInSameSession() {
        String[] queries = {
                "Khách sạn có bể bơi không?",
                "Bể bơi mở cửa mấy giờ?",
                "Có phục vụ đồ ăn ở bể bơi không?",
                "Giá vé vào bể bơi thế nào?"
        };

        for (String query : queries) {
            String response = hotelAiChatService.askLlama3WithRAG(sessionId, query);
            assertNotNull(response);
            assertFalse(response.isEmpty());
        }

        // Kiểm tra xem session có lưu đủ số lượng messages không
        ChatSession session = chatSessionRepository.findById(sessionId).orElseThrow();
        assertTrue(session.getMessages().size() >= queries.length * 2); // user + assistant messages
    }

    @Test
    void shouldHandleLongConversationHistory() {
        // Tạo một cuộc hội thoại dài
        for (int i = 0; i < 2; i++) {
            String query = "Câu hỏi số " + (i + 1) + " về dịch vụ khách sạn?";
            String response = hotelAiChatService.askLlama3WithRAG(sessionId, query);
            assertNotNull(response);
            assertFalse(response.isEmpty());
        }

        // Kiểm tra câu hỏi cuối vẫn hoạt động tốt
        String nameHotelQuery = "Tôi muốn biết tên khách sạn?";
        String responses = hotelAiChatService.askLlama3WithRAG(sessionId, nameHotelQuery);
        String finalQuery = "Tổng hợp lại các thông tin trên?";
        String response = hotelAiChatService.askLlama3WithRAG(sessionId, finalQuery);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.length() > 200); // Expect a comprehensive summary
    }

    @Test
    void shouldHandleFoundRoomConversationHistory() {

        // Kiểm tra câu hỏi cuối vẫn hoạt động tốt
        String nameHotelQuery = "Tôi muốn biết tất cả phòng của khách sạn đang có thể đặt ?";
        String response = hotelAiChatService.askLlama3WithRAG(sessionId, nameHotelQuery);

        String findRoomHotelQuery = "Tôi muốn biết phòng 201 có thể đặt phòng không ?";
        String response2 = hotelAiChatService.askLlama3WithRAG(sessionId, nameHotelQuery);
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertTrue(response.length() > 200); // Expect a comprehensive summary
    }
}