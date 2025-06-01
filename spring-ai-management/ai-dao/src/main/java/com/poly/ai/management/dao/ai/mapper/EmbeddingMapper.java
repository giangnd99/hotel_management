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
        entity.setId(embedding.getId());
        entity.setPromptId(embedding.getPromptId());
        entity.setModelId(embedding.getModelId());
        entity.setVector(embedding.getVectorArray());
        return entity;
    }

    public static Embedding toDomainEntity(EmbeddingEntity entity) {
        return Embedding.builder()
                .embeddingId(new EmbeddingID(entity.getId()))
                .promptId(new PromptID(entity.getPromptId().getValue()))
                .modelId(new AiModelID(entity.getModelId().getValue()))
                .vector(entity.getVector())
                .build();
    }
}
