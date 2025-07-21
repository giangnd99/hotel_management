package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.ai.management.domain.valueobject.SessionID;
import com.poly.domain.entity.AggregateRoot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * ChatSession là một Aggregate Root trong Domain Driven Design,
 * đại diện cho một phiên hội thoại duy nhất với trợ lý AI.
 * Nó quản lý lịch sử tin nhắn và đảm bảo tính toàn vẹn của phiên.
 */
public class ChatSession extends AggregateRoot<SessionID> implements Serializable {

    private static final long serialVersionUID = 1L;

    // Danh sách các tin nhắn trong phiên, được quản lý chặt chẽ bởi Aggregate Root.
    private List<DomainMessage> messages;

    // System Message ban đầu dưới dạng chuỗi thuần túy.
    // Đây là một quy tắc nghiệp vụ cốt lõi của phiên chat.
    private static final String INITIAL_SYSTEM_PROMPT = """
            Bạn là một trợ lý ảo của khách sạn Nikka, chuyên trả lời các câu hỏi về phòng ốc và dịch vụ khách sạn.
            Hãy sử dụng thông tin từ các tài liệu được cung cấp và lịch sử hội thoại để trả lời câu hỏi của người dùng.
            Nếu thông tin không có trong ngữ cảnh hoặc lịch sử hội thoại, hãy nói rằng bạn không biết và đề xuất người dùng liên hệ trực tiếp.
             **QUAN TRỌNG:** Luôn luôn trả lời một cách lịch sự, đầy đủ, chuyên nghiệp, và **luôn sử dụng TIẾNG VIỆT có dấu**.
            """;

    // Private constructor để bắt buộc sử dụng Builder Pattern
    // Hoặc cho các cơ chế tái tạo đối tượng (deserialization)
    private ChatSession() {
        this.messages = new ArrayList<>();
    }

    /**
     * Constructor được sử dụng bởi Builder để tạo một đối tượng ChatSession hoàn chỉnh.
     * Validate các thuộc tính cơ bản ngay tại thời điểm tạo.
     *
     * @param builder Builder chứa các thuộc tính để xây dựng ChatSession.
     */
    private ChatSession(Builder builder) {
        // Gán ID từ builder.
        // Aggregate Root ID luôn phải được thiết lập khi tạo.
        setId(builder.id);

        // Khởi tạo danh sách tin nhắn từ builder hoặc tạo mới.
        // messages list không được null
        this.messages = builder.messages != null ? new ArrayList<>(builder.messages) : new ArrayList<>();

        // Nếu ID là null sau khi set từ builder (chỉ xảy ra nếu builder.id là null),
        // thì đây là lỗi nghiệp vụ nghiêm trọng cho Aggregate Root.
        Objects.requireNonNull(getId(), "Session ID must not be null for a new ChatSession.");

        // Thêm system message ban đầu nếu chưa có và nếu đây là phiên mới khởi tạo.
        // Logic này có thể phức tạp hơn tùy thuộc vào cách bạn quản lý lịch sử.
        // Ở đây, tôi giả định rằng khi tạo mới (tức là messages ban đầu có thể rỗng),
        // thì System Prompt sẽ được thêm vào.
        if (this.messages.isEmpty() || !containsSystemMessage(this.messages)) {
            this.messages.add(0, new DomainMessage(DomainMessage.Type.SYSTEM, INITIAL_SYSTEM_PROMPT));
        }

        // Thực hiện validate sau khi tất cả các thuộc tính đã được thiết lập.
        validateChatSession();
    }

    /**
     * Phương thức nội bộ để kiểm tra xem danh sách tin nhắn có chứa System Message không.
     *
     * @param messages Danh sách tin nhắn.
     * @return true nếu tìm thấy System Message, ngược lại false.
     */
    private boolean containsSystemMessage(List<DomainMessage> messages) {
        return messages.stream()
                .anyMatch(msg -> DomainMessage.Type.SYSTEM.equals(msg.getType()));
    }

    /**
     * Phương thức khởi tạo aggregate root.
     * Thường được gọi trong domain service khi tạo một phiên chat mới.
     *
     * @param sessionId ID của phiên chat.
     * @return Một đối tượng ChatSession đã được khởi tạo.
     */
    public static ChatSession initialize(SessionID sessionId) {
        return ChatSession.Builder.builder()
                .id(sessionId)
                .messages(new ArrayList<>()) // Khởi tạo rỗng, constructor sẽ thêm system prompt
                .build();
    }

