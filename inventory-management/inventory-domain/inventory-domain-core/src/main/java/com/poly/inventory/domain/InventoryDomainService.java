package com.poly.inventory.domain;

public interface InventoryDomainService {
    // Định nghĩa các method xử lý nghiệp vụ tổng hợp, ví dụ:
    void transferQuantity(String fromItemId, String toItemId, int quantity);
}
