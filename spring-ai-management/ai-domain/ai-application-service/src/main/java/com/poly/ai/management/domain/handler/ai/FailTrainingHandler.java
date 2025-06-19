package com.poly.ai.management.domain.handler.ai;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.entity.TrainingJob;
import com.poly.ai.management.domain.port.output.repository.AiModelRepository;
import com.poly.ai.management.domain.port.output.repository.TrainingJobRepository;
import com.poly.service.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class FailTrainingHandler extends BaseHandler<AiDomainService, TrainingJobRepository> {

    public FailTrainingHandler(AiDomainService domainService, TrainingJobRepository trainingJobRepository) {
        super(domainService, trainingJobRepository);
    }

    @Transactional
    public TrainingJob failTraining(TrainingJob job, String errorMessage) {
        // Gọi domainService service để đánh dấu thất bại
        TrainingJob failedJob = domainService.failTraining(job, errorMessage);
        // Lưu TrainingJob
        TrainingJob savedJob = repository.save(failedJob);
        log.info("Failed TrainingJob with id: {}, error: {}", savedJob.getId().getValue(), errorMessage);
        return savedJob;
    }
}
