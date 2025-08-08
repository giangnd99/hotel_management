package com.poly.restaurant.application.port.in.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.port.in.TableUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.poly.restaurant.application.dto.TableDTO;
import com.poly.restaurant.application.handler.TableHandler;
import com.poly.restaurant.application.mapper.TableMapper;
import com.poly.restaurant.domain.entity.Table;
import com.poly.restaurant.domain.entity.TableStatus;

import java.util.List;
import java.util.stream.Collectors;

@DomainHandler
@RequiredArgsConstructor
@Slf4j
public class TableUseCaseImpl implements TableUseCase {

    private final TableHandler tableHandler;

    @Override
    public List<TableDTO> getAllTables() {
        log.info("Getting all tables");
        return tableHandler.getAll()
                .stream()
                .map(TableMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TableDTO getTableById(String id) {
        log.info("Getting table by id: {}", id);
        Table table = tableHandler.getById(id);
        return TableMapper.toDto(table);
    }

    @Override
    public TableDTO createTable(TableDTO request) {
        log.info("Creating new table: {}", request);
        Table entity = TableMapper.toEntity(request);
        Table createdTable = tableHandler.createTable(entity);
        return TableMapper.toDto(createdTable);
    }

    @Override
    public TableDTO updateTable(String id, TableDTO request) {
        log.info("Updating table: {} with data: {}", id, request);
        Table entity = TableMapper.toEntity(request);
        Table updatedTable = tableHandler.update(id, entity);
        return TableMapper.toDto(updatedTable);
    }

    @Override
    public void deleteTable(String id) {
        log.info("Deleting table: {}", id);
        tableHandler.delete(id);
    }

    @Override
    public List<TableDTO> getTablesByStatus(String status) {
        log.info("Getting tables by status: {}", status);
        TableStatus tableStatus = TableStatus.valueOf(status.toUpperCase());
        return tableHandler.getTablesByStatus(tableStatus)
                .stream()
                .map(TableMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TableDTO getTableByNumber(int number) {
        log.info("Getting table by number: {}", number);
        Table table = tableHandler.getTableByNumber(number);
        return TableMapper.toDto(table);
    }

    @Override
    public TableDTO reserveTable(String tableId) {
        log.info("Reserving table: {}", tableId);
        Table table = tableHandler.reserveTable(tableId);
        return TableMapper.toDto(table);
    }

    @Override
    public TableDTO occupyTable(String tableId) {
        log.info("Occupying table: {}", tableId);
        Table table = tableHandler.occupyTable(tableId);
        return TableMapper.toDto(table);
    }

    @Override
    public TableDTO freeTable(String tableId) {
        log.info("Freeing table: {}", tableId);
        Table table = tableHandler.freeTable(tableId);
        return TableMapper.toDto(table);
    }

    @Override
    public TableDTO updateTableStatus(String tableId, String status) {
        log.info("Updating table status: {} to {}", tableId, status);
        TableStatus tableStatus = TableStatus.valueOf(status.toUpperCase());
        Table table = tableHandler.updateTableStatus(tableId, tableStatus);
        return TableMapper.toDto(table);
    }
}
