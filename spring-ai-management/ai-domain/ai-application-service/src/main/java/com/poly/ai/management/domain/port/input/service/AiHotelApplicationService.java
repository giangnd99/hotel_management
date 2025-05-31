package com.poly.ai.management.domain.port.input.service;

import com.poly.ai.management.domain.entity.*;

import java.util.List;

public interface AiHotelApplicationService {
    AiModel createAiModel(AiModel model);

    Response processPrompt(Prompt prompt);

    Embedding generateEmbedding(Prompt prompt);

    List<Embedding> findSimilarEmbeddings(Embedding queryEmbedding, int topK);

    Dataset prepareDataset(Dataset dataset);

    TrainingJob startTraining(TrainingJob job);

    TrainingJob completeTraining(TrainingJob job);

    TrainingJob failTraining(TrainingJob job, String errorMessage);
}
