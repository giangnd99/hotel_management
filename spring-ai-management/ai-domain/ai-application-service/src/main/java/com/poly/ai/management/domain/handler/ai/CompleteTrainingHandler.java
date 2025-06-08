package com.poly.ai.management.domain.handler.ai;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.entity.TrainingJob;
import com.poly.ai.management.domain.port.output.repository.TrainingJobRepository;
import com.poly.service.handler.BaseHandler;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class CompleteTrainingHandler extends BaseHandler<AiDomainService, TrainingJobRepository> {

    private final OllamaChatModel ollamaChatModel;

    public CompleteTrainingHandler(AiDomainService domainService, TrainingJobRepository trainingJobRepository, OllamaChatModel ollamaChatModel) {
        super(domainService, trainingJobRepository);
        this.ollamaChatModel = ollamaChatModel;
    }

    @Transactional
    public TrainingJob completeTraining(TrainingJob job) {
        // Gọi domainService service để hoàn thành
        TrainingJob completedJob = domainService.completeTraining(job);

        // Lưu TrainingJob
        TrainingJob savedJob = repository.save(completedJob);

        log.info("Completed TrainingJob with id: {}", savedJob.getId().getValue());
        return savedJob;
    }
}
