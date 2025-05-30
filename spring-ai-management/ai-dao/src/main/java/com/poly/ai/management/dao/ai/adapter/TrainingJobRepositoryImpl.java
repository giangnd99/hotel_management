package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.repository.TrainingJobJPARepository;
import com.poly.ai.management.domain.entity.TrainingJob;
import com.poly.ai.management.domain.port.output.repository.TrainingJobRepository;
import org.springframework.stereotype.Component;

@Component
public class TrainingJobRepositoryImpl implements TrainingJobRepository {

    private final TrainingJobJPARepository trainingJobJPARepository;

    public TrainingJobRepositoryImpl(TrainingJobJPARepository trainingJobJPARepository) {
        this.trainingJobJPARepository = trainingJobJPARepository;
    }


    @Override
    public TrainingJob save(TrainingJob startedJob) {
        return trainingJobJPARepository.save(startedJob);
    }
}
