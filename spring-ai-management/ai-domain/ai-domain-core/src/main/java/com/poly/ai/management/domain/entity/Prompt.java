package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.domain.entity.BaseEntity;

public class Prompt extends BaseEntity<PromptID> {

    private String text;
    private String modelId;

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

    public String getModelId() {
        return modelId;
    }

    private Prompt(Builder builder) {
        super.setId(builder.promptID);
        text = builder.text;
        modelId = builder.modelId;
    }


    public static final class Builder {
        private PromptID promptID;
        private String text;
        private String modelId;

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

        public Builder modelId(String val) {
            modelId = val;
            return this;
        }

        public Prompt build() {
            return new Prompt(this);
        }
    }
}
