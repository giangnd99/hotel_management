package com.poly.ai.management.domain;

import com.poly.ai.management.domain.entity.*;
import com.poly.ai.management.domain.entity.rag.Embedding;
import com.poly.ai.management.domain.entity.train.Dataset;
import com.poly.ai.management.domain.entity.train.Response;
import com.poly.ai.management.domain.entity.train.TrainingJob;

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

    Room findAvailableRoom(AiModel model);

    Booking findAvailableBooking(AiModel model);
}