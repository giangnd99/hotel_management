package com.poly.ai.management.domain.handler.ai;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.entity.AiModel;
import com.poly.ai.management.domain.entity.train.Dataset;
import com.poly.ai.management.domain.entity.train.TrainingJob;
import com.poly.ai.management.domain.port.output.repository.AiModelRepository;
import com.poly.ai.management.domain.port.output.repository.DatasetRepository;
import com.poly.ai.management.domain.port.output.repository.TrainingJobRepository;
import com.poly.service.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class StartTrainingHandler extends BaseHandler<AiDomainService, TrainingJobRepository> {

    private final AiModelRepository aiModelRepository;
    private final DatasetRepository datasetRepository;

    public StartTrainingHandler(AiDomainService domainService, TrainingJobRepository trainingJobRepository, AiModelRepository aiModelRepository, DatasetRepository datasetRepository) {
        super(domainService, trainingJobRepository);
        this.aiModelRepository = aiModelRepository;
        this.datasetRepository = datasetRepository;
    }

    @Transactional
    public TrainingJob startTraining(TrainingJob job) {
        // Gán ID cho TrainingJob
//        job.setId(new TrainingJobID(UUID.randomUUID().toString()));
        // Lấy AiModel và Dataset từ repository
        AiModel model = aiModelRepository.findById(job.getModelId().getValue())
                .orElseThrow(() -> new IllegalArgumentException("AI Model not found with id: " + job.getModelId()));
        Dataset dataset = datasetRepository.findById(job.getDatasetId().getValue())
                .orElseThrow(() -> new IllegalArgumentException("Dataset not found with id: " + job.getDatasetId()));

        // Gọi domainService service để bắt đầu huấn luyện
        TrainingJob startedJob = domainService.startTraining(job, model, dataset);
        // Lưu TrainingJob
        TrainingJob savedJob = repository.save(startedJob);
        log.info("Started TrainingJob with id: {}", savedJob.getId().getValue());
        return savedJob;
    }
}
