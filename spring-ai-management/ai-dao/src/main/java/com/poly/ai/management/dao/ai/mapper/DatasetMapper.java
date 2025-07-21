package com.poly.ai.management.dao.ai.mapper;

import com.poly.ai.management.dao.ai.entity.DatasetEntity;
import com.poly.ai.management.domain.entity.train.Dataset;
import com.poly.ai.management.domain.valueobject.DatasetID;

public class DatasetMapper {

    public static DatasetEntity toEntity(Dataset dataset) {
        DatasetEntity entity = new DatasetEntity();
        entity.setId(dataset.getId().getValue());
        entity.setName(dataset.getName());
        entity.setSource(dataset.getSource());
        entity.setSize(dataset.getSize());
        return entity;
    }

    public static Dataset toDomain(DatasetEntity entity) {
        return Dataset.Builder.builder()
                .id(new DatasetID(entity.getId()))
                .name(entity.getName())
                .source(entity.getSource())
                .size(entity.getSize())
                .build();
    }
}
