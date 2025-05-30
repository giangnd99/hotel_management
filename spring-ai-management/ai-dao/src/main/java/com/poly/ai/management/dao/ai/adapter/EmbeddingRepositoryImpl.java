package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.repository.EmbeddingJPARepository;
import com.poly.ai.management.domain.entity.Embedding;
import com.poly.ai.management.domain.port.output.repository.EmbeddingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmbeddingRepositoryImpl extends EmbeddingRepository {

    private final EmbeddingJPARepository embeddingJPARepository;

    public EmbeddingRepositoryImpl(EmbeddingJPARepository embeddingJPARepository) {
        this.embeddingJPARepository = embeddingJPARepository;
    }

    @Override
    public Embedding save(Embedding queryEmbedding) {
        return embeddingJPARepository.save(queryEmbedding);
    }

    @Override
    public List<Embedding> findAll() {
        return embeddingJPARepository.findAll();
    }
}
