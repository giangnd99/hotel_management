package com.poly.ai.management.domain;


import com.poly.ai.management.domain.entity.train.Dataset;
import com.poly.ai.management.domain.entity.train.TrainingJob;
import com.poly.ai.management.domain.handler.ai.*;
import com.poly.ai.management.domain.port.input.service.FineTuningAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FineTuningAIServiceImpl implements FineTuningAIService {

    private final CompleteTrainingHandler completeTrainingHandler;
    private final FailTrainingHandler failTrainingHandler;
    private final PrepareDatasetHandler prepareDatasetHandler;
    private final StartTrainingHandler startTrainingHandler;

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
}
