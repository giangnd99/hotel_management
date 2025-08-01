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
        
        return new Table(
                dto.id(),
                dto.number(),
                TableStatus.valueOf(dto.status())
        );
    }
} 