package com.poly.ai.management.domain;

import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.entity.Dataset;
import com.poly.ai.management.domain.entity.Embedding;
import com.poly.ai.management.domain.entity.Prompt;
import com.poly.ai.management.domain.entity.Response;
import com.poly.ai.management.domain.entity.TrainingJob;
import com.poly.ai.management.domain.exception.AiDomainException;

import java.util.List;

public class AiDomainServiceImpl implements AiDomainService {

    @Override
    public AiModel validateAndInitiateAiModel(AiModel model) {
        validateAiModel(model);
        model.validate();
        return model;
    }

    @Override
    public Response processPrompt(Prompt prompt, AiModel model) {
        validateAiModel(model);
        prompt.validate();
        prompt.preprocess();
        Response response = Response.builder()
                .modelId(model.getId().getValue())
                .promptId(prompt.getId().getValue())
                .build();
        return response;
    }

    @Override
    public Embedding generateEmbedding(Prompt prompt, AiModel model) {
        validateAiModel(model);
        prompt.validate();
        return Embedding.builder()
                .dataId(prompt.getId().getValue())
                .modelId(model.getId().getValue())
                .build();
    }

    @Override
    public List<Embedding> findSimilarEmbeddings(Embedding queryEmbedding, int topK) {
        queryEmbedding.validate();
        queryEmbedding.normalize();
        return List.of(); // Giả lập, sẽ được triển khai ở tầng infrastructure
    }

    @Override
    public Dataset prepareDataset(Dataset dataset) {
        dataset.validate();
        dataset.split(0.8); // Ví dụ: chia dataset với tỷ lệ 80%
        return dataset;
    }

    @Override
    public TrainingJob startTraining(TrainingJob job, AiModel model, Dataset dataset) {
        validateAiModel(model);
        dataset.validate();
        job.initialize(model.getId().getValue(), dataset.getId().getValue());
        job.start();
        return job;
    }

    @Override
    public TrainingJob completeTraining(TrainingJob job) {
        job.complete();
        return job;
    }

    @Override
    public TrainingJob failTraining(TrainingJob job, String errorMessage) {
        job.fail(errorMessage);
        return job;
    }

    private void validateAiModel(AiModel model) {
        if (!model.isActive()) {
            throw new AiDomainException("AI Model with id " + model.getId() + " is currently not active!");
        }
    }
}