package com.poly.ai.management.domain;


import com.poly.ai.management.domain.entity.*;
import com.poly.ai.management.domain.entity.rag.Embedding;
import com.poly.ai.management.domain.entity.train.Response;
import com.poly.ai.management.domain.handler.ai.*;
import com.poly.ai.management.domain.port.input.service.AiHotelApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiHotelApplicationServiceImpl implements AiHotelApplicationService {

    private final CreateAiModelHandler createAiModelHandler;
    private final FindSimilarEmbeddingsHandler findSimilarEmbeddingsHandler;
    private final GenerateEmbeddingHandler generateEmbeddingHandler;
    private final ProcessPromptHandler processPromptHandler;


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
    public Booking findAvailableBooking(Booking booking) {
        return null;
    }
}
