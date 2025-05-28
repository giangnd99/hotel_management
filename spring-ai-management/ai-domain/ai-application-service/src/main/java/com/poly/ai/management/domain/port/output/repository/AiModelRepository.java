package com.poly.ai.management.domain.port.output.repository;

import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.valueobject.AiModelID;

import java.util.List;

public interface AiModelRepository {
    AiModel findById(AiModelID aiModelID);
    List<AiModel> findAll();
    void save(AiModel aiModel);
}
