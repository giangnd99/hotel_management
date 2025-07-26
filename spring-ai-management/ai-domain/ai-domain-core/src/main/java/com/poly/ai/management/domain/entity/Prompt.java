package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.domain.entity.BaseEntity;

public class Prompt extends BaseEntity<PromptID> {

    private String text;
    private String content;
    private AiModelID modelId;

    public void validate() {
        if (text == null || text.isEmpty()) {
            throw new IllegalStateException("Prompt text cannot be empty");
        }
        if (text.length() > 1000) {
            throw new IllegalStateException("Prompt text exceeds maximum length");
        }
    }

    public void preprocess() {
        text = text.trim().replaceAll("\\s+", " ");
    }

    public String getText() {
        return text;
    }

    public AiModelID getModelId() {
        return modelId;
    }

    private Prompt(Builder builder) {
        super.setId(builder.promptID);
        text = builder.text;
        content = builder.content;
        modelId = builder.modelId;
    }


    public static final class Builder {
        private PromptID promptID;
        private String text;
        private String content;
        private AiModelID modelId;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(PromptID val) {
            promptID = val;
            return this;
        }

        public Builder text(String val) {
            text = val;
            return this;
        }

        public Builder modelId(AiModelID val) {
            modelId = val;
            return this;
        }

        public Builder content(String val) {
            content = val;
            return this;
        }

        public Prompt build() {
            return new Prompt(this);
        }
    }
}
