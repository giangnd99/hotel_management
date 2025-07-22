package com.poly.ai.management.domain.handler.ai;

import com.poly.ai.management.domain.AiDomainService;
import com.poly.ai.management.domain.entity.train.Dataset;
import com.poly.ai.management.domain.port.output.repository.DatasetRepository;
import com.poly.service.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class PrepareDatasetHandler extends BaseHandler<AiDomainService, DatasetRepository> {

    public PrepareDatasetHandler(AiDomainService domainService, DatasetRepository datasetRepository) {
        super(domainService, datasetRepository);
    }

    @Transactional
    public Dataset prepareDataset(Dataset dataset) {
        // Gán ID cho Dataset
//        dataset.setId(new DatasetID(UUID.randomUUID().toString()));
        // Gọi domainService service để chuẩn bị Dataset
        Dataset preparedDataset = domainService.prepareDataset(dataset);
        // Lưu Dataset
        Dataset savedDataset = repository.save(preparedDataset);
        log.info("Prepared Dataset with id: {}", savedDataset.getId().getValue());
        return savedDataset;
    }
}
