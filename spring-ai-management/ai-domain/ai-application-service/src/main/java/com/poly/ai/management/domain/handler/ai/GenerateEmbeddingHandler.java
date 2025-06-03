package com.poly.ai.management.domain.handler.ai;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.entity.Embedding;
import com.poly.ai.management.domain.entity.Prompt;
import com.poly.ai.management.domain.port.output.repository.AiModelRepository;
import com.poly.ai.management.domain.port.output.repository.EmbeddingRepository;
import com.poly.ai.management.domain.port.output.repository.PromptRepository;
import com.poly.ai.management.domain.valueobject.EmbeddingID;
import com.poly.ai.management.domain.valueobject.PromptID;
import com.poly.service.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@Slf4j
public class GenerateEmbeddingHandler extends BaseHandler<AiDomainService, EmbeddingRepository> {

    private final EmbeddingModel embeddingModel;
    private final PromptRepository promptRepository;
    private final AiModelRepository aiModelRepository;

    public GenerateEmbeddingHandler(AiDomainService domainService, EmbeddingRepository embeddingRepository, EmbeddingModel embeddingModel, PromptRepository promptRepository, AiModelRepository aiModelRepository) {
        super(domainService, embeddingRepository);
        this.embeddingModel = embeddingModel;
        this.promptRepository = promptRepository;
        this.aiModelRepository = aiModelRepository;
    }

    @Transactional
    public Embedding generateEmbedding(Prompt prompt) {
        // Gán ID cho Prompt nếu chưa có
        if (prompt.getId() == null) {
            prompt.setId(new PromptID(UUID.randomUUID().toString()));
            prompt = promptRepository.save(prompt);
        }

        // Lấy AiModel từ repository
        Prompt finalPrompt = prompt;
        AiModel model = aiModelRepository.findById(prompt.getModelId().getValue())
                .orElseThrow(() -> new IllegalArgumentException("AI Model not found with id: " + finalPrompt.getModelId()));

        // Gọi domainService service để tạo Embedding
        Embedding embedding = domainService.generateEmbedding(prompt, model);

        // Gán ID và vector cho Embedding
        embedding.setId(new EmbeddingID(UUID.randomUUID().toString()));
        // Gọi Spring AI để tạo vector thực sự
        float[] vector = embeddingModel.embed(prompt.getText());
        embedding.setVector(vector);

        // Lưu Embedding
        Embedding savedEmbedding = repository.save(embedding);
        log.info("Generated Embedding for Prompt id: {}, Embedding id: {}", prompt.getId().getValue(), savedEmbedding.getId().getValue());
        return savedEmbedding;
    }
}
