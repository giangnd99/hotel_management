package com.poly.ai.management.dao.ai.mapper;

import com.poly.ai.management.dao.ai.entity.TrainingJobEntity;
import com.poly.ai.management.domain.entity.TrainingJob;
import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.DatasetID;
import com.poly.ai.management.domain.valueobject.TrainingJobID;

public class TrainingJobMapper {

    public static TrainingJobEntity toEntity(TrainingJob job) {
        TrainingJobEntity entity = new TrainingJobEntity();
        entity.setId(job.getId().getValue());
        entity.setModelId(job.getModelId().getValue());
        entity.setDatasetId(job.getDatasetId().getValue());
        entity.setStatus(job.getStatus());
        entity.setErrorMessages(job.getErrorMessages());
        return entity;
    }

    public static TrainingJob toDomain(TrainingJobEntity entity) {
        return TrainingJob.builder()
                .trainingJobId(new TrainingJobID(entity.getId()))
                .modelId(new AiModelID(entity.getModelId()))
                .datasetId(new DatasetID(entity.getDatasetId()))
                .status(entity.getStatus())
                .errorMessages(entity.getErrorMessages())
                .build();
    }
}
