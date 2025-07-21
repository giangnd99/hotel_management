package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.entity.EmbeddingEntity;
import com.poly.ai.management.dao.ai.mapper.EmbeddingMapper;
import com.poly.ai.management.dao.ai.repository.EmbeddingJPARepository;
import com.poly.ai.management.domain.entity.rag.Embedding;
import com.poly.ai.management.domain.port.output.repository.EmbeddingRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmbeddingRepositoryImpl implements EmbeddingRepository {

    private final EmbeddingJPARepository embeddingJPARepository;

    public EmbeddingRepositoryImpl(EmbeddingJPARepository embeddingJPARepository) {
        this.embeddingJPARepository = embeddingJPARepository;
    }

    @Override
    public Embedding save(Embedding embedding) {
        EmbeddingEntity entity = EmbeddingMapper.toJpaEntity(embedding);
        EmbeddingEntity saved = embeddingJPARepository.save(entity);
        return EmbeddingMapper.toDomainEntity(saved);
    }

    @Override
    public List<Embedding> findAll() {
        return embeddingJPARepository.findAll()
                .stream()
                .map(EmbeddingMapper::toDomainEntity)
                .collect(Collectors.toList());
    }
}
