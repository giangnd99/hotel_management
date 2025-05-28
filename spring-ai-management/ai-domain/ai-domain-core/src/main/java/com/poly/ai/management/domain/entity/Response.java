package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.ResponseID;
import com.poly.domain.entity.BaseEntity;

public class Response extends BaseEntity<ResponseID> {

    private String generatedText;
    private String modelId;
    private String promptId;

    public String getGeneratedText() {
        return generatedText;
    }

    public String getModelId() {
        return modelId;
    }

    public String getPromptId() {
        return promptId;
    }

    private Response(Builder builder) {
        super.setId(builder.responseID);
        generatedText = builder.generatedText;
        modelId = builder.modelId;
        promptId = builder.promptId;
    }


    public static final class Builder {
        private ResponseID responseID;
        private String generatedText;
        private String modelId;
        private String promptId;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder responseID(ResponseID val) {
            responseID = val;
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
