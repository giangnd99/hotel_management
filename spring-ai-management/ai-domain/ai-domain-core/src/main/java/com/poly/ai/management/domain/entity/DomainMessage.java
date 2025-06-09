package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.DomainMessageID;
import com.poly.domain.entity.BaseEntity;

import java.io.Serializable;
import java.util.Objects;

public class DomainMessage extends BaseEntity<DomainMessageID> implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {
        SYSTEM, USER, ASSISTANT
    }

    private Type type;
    private String content;

    // Constructors
    public DomainMessage() {
    }

    public DomainMessage(Type type, String content) {
        this.type = Objects.requireNonNull(type, "Message type must not be null");
        this.content = Objects.requireNonNull(content, "Message content must not be null");
    }

    // Getters and Setters
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainMessage that = (DomainMessage) o;
        return type == that.type && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, content);
    }

    @Override
    public String toString() {
        return "DomainMessage{" +
                "type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
