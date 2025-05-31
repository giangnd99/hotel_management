package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.repository.DatasetJPARepository;
import com.poly.ai.management.domain.entity.Dataset;
import com.poly.ai.management.domain.port.output.repository.DatasetRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DatasetRepositoryImpl implements DatasetRepository {

    private final DatasetJPARepository datasetJPARepository;

    public DatasetRepositoryImpl(DatasetJPARepository datasetJPARepository) {
        this.datasetJPARepository = datasetJPARepository;
    }

    @Override
    public Dataset save(Dataset preparedDataset) {
        return datasetJPARepository.save(preparedDataset);
    }

    @Override
    public Optional<Dataset> findById(String datasetId) {
        return Optional.empty();
    }
}
