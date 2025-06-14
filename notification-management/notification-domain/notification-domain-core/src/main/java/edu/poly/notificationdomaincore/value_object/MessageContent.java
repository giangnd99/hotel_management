package edu.poly.notificationdomaincore.value_object;

/**
 * Value Object đại diện cho nội dung tin nhắn
 */
public class MessageContent {
    private final String content;

    public MessageContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }
}