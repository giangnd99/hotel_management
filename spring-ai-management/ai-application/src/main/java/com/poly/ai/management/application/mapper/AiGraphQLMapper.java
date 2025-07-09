package com.poly.ai.management.application.mapper;

import com.poly.ai.management.application.dto.*;
import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.entity.train.Dataset;
import com.poly.ai.management.domain.entity.Prompt;
import com.poly.ai.management.domain.entity.train.TrainingJob;
import com.poly.ai.management.domain.valueobject.AiModelID;
import com.poly.ai.management.domain.valueobject.DatasetID;

import java.util.List;

public class AiGraphQLMapper {

    public static AiModel toAiModel(CreateAiModelInput input) {
        return AiModel.Builder.builder()
                .name(input.getName())
                .provider(input.getProvider())
                .build();
    }

    public static Prompt toPrompt(ProcessPromptInput input) {
        return Prompt.Builder.builder()
                .modelId(new AiModelID(input.getModelId()))
                .text(input.getPrompt())
                .build();
    }

    public static Prompt toPrompt(GenerateEmbeddingInput input, String modelId) {
        return Prompt.Builder.builder()
                .modelId(new AiModelID(modelId))
                .text(input.getPrompt())
                .build();
    }

    public static TrainingJob toTrainingJob(StartTrainingInput input) {
        return TrainingJob.builder()
                .modelId(new AiModelID(input.getModelId()))
                .datasetId(new DatasetID(input.getDatasetId()))
                .build();
    }

    public static TrainingJob toCompleteJob(CompleteTrainingInput input) {
        return TrainingJob.builder()
                .build();
    }

    public static TrainingJob toFailJob(FailTrainingInput input) {
        return TrainingJob.builder()
                .errorMessages(List.of(input.getMessage()))
                .build();
    }

    public static Dataset toDataset(PrepareDatasetInput input) {
        return Dataset.Builder.builder()
                .name(input.getName())
                .size(input.getSize())
                .source(input.getSource())
                .build();
    }
}
