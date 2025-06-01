package com.poly.ai.management.dao.ai.mapper;

import com.poly.ai.management.dao.ai.entity.ResponseEntity;
import com.poly.ai.management.domain.entity.Response;
import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.ai.management.domain.valueobject.ResponseID;

public class ResponseMapper {

    public static Response toDomain(ResponseEntity entity) {
        return Response.builder()
                .responseId(new ResponseID(entity.getId()))
                .generatedText(entity.getGeneratedText())
                .modelId(new AiModelID(entity.getAiModelId().getValue()))
                .promptId(new PromptID(entity.getPromptId().getValue()))
                .build();
    }

    public static ResponseEntity toJPA(Response domain) {
        ResponseEntity entity = new ResponseEntity();
        entity.setId(domain.getId());
        entity.setGeneratedText(domain.getGeneratedText());
        entity.setPromptId(domain.getPromptId());
        entity.setAiModelId(domain.getModelId());
        return entity;
    }
}
