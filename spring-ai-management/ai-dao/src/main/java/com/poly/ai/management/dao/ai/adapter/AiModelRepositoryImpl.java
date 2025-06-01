package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.entity.AiModelEntity;
import com.poly.ai.management.dao.ai.mapper.AiModelMapper;
import com.poly.ai.management.dao.ai.repository.AiModelJPARepository;
import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.port.output.repository.AiModelRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AiModelRepositoryImpl implements AiModelRepository {

    private final AiModelJPARepository repository;

    public AiModelRepositoryImpl(AiModelJPARepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AiModel> findById(String aiModelID) {
        return repository.findById(aiModelID)
                .map(AiModelMapper::toDomain);
    }

    @Override
    public List<AiModel> findAll() {
        return repository.findAll()
                .stream()
                .map(AiModelMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public AiModel save(AiModel aiModel) {
        AiModelEntity entity = AiModelMapper.toJPA(aiModel);
        AiModelEntity saved = repository.save(entity);
        return AiModelMapper.toDomain(saved);
    }
}
