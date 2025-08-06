package com.poly.restaurant.application.port.in.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.*;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.port.in.TableUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@DomainHandler
@RequiredArgsConstructor
@Slf4j
public class TableUseCaseImpl implements TableUseCase {

    private final OrderHandler orderHandler;

    @Override
    public List<TableDTO> getAllTables() {
        log.info("Getting all tables");
        return List.of();
    }
}
