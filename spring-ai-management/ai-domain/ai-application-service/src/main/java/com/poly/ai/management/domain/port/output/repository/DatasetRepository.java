package com.poly.ai.management.domain.port.output.repository;


import com.poly.ai.management.domain.entity.train.Dataset;

import java.util.Optional;

public interface DatasetRepository {

    Dataset save(Dataset preparedDataset);

    Optional<Dataset> findById(String datasetId);
}
