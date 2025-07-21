package com.poly.ai.management.domain.port.input.service;

import com.poly.ai.management.domain.entity.*;
import com.poly.ai.management.domain.entity.rag.Embedding;
import com.poly.ai.management.domain.entity.train.Response;

import java.util.List;

public interface AiHotelApplicationService {
    AiModel createAiModel(AiModel model);

    Response processPrompt(Prompt prompt);

    Embedding generateEmbedding(Prompt prompt);

    List<Embedding> findSimilarEmbeddings(Embedding queryEmbedding, int topK);

    Booking findAvailableBooking(Booking booking);
}
