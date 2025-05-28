package com.poly.ai.management.domain.entity;

import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.DatasetID;
import com.poly.ai.management.domain.valueobject.TrainingJobID;
import com.poly.domain.entity.BaseEntity;

import java.time.LocalDateTime;

public class TrainingJob extends BaseEntity<TrainingJobID> {

    private AiModelID aiModelID;
    private DatasetID datasetID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public AiModelID getAiModelID() {
        return aiModelID;
    }

    public DatasetID getDatasetID() {
        return datasetID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    private TrainingJob(Builder builder) {
        super.setId(builder.trainingJobID);
        aiModelID = builder.aiModelID;
        datasetID = builder.datasetID;
        startTime = builder.startTime;
        endTime = builder.endTime;
        status = builder.status;
    }


    public static final class Builder {
        private TrainingJobID trainingJobID;
        private AiModelID aiModelID;
        private DatasetID datasetID;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String status;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder trainingJobID(TrainingJobID val) {
            trainingJobID = val;
            return this;
        }

        public Builder aiModelID(AiModelID val) {
            aiModelID = val;
            return this;
        }

        public Builder datasetID(DatasetID val) {
            datasetID = val;
            return this;
        }

        public Builder startTime(LocalDateTime val) {
            startTime = val;
            return this;
        }

        public Builder endTime(LocalDateTime val) {
            endTime = val;
            return this;
        }

        public Builder status(String val) {
            status = val;
            return this;
        }

        public TrainingJob build() {
            return new TrainingJob(this);
        }
    }
}
