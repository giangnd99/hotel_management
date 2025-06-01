package com.poly.ai.management.domain.port.output.repository;


import com.poly.ai.management.domain.entity.TrainingJob;

import java.util.Optional;

public interface TrainingJobRepository {

    TrainingJob save(TrainingJob startedJob);

    Optional<TrainingJob> findById(String jobId);
}