    /**
     * Thêm một tin nhắn từ người dùng vào lịch sử hội thoại.
     *
     * @param userMessageContent Nội dung tin nhắn người dùng.
     * @throws AiDomainException nếu nội dung tin nhắn null hoặc rỗng.
     */
    public void addUserMessage(String userMessageContent) {
        if (userMessageContent == null || userMessageContent.trim().isEmpty()) {
            throw new AiDomainException("User message content cannot be null or empty.");
        }
        this.messages.add(new DomainMessage(DomainMessage.Type.USER, userMessageContent));
    }

    /**
     * Thêm một tin nhắn từ trợ lý (bot) vào lịch sử hội thoại.
     *
     * @param assistantMessageContent Nội dung tin nhắn trợ lý.
     * @throws AiDomainException nếu nội dung tin nhắn null hoặc rỗng.
     */
    public void addAssistantMessage(String assistantMessageContent) {
        if (assistantMessageContent == null || assistantMessageContent.trim().isEmpty()) {
            throw new AiDomainException("Assistant message content cannot be null or empty.");
        }
        this.messages.add(new DomainMessage(DomainMessage.Type.ASSISTANT, assistantMessageContent));
    }

    /**
     * Lấy danh sách các tin nhắn trong phiên.
     * Trả về một bản sao không thể sửa đổi để bảo vệ tính toàn vẹn của lịch sử Aggregate.
     *
     * @return Danh sách các DomainMessage không thể sửa đổi.
     */
    public List<DomainMessage> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * Setter cho danh sách tin nhắn.
     * Phương thức này chủ yếu dành cho các cơ chế tái tạo đối tượng (ví dụ: deserialization từ DB/Cache).
     * Không nên sử dụng để thay đổi lịch sử thông thường; thay vào đó, hãy dùng add*Message().
     *
     * @param messages Danh sách DomainMessage để thiết lập.
     */
    public void setMessages(List<DomainMessage> messages) {
        Objects.requireNonNull(messages, "Messages list cannot be null during deserialization.");
        this.messages = new ArrayList<>(messages);
        // Sau khi set messages, cần validate lại nếu đây là một hoạt động tạo/tái tạo Aggregate Root
        validateChatSession();
    }

    /**
     * Phương thức validate tổng thể cho entity ChatSession.
     * Đảm bảo tính nhất quán của Aggregate Root.
     *
     * @throws AiDomainException Nếu dữ liệu không hợp lệ.
     */
    public void validateChatSession() {
        // ID của Aggregate Root không được null.
        if (getId() == null || getId().getValue() == null || getId().getValue().trim().isEmpty()) {
            throw new AiDomainException("ChatSession ID must be valid and non-empty.");
        }
        // Lịch sử tin nhắn không được null (có thể rỗng nếu mới tạo, nhưng không null).
        if (messages == null) {
            throw new AiDomainException("Messages list cannot be null.");
        }
        // Đảm bảo System Message luôn có mặt (là tin nhắn đầu tiên) sau khi khởi tạo.
        // Có thể cần logic phức tạp hơn nếu bạn cho phép xóa tin nhắn hoặc thêm system prompt động.
        if (messages.isEmpty() || messages.get(0).getType() != DomainMessage.Type.SYSTEM) {
            throw new AiDomainException("ChatSession must always start with a SYSTEM message.");
        }
        // Kiểm tra nội dung của các tin nhắn không được null/rỗng
        for (DomainMessage message : messages) {
            if (message.getContent() == null || message.getContent().trim().isEmpty()) {
                throw new AiDomainException("Message content cannot be null or empty.");
            }
        }
    }

    /**
     * Lấy System Prompt ban đầu.
     *
     * @return Chuỗi System Prompt cố định.
     */
    public String getInitialSystemPrompt() {
        return INITIAL_SYSTEM_PROMPT;
    }

    /**
     * Builder Pattern cho ChatSession.
     * Cung cấp một cách linh hoạt và an toàn để xây dựng các đối tượng ChatSession.
     */
    public static final class Builder {
        private SessionID id;
        private List<DomainMessage> messages;

        private Builder() {
            // Khởi tạo messages để tránh NullPointerException khi build nếu không được set.
            this.messages = new ArrayList<>();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(SessionID val) {
            id = val;
            return this;
        }

        public Builder messages(List<DomainMessage> val) {
            // Tạo bản sao để tránh thay đổi trực tiếp danh sách bên ngoài
            messages = val != null ? new ArrayList<>(val) : new ArrayList<>();
            return this;
        }

        /**
         * Xây dựng đối tượng ChatSession.
         * Constructor private của ChatSession sẽ thực hiện validate cuối cùng.
         *
         * @return Đối tượng ChatSession đã được xây dựng.
         */
        public ChatSession build() {
            return new ChatSession(this);
        }
    }
}
