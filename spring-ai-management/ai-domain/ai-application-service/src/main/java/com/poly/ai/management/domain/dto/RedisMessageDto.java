package com.poly.ai.management.domain.dto;

import com.poly.ai.management.domain.entity.DomainMessage;
import com.poly.ai.management.domain.exception.AiDomainException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.ai.chat.messages.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class RedisMessageDto implements Serializable {

    private String type;
    private String content;

    public RedisMessageDto() {
    }

    public RedisMessageDto(String type, String content) {
        this.type = Objects.requireNonNull(type, "Type must not be null");
        this.content = Objects.requireNonNull(content, "Content must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedisMessageDto that = (RedisMessageDto) o;
        return Objects.equals(type, that.type) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, content);
    }
}
