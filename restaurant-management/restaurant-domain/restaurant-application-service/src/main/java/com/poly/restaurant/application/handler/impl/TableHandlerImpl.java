package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.handler.TableHandler;
import com.poly.restaurant.application.port.out.repo.TableRepositoryPort;
import com.poly.restaurant.application.port.out.repo.RepositoryPort;
import com.poly.restaurant.domain.entity.Table;
import com.poly.restaurant.domain.entity.TableStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@DomainHandler
@RequiredArgsConstructor
@Slf4j
public class TableHandlerImpl extends AbstractGenericHandlerImpl<Table, String> implements TableHandler {

    private final TableRepositoryPort repository;

    @Override
    protected RepositoryPort<Table, String> getRepository() {
        return repository;
    }

    @Override
    public Table createTable(Table table) {
        log.info("Creating new table: {}", table.getId());

        // Validation: kiểm tra số bàn đã tồn tại chưa
        if (repository.existsByNumber(table.getNumber())) {
            throw new IllegalStateException("Table number " + table.getNumber() + " already exists");
        }

        return repository.save(table);
    }

    @Override
    public Table updateTableStatus(String tableId, TableStatus newStatus) {
        log.info("Updating table status: {} to {}", tableId, newStatus);
        Table table = getById(tableId);
        table.setStatus(newStatus);
        return repository.save(table);
    }

    @Override
    public List<Table> getTablesByStatus(TableStatus status) {
        log.info("Getting tables by status: {}", status);
        return repository.findByStatus(status);
    }

    @Override
    public Table getTableByNumber(int number) {
        log.info("Getting table by number: {}", number);
        return repository.findByNumber(number)
                .orElseThrow(() -> new RuntimeException("Table not found with number: " + number));
    }

    @Override
    public boolean isTableAvailable(String tableId) {
        Table table = getById(tableId);
        return table.getStatus() == TableStatus.AVAILABLE;
    }

    @Override
    public Table reserveTable(String tableId) {
        log.info("Reserving table: {}", tableId);
        Table table = getById(tableId);

        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new IllegalStateException("Table must be AVAILABLE to be reserved: " + tableId);
        }

        return updateTableStatus(tableId, TableStatus.RESERVED);
    }

    @Override
    public Table occupyTable(String tableId) {
        log.info("Occupying table: {}", tableId);
        Table table = getById(tableId);

        if (table.getStatus() == TableStatus.OCCUPIED) {
            throw new IllegalStateException("Table is already occupied: " + tableId);
        }

        return updateTableStatus(tableId, TableStatus.OCCUPIED);
    }

    @Override
    public Table freeTable(String tableId) {
        log.info("Freeing table: {}", tableId);
        Table table = getById(tableId);

        if (table.getStatus() != TableStatus.OCCUPIED) {
            throw new IllegalStateException("Table must be OCCUPIED to be freed: " + tableId);
        }

        return updateTableStatus(tableId, TableStatus.AVAILABLE);
    }
}
