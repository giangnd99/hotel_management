package com.poly.ai.management.dao.ai.adapter;

import com.poly.ai.management.dao.ai.entity.DatasetEntity;
import com.poly.ai.management.dao.ai.mapper.DatasetMapper;
import com.poly.ai.management.dao.ai.repository.DatasetJPARepository;
import com.poly.ai.management.domain.entity.train.Dataset;
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
        DatasetEntity entity = DatasetMapper.toEntity(preparedDataset);
        DatasetEntity saved = datasetJPARepository.save(entity);
        return DatasetMapper.toDomain(saved);
    }

    @Override
    public Optional<Dataset> findById(String datasetId) {
        return datasetJPARepository.findById(datasetId)
                .map(DatasetMapper::toDomain);
    }
}
