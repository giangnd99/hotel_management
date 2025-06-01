package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.entity.TrainingJobEntity;
import com.poly.ai.management.dao.ai.mapper.TrainingJobMapper;
import com.poly.ai.management.dao.ai.repository.TrainingJobJPARepository;
import com.poly.ai.management.domain.entity.TrainingJob;
import com.poly.ai.management.domain.port.output.repository.TrainingJobRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TrainingJobRepositoryImpl implements TrainingJobRepository {

    private final TrainingJobJPARepository trainingJobJPARepository;

    public TrainingJobRepositoryImpl(TrainingJobJPARepository trainingJobJPARepository) {
        this.trainingJobJPARepository = trainingJobJPARepository;
    }

    @Override
    public TrainingJob save(TrainingJob trainingJob) {
        TrainingJobEntity entity = TrainingJobMapper.toEntity(trainingJob);
        TrainingJobEntity savedEntity = trainingJobJPARepository.save(entity);
        return TrainingJobMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TrainingJob> findById(String jobId) {
        return trainingJobJPARepository.findById(jobId)
                .map(TrainingJobMapper::toDomain);
    }
}
