package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.handler.DeleteItemHandler;
import com.poly.inventory.application.port.out.DeleteInventoryPort;

public class DeleteItemHandlerImpl implements DeleteItemHandler {
    private final DeleteInventoryPort deletePort;

    public DeleteItemHandlerImpl(DeleteInventoryPort deletePort) {
        this.deletePort = deletePort;
    }

    @Override
    public void deleteById(Integer id) {
        deletePort.deleteById(id);
    }
}
