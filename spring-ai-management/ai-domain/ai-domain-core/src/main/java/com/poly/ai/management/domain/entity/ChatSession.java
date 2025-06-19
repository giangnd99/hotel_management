package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.SessionID;
import com.poly.domain.entity.AggregateRoot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChatSession extends AggregateRoot<SessionID> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<DomainMessage> messages; // Sử dụng DomainMessage thuần túy

    // System Message ban đầu dưới dạng chuỗi thuần túy
    private static final String INITIAL_SYSTEM_PROMPT = """
            Bạn là một trợ lý ảo của khách sạn, chuyên trả lời các câu hỏi về phòng ốc và dịch vụ khách sạn.
            Hãy sử dụng thông tin từ các tài liệu được cung cấp và lịch sử hội thoại để trả lời câu hỏi của người dùng.
            Nếu thông tin không có trong ngữ cảnh hoặc lịch sử hội thoại, hãy nói rằng bạn không biết và đề xuất người dùng liên hệ trực tiếp.
            Trả lời một cách lịch sự, đầy đủ và chuyên nghiệp.
            """;

    /**
     * Constructor mặc định.
     */
    public ChatSession() {
        this.messages = new ArrayList<>();
    }

    /**
     * Constructor để tạo một ChatSession mới với ID phiên và System Message ban đầu.
     *
     * @param sessionId ID duy nhất của phiên hội thoại.
     */
    public ChatSession(String sessionId) {
        this.messages = new ArrayList<>();
        // Thêm System Message ban đầu vào lịch sử
        this.messages.add(new DomainMessage(DomainMessage.Type.SYSTEM, INITIAL_SYSTEM_PROMPT));
    }

    /**
     * Lấy danh sách các tin nhắn trong phiên.
     * Trả về một bản sao không thể sửa đổi để bảo vệ tính toàn vẹn của lịch sử.
     *
     * @return Danh sách các DomainMessage không thể sửa đổi.
     */
    public List<DomainMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * Setter cho danh sách tin nhắn. Dành cho các cơ chế tái tạo đối tượng (ví dụ: deserialization).
     *
     * @param messages Danh sách DomainMessage để thiết lập.
     */
    public void setMessages(List<DomainMessage> messages) {
        this.messages = new ArrayList<>(messages);
    }

    /**
     * Thêm một tin nhắn từ người dùng vào lịch sử hội thoại.
     *
     * @param userMessage Đối tượng DomainMessage (type USER).
     */
    public void addUserMessage(String userMessageContent) {
        Objects.requireNonNull(userMessageContent, "User message content must not be null");
        this.messages.add(new DomainMessage(DomainMessage.Type.USER, userMessageContent));
    }

    /**
     * Thêm một tin nhắn từ trợ lý (bot) vào lịch sử hội thoại.
     *
     * @param assistantMessage Đối tượng DomainMessage (type ASSISTANT).
     */
    public void addAssistantMessage(String assistantMessageContent) {
        Objects.requireNonNull(assistantMessageContent, "Assistant message content must not be null");
        this.messages.add(new DomainMessage(DomainMessage.Type.ASSISTANT, assistantMessageContent));
    }

    /**
     * Lấy System Prompt ban đầu.
     *
     * @return Chuỗi System Prompt cố định.
     */
    public String getInitialSystemPrompt() {
        return INITIAL_SYSTEM_PROMPT;
    }
}
