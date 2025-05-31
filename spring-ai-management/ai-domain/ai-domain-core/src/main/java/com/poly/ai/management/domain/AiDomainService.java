package com.poly.ai.management.domain;

import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.entity.Dataset;
import com.poly.ai.management.domain.entity.Embedding;
import com.poly.ai.management.domain.entity.Prompt;
import com.poly.ai.management.domain.entity.Response;
import com.poly.ai.management.domain.entity.TrainingJob;

import java.util.List;

public interface AiDomainService {

    AiModel validateAndInitiateAiModel(AiModel model);

    Response processPrompt(Prompt prompt, AiModel model);

    Embedding generateEmbedding(Prompt prompt, AiModel model);

    List<Embedding> findSimilarEmbeddings(Embedding queryEmbedding, int topK);

    Dataset prepareDataset(Dataset dataset);

    TrainingJob startTraining(TrainingJob job, AiModel model, Dataset dataset);

    TrainingJob completeTraining(TrainingJob job);

    TrainingJob failTraining(TrainingJob job, String errorMessage);
}