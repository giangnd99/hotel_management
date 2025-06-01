package com.poly.ai.management.dao.ai.mapper;

import com.poly.ai.management.dao.ai.entity.PromptEntity;
import com.poly.ai.management.domain.entity.Prompt;
import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.PromptID;

public class PromptMapper {

    public static Prompt toDomain(PromptEntity entity) {
        return Prompt.Builder.builder()
                .id(new PromptID(entity.getId()))
                .text(entity.getText())
                .modelId(new AiModelID(entity.getAiModelID().getValue()))
                .build();
    }

    public static PromptEntity toJPA(Prompt domain) {
        PromptEntity entity = new PromptEntity();
        entity.setId(domain.getId());
        entity.setText(domain.getText());
        entity.setAiModelID(domain.getModelId());
        return entity;
    }
}
