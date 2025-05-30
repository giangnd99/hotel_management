package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.TrainingJobID;
import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.domain.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainingJob extends BaseEntity<TrainingJobID> {
    private final String modelId;
    private final String datasetId;
    private String status;
    private List<String> errorMessages;

    private TrainingJob(Builder builder) {
        super.setId(builder.trainingJobId);
        this.modelId = builder.modelId;
        this.datasetId = builder.datasetId;
        this.status = builder.status;
        this.errorMessages = builder.errorMessages;
    }

    public void initialize(String modelId, String datasetId) {
        if (getId() != null) {
            throw new AiDomainException("TrainingJob is already initialized!");
        }
        if (modelId == null || modelId.isEmpty()) {
            throw new AiDomainException("Model ID cannot be empty!");
        }
        if (datasetId == null || datasetId.isEmpty()) {
            throw new AiDomainException("Dataset ID cannot be empty!");
        }
        this.status = "PENDING";
    }

    public void start() {
        if (!"PENDING".equals(status)) {
            throw new AiDomainException("TrainingJob must be in PENDING state to start!");
        }
        this.status = "RUNNING";
    }

    public void complete() {
        if (!"RUNNING".equals(status)) {
            throw new AiDomainException("TrainingJob must be in RUNNING state to complete!");
        }
        this.status = "COMPLETED";
    }

    public void fail(String errorMessage) {
        if (!"RUNNING".equals(status)) {
            throw new AiDomainException("TrainingJob must be in RUNNING state to fail!");
        }
        this.status = "FAILED";
        if (errorMessages == null) {
            errorMessages = new ArrayList<>();
        }
        errorMessages.add(errorMessage);
    }

    public String getModelId() {
        return modelId;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getErrorMessages() {
        return errorMessages != null ? Collections.unmodifiableList(errorMessages) : Collections.emptyList();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private TrainingJobID trainingJobId;
        private String modelId;
        private String datasetId;
        private String status = "PENDING";
        private List<String> errorMessages;

        private Builder() {
        }

        public Builder trainingJobId(TrainingJobID val) {
            trainingJobId = val;
            return this;
        }

        public Builder modelId(String val) {
            modelId = val;
            return this;
        }

        public Builder datasetId(String val) {
            datasetId = val;
            return this;
        }

        public Builder status(String val) {
            status = val;
            return this;
        }

        public Builder errorMessages(List<String> val) {
            errorMessages = val;
            return this;
        }

        public TrainingJob build() {
            return new TrainingJob(this);
        }
    }
}