package com.poly.restaurant.dataaccess.mapper;

import com.poly.restaurant.dataaccess.entity.TableJpaEntity;
import com.poly.restaurant.domain.entity.Table;

public class TableEntityMapper {

    private TableEntityMapper() {
        // private constructor to prevent instantiation
    }

    public static TableJpaEntity toEntity(Table table) {
        if (table == null) return null;

        return TableJpaEntity.builder()
                .id(table.getId())
                .number(table.getNumber())
                .status(table.getStatus())
                .build();
    }

    public static Table toDomain(TableJpaEntity entity) {
        if (entity == null) return null;

        return new Table(
                entity.getId(),
                entity.getNumber(),
                entity.getStatus()
        );
    }
}