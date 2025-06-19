package com.poly.ai.management.dao.ai.mapper;

import com.poly.ai.management.dao.ai.entity.ResponseEntity;
import com.poly.ai.management.domain.entity.train.Response;
import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.ai.management.domain.valueobject.ResponseID;

public class ResponseMapper {

    public static Response toDomain(ResponseEntity entity) {
        return Response.builder()
                .responseId(new ResponseID(entity.getId()))
                .generatedText(entity.getGeneratedText())
                .modelId(new AiModelID(entity.getAiModelId()))
                .promptId(new PromptID(entity.getPromptId()))
                .build();
    }

    public static ResponseEntity toJPA(Response domain) {
        ResponseEntity entity = new ResponseEntity();
        entity.setId(domain.getId().getValue());
        entity.setGeneratedText(domain.getGeneratedText());
        entity.setPromptId(domain.getPromptId().getValue());
        entity.setAiModelId(domain.getModelId().getValue());
        return entity;
    }
}
