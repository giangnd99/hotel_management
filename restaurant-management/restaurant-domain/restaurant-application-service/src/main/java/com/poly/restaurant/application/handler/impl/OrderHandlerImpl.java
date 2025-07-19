package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.port.out.OrderRepositoryPort;
import com.poly.restaurant.application.port.out.RepositoryPort;
import com.poly.restaurant.domain.entity.Order;
import lombok.RequiredArgsConstructor;

@DomainHandler
@RequiredArgsConstructor
public class OrderHandlerImpl extends AbstractGenericHandlerImpl<Order, Integer> implements OrderHandler {

    private final OrderRepositoryPort repository;

    @Override
    protected RepositoryPort<Order, Integer> getRepository() {
        return repository;
    }
}
