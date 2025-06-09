package com.poly.ai.management.domain.handler.ai;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.entity.rag.Embedding;
import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.ai.management.domain.port.output.repository.EmbeddingRepository;
import com.poly.ai.management.domain.valueobject.EmbeddingID;
import com.poly.service.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FindSimilarEmbeddingsHandler extends BaseHandler<AiDomainService, EmbeddingRepository> {

    public FindSimilarEmbeddingsHandler(AiDomainService domainService, EmbeddingRepository embeddingRepository) {
        super(domainService, embeddingRepository);
    }

    @Transactional
    public List<Embedding> findSimilarEmbeddings(Embedding queryEmbedding, int topK) {
        if (queryEmbedding.getId() == null) {
            queryEmbedding.setId(new EmbeddingID(UUID.randomUUID().toString()));
            queryEmbedding = repository.save(queryEmbedding);
        }

        List<Embedding> allEmbeddings = repository.findAll();
        float[] queryVector = queryEmbedding.getVectorArray(); // dùng float[] thay vì List<Float>

        List<Embedding> embeddingsFromDb = allEmbeddings.stream()
                .filter(e -> cosineSimilarity(e.getVectorArray(), queryVector) > 0.7)
                .sorted((e1, e2) -> Double.compare(
                        cosineSimilarity(e2.getVectorArray(), queryVector),
                        cosineSimilarity(e1.getVectorArray(), queryVector)))
                .limit(topK)
                .collect(Collectors.toList());

        log.info("Found {} similar embeddings for Embedding id: {}", embeddingsFromDb.size(), queryEmbedding.getId().getValue());
        return embeddingsFromDb;
    }

    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        if (vectorA == null || vectorB == null || vectorA.length != vectorB.length) {
            throw new AiDomainException("Vectors must be non-null and of the same length for cosine similarity!");
        }

        float dotProduct = 0.0f;
        float normA = 0.0f;
        float normB = 0.0f;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += vectorA[i] * vectorA[i];
            normB += vectorB[i] * vectorB[i];
        }

        double denominator = Math.sqrt(normA) * Math.sqrt(normB);
        if (denominator == 0.0) {
            return 0.0;
        }

        return dotProduct / denominator;
    }
}
