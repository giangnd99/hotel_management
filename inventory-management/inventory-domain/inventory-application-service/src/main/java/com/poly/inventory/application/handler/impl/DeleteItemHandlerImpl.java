package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.handler.DeleteItemHandler;
import com.poly.inventory.application.port.out.DeleteInventoryPort;
import com.poly.inventory.domain.value_object.ItemId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteItemHandlerImpl implements DeleteItemHandler {
    private final DeleteInventoryPort deletePort;

    @Override
    public void deleteById(ItemId id) {
        deletePort.deleteById(id);
    }
}
