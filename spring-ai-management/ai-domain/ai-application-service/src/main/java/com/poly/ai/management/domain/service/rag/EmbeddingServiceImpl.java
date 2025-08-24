package com.poly.ai.management.domain.service.rag;

import com.poly.ai.management.domain.port.input.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingServiceImpl implements EmbeddingService {

    private final EmbeddingModel embeddingModel;

    @Override
    public List<float[]> embedDocuments(List<Document> documents) {

        EmbeddingOptions defaultOptions = EmbeddingOptionsBuilder.builder().build();

        BatchingStrategy defaultBatchingStrategy = new TokenCountBatchingStrategy();

        return embeddingModel.embed(documents, defaultOptions, defaultBatchingStrategy);
    }

    public float[] embedDocument(Document document) {
        return embeddingModel.embed(document);
    }
}
