package com.poly.ai.management.dao.ai.mapper;

import com.poly.ai.management.dao.ai.entity.AiModelEntity;
import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.valueobject.AiModelID;

public class AiModelMapper {

    public static AiModel toDomain(AiModelEntity entity) {
        return AiModel.Builder.builder()
                .aiModelID(new AiModelID(entity.getId()))
                .name(entity.getName())
                .provider(entity.getProvider())
                .version(entity.getVersion())
                .isActive(entity.isActive())
                .build();
    }

    public static AiModelEntity toJPA(AiModel domain) {
        AiModelEntity entity = new AiModelEntity();
        entity.setId(domain.getId().getValue());
        entity.setName(domain.getName());
        entity.setProvider(domain.getProvider());
        entity.setVersion(domain.getVersion());
        entity.setActive(domain.isActive());
        return entity;
    }
}
