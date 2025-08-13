package com.poly.restaurant.application.mapper;

import com.poly.restaurant.application.dto.TableDTO;
import com.poly.restaurant.domain.entity.Table;
import com.poly.restaurant.domain.entity.TableStatus;

public class TableMapper {

    // Domain -> DTO: Table -> TableDTO
    public static TableDTO toDto(Table table) {
        if (table == null) return null;

        return new TableDTO(
                table.getId(),
                table.getNumber(),
                table.getStatus().name()
        );
    }

    // DTO -> Domain: TableDTO -> Table
    public static Table toEntity(TableDTO dto) {
        if (dto == null) return null;

        // Validate required fields
        if (dto.id() == null || dto.id().trim().isEmpty()) {
            throw new IllegalArgumentException("Table ID cannot be null or empty");
        }

        if (dto.number() == null || dto.number() <= 0) {
            throw new IllegalArgumentException("Table number must be positive");
        }

        // Parse status with default value
        TableStatus status = TableStatus.AVAILABLE; // Default status
        if (dto.status() != null && !dto.status().trim().isEmpty()) {
            try {
                status = TableStatus.valueOf(dto.status().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid table status: " + dto.status());
            }
        }

        return new Table(
                dto.id().trim(),
                dto.number(),
                status
        );
    }
}
