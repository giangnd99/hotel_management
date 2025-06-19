package com.poly.ai.management.domain.port.output.repository;


import com.poly.ai.management.domain.entity.rag.Embedding;

import java.util.List;

public interface EmbeddingRepository {

    Embedding save(Embedding queryEmbedding);

    List<Embedding> findAll();
}
