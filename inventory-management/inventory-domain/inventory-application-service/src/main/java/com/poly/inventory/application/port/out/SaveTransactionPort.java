package com.poly.inventory.application.port.out;

import com.poly.inventory.domain.entity.InventoryTransaction;

public interface SaveTransactionPort {
    InventoryTransaction save(InventoryTransaction transaction);
}
