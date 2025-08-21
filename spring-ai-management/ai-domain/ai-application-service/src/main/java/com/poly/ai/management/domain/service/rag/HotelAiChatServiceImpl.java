package com.poly.ai.management.domain.service.rag;

import com.poly.ai.management.domain.dto.AIResponse;
import com.poly.ai.management.domain.entity.ChatSession;
import com.poly.ai.management.domain.port.input.service.HotelAiChatService;
import com.poly.ai.management.domain.port.output.repository.ChatSessionRepository;
import com.poly.ai.management.domain.valueobject.SessionID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelAiChatServiceImpl implements HotelAiChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final ChatSessionRepository chatSessionRepository; // Inject interface

    @Override
    public AIResponse askLlama3WithRAG(String sessionId, String userQuery) {
        log.info("Nhận truy vấn người dùng cho phiên {}: {}", sessionId, userQuery);

        // 1. Lấy lịch sử hội thoại hiện tại từ Redis hoặc khởi tạo mới nếu không tìm thấy
        Optional<ChatSession> optionalChatSession = chatSessionRepository.findById(sessionId);
        ChatSession chatSession = optionalChatSession.orElseGet(() -> {
            log.info("Tạo phiên hội thoại mới cho session ID: {}", sessionId);
            return ChatSession.initialize(new SessionID(sessionId));
        });

        // 2. Thêm câu hỏi hiện tại của người dùng vào lịch sử trong đối tượng ChatSession
        chatSession.addUserMessage(userQuery);

        // Lưu ý: Chúng ta chưa thêm currentUserMessage vào chatSession.messages ngay.
        // Nó sẽ được thêm vào sau khi tạo Prompt.

        // 3. Thực hiện Retrieval-Augmented Generation (RAG)
        // Tìm kiếm các tài liệu liên quan trong Vector Store dựa trên câu hỏi của người dùng
        List<Document> relevantDocuments = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuery)
                        .topK(5) // Lấy 5 tài liệu (chunks) gần nhất về mặt ngữ nghĩa
                        .build()
        );

        if (relevantDocuments != null) {
            log.info("Đã truy xuất {} tài liệu liên quan từ Vector Store.", relevantDocuments.size());
        }

        // Trích xuất nội dung từ các tài liệu liên quan để tạo ngữ cảnh
        String context = relevantDocuments.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));
        // Phân tách các document rõ ràng

        log.debug("Ngữ cảnh được tạo: \n{}", context);

        // 4. Chuyển đổi DomainMessage sang Spring AI Message để gửi đến LLM
        List<Message> messagesForLlm = chatSession.getMessages().stream()
                .map(dm -> { // Chuyển đổi DomainMessage sang Spring AI Message
                    return switch (dm.getType()) {
                        case SYSTEM -> new SystemMessage(dm.getContent());
                        case USER -> new UserMessage(dm.getContent());
                        case ASSISTANT -> new AssistantMessage(dm.getContent());
                    };
                })
                .collect(Collectors.toList());
        // Tạo một UserMessage mới kết hợp câu hỏi của người dùng với ngữ cảnh RAG.
        // Đặt ngữ cảnh RAG trực tiếp vào UserMessage để LLM có thể dễ dàng tham chiếu.
        String ragAugmentedUserQuery = String.format("""
                Ngữ cảnh bổ sung từ cơ sở dữ liệu khách sạn:
                %s
                
                Câu hỏi của tôi: %s
                """, context, userQuery);

        // Thay thế UserMessage cuối cùng (userQuery gốc) bằng UserMessage đã augmented RAG
        // hoặc thêm vào cuối nếu muốn giữ cả 2 (tùy thuộc vào cách bạn muốn prompt)
        messagesForLlm.add(new UserMessage(ragAugmentedUserQuery));

        // 5. Tạo Prompt từ danh sách Messages và gửi đến LLM
        Prompt prompt = new Prompt(messagesForLlm);

        log.debug("Prompt cuối cùng gửi đến LLM: \n{}", prompt.getContents());

        // Gửi Prompt đến Llama3 và nhận phản hồi
        String responseContent = Objects.requireNonNull(
                chatClient.prompt(prompt)
                        .call()
                        .content());

        // 6. Thêm phản hồi của bot vào lịch sử hội thoại trong đối tượng ChatSession
        chatSession.addAssistantMessage(responseContent);

        // 7. Lưu ChatSession đã cập nhật vào Redis
        chatSessionRepository.save(chatSession);

        log.info("Đã lưu lịch sử hội thoại cho phiên {} vào Redis.", sessionId);

        log.info("Phản hồi từ Llama3: {}", responseContent);
        AIResponse response = new AIResponse();
        response.setSessionId(sessionId);
        response.setValue(responseContent);
        return response;
    }

    public void clearChatHistory(String sessionId) {
        if (chatSessionRepository.existsById(sessionId)) {
            chatSessionRepository.deleteById(sessionId);
            log.info("Đã xóa lịch sử hội thoại cho phiên {} từ Redis.", sessionId);
        } else {
            log.warn("Không tìm thấy lịch sử hội thoại để xóa cho phiên: {}", sessionId);
        }
    }
}
