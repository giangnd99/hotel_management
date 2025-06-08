package com.poly.ai.management.domain;


import com.poly.ai.management.domain.entity.*;
import com.poly.ai.management.domain.exception.AiDomainException;
import com.poly.ai.management.domain.handler.ai.*;
import com.poly.ai.management.domain.port.input.service.AiHotelApplicationService;
import com.poly.ai.management.domain.port.output.repository.*;
import com.poly.ai.management.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiHotelApplicationServiceImpl implements AiHotelApplicationService {

    private final CreateAiModelHandler createAiModelHandler;
    private final CompleteTrainingHandler completeTrainingHandler;
    private final FailTrainingHandler failTrainingHandler;
    private final FindSimilarEmbeddingsHandler findSimilarEmbeddingsHandler;
    private final GenerateEmbeddingHandler generateEmbeddingHandler;
    private final PrepareDatasetHandler prepareDatasetHandler;
    private final ProcessPromptHandler processPromptHandler;
    private final StartTrainingHandler startTrainingHandler;

    @Override
    @Transactional
    public AiModel createAiModel(AiModel model) {
        return createAiModelHandler.createAiModel(model);
    }

    @Override
    @Transactional
    public Response processPrompt(Prompt prompt) {
        return processPromptHandler.processPrompt(prompt);
    }

    @Override
    @Transactional
    public Embedding generateEmbedding(Prompt prompt) {
        return generateEmbeddingHandler.generateEmbedding(prompt);
    }

    @Override
    @Transactional
    public List<Embedding> findSimilarEmbeddings(Embedding queryEmbedding, int topK) {
        return findSimilarEmbeddingsHandler.findSimilarEmbeddings(queryEmbedding, topK);
    }

    @Override
    @Transactional
    public Dataset prepareDataset(Dataset dataset) {
        return prepareDatasetHandler.prepareDataset(dataset);
    }

    @Override
    @Transactional
    public TrainingJob startTraining(TrainingJob job) {
        return startTrainingHandler.startTraining(job);
    }

    @Override
    @Transactional
    public TrainingJob completeTraining(TrainingJob job) {
        return completeTrainingHandler.completeTraining(job);
    }

    @Override
    @Transactional
    public TrainingJob failTraining(TrainingJob job, String errorMessage) {
        return failTrainingHandler.failTraining(job, errorMessage);
    }

    @Override
    public Booking findAvailableBooking(Booking booking) {
        return ;
    }
}
