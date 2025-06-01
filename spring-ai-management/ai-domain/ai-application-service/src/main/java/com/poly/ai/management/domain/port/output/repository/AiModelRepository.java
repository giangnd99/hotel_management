package com.poly.ai.management.domain.port.output.repository;

import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.valueobject.AiModelID;

import java.util.List;
import java.util.Optional;

public interface AiModelRepository {
    Optional<AiModel> findById(String aiModelID);
    List<AiModel> findAll();
    AiModel save(AiModel aiModel);
}
