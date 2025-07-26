package com.poly.ai.management.domain.port.input.service;

import com.poly.ai.management.domain.entity.train.Dataset;
import com.poly.ai.management.domain.entity.train.TrainingJob;

public interface FineTuningAIService {

    Dataset prepareDataset(Dataset dataset);

    TrainingJob startTraining(TrainingJob job);

    TrainingJob completeTraining(TrainingJob job);

    TrainingJob failTraining(TrainingJob job, String errorMessage);
}
