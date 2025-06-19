package com.poly.ai.management.application.resolver;

import com.poly.ai.management.domain.entity.*;
import com.poly.ai.management.domain.port.input.service.AiHotelApplicationService;
import com.poly.ai.management.application.dto.*;
import com.poly.ai.management.application.mapper.AiGraphQLMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AiGraphQLResolver {

    private final AiHotelApplicationService aiService;

    // === QUERIES ===

    @QueryMapping
    public List<Embedding> findSimilarEmbeddings(@Argument ProcessPromptInput prompt, @Argument int topK) {
        Prompt promptEntity = AiGraphQLMapper.toPrompt(prompt);
        Embedding queryEmbedding = aiService.generateEmbedding(promptEntity);
        return aiService.findSimilarEmbeddings(queryEmbedding, topK);
    }

    // === MUTATIONS ===

    @MutationMapping
    public AiModel createAiModel(@Argument CreateAiModelInput input) {
        return aiService.createAiModel(AiGraphQLMapper.toAiModel(input));
    }

    @MutationMapping
    public Response processPrompt(@Argument ProcessPromptInput input) {
        // Sử dụng model mặc định hoặc truyền qua context nếu cần
        return aiService.processPrompt(AiGraphQLMapper.toPrompt(input));
    }

    @MutationMapping
    public Embedding generateEmbedding(@Argument GenerateEmbeddingInput input) {
        return aiService.generateEmbedding(AiGraphQLMapper.toPrompt(input, "default-model"));
    }

    @MutationMapping
    public Dataset prepareDataset(@Argument PrepareDatasetInput input) {
        return aiService.prepareDataset(AiGraphQLMapper.toDataset(input));
    }

    @MutationMapping
    public TrainingJob startTraining(@Argument StartTrainingInput input) {
        return aiService.startTraining(AiGraphQLMapper.toTrainingJob(input));
    }

    @MutationMapping
    public TrainingJob completeTraining(@Argument CompleteTrainingInput input) {
        return aiService.completeTraining(AiGraphQLMapper.toCompleteJob(input));
    }

    @MutationMapping
    public TrainingJob failTraining(@Argument FailTrainingInput input) {
        return aiService.failTraining(AiGraphQLMapper.toFailJob(input), input.getMessage());
    }
}
