package com.poly.ai.management.domain.entity.train;


import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.ai.management.domain.valueobject.ResponseID;
import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.domain.entity.BaseEntity;

public class Response extends BaseEntity<ResponseID> {
    private String generatedText;
    private final AiModelID modelId;
    private String source;
    private final PromptID promptId;

    private Response(Builder builder) {
        super.setId(builder.responseId);
        this.generatedText = builder.generatedText;
        this.modelId = builder.modelId;
        this.promptId = builder.promptId;
        this.source = builder.source;
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
        if (modelId == null || modelId.getValue().isEmpty()) {
            throw new AiDomainException("Model ID cannot be empty!");
        }
        if (promptId == null || promptId.getValue().isEmpty()) {
            throw new AiDomainException("Prompt ID cannot be empty!");
        }
    }

    public String getGeneratedText() {
        return generatedText;
    }

    public AiModelID getModelId() {
        return modelId;
    }

    public PromptID getPromptId() {
        return promptId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ResponseID responseId;
        private String generatedText;
        private AiModelID modelId;
        private PromptID promptId;
        private String source;

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

        public Builder modelId(AiModelID val) {
            modelId = val;
            return this;
        }

        public Builder promptId(PromptID val) {
            promptId = val;
            return this;
        }

        public Builder source(String val) {
            source = val;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}