package com.poly.ai.management.domain.entity;


import com.poly.ai.management.domain.valueobject.ResponseID;
import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.domain.entity.BaseEntity;

public class Response extends BaseEntity<ResponseID> {
    private String generatedText;
    private final String modelId;
    private final String promptId;

    private Response(Builder builder) {
        super.setId(builder.responseId);
        this.generatedText = builder.generatedText;
        this.modelId = builder.modelId;
        this.promptId = builder.promptId;
    }

    public void setGeneratedText(String text) {
        if (text == null || text.isEmpty()) {
            throw new AiDomainException("Response text cannot be empty!");
        }
        this.generatedText = text;
    }

    public void validate() {
        if (generatedText == null || generatedText.isEmpty()) {
            throw new AiDomainException("Response text cannot be empty!");
        }
        if (modelId == null || modelId.isEmpty()) {
            throw new AiDomainException("Model ID cannot be empty!");
        }
        if (promptId == null || promptId.isEmpty()) {
            throw new AiDomainException("Prompt ID cannot be empty!");
        }
    }

    public String getGeneratedText() {
        return generatedText;
    }

    public String getModelId() {
        return modelId;
    }

    public String getPromptId() {
        return promptId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ResponseID responseId;
        private String generatedText;
        private String modelId;
        private String promptId;

        private Builder() {
        }

        public Builder responseId(ResponseID val) {
            responseId = val;
            return this;
        }

        public Builder generatedText(String val) {
            generatedText = val;
            return this;
        }

        public Builder modelId(String val) {
            modelId = val;
            return this;
        }

        public Builder promptId(String val) {
            promptId = val;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}