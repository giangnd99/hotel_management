package com.poly.ai.management.dao.ai.mapper;

import com.poly.ai.management.dao.ai.entity.EmbeddingEntity;
import com.poly.ai.management.domain.entity.Embedding;
import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.DatasetID;
import com.poly.ai.management.domain.valueobject.EmbeddingID;
import com.poly.ai.management.domain.valueobject.PromptID;

public class EmbeddingMapper {

    public static EmbeddingEntity toJpaEntity(Embedding embedding) {
        EmbeddingEntity entity = new EmbeddingEntity();
        entity.setId(embedding.getId().getValue());
        entity.setPromptId(embedding.getPromptId().getValue());
        entity.setModelId(embedding.getModelId().getValue());
        entity.setVector(embedding.getVectorArray());
        return entity;
    }

    public static Embedding toDomainEntity(EmbeddingEntity entity) {
        return Embedding.builder()
                .embeddingId(new EmbeddingID(entity.getId()))
                .promptId(new PromptID(entity.getPromptId()))
                .modelId(new AiModelID(entity.getModelId()))
                .vector(entity.getVector())
                .build();
    }
}
