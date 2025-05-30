package com.poly.ai.management.domain.port.output.repository;


import com.poly.ai.management.domain.entity.TrainingJob;

public interface TrainingJobRepository {

    TrainingJob save(TrainingJob startedJob);
}
