package com.poly.inventory.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryStatisticsDto {

    /** Tổng số lượng loại vật tư (số item) */
    private Long totalItems;

    /** Số lượng item còn hàng (quantity > 0) */
    private Long inStockItems;

    /** Số lượng item đã hết hàng (quantity = 0) */
    private Long outOfStockItems;

    /** Số lượng item sắp hết hàng (quantity < threshold, ví dụ 5) */
    private Long lowStockItems;

    /** Tổng giá trị tồn kho (tính = sum(quantity * price)) */
    private Double totalStockValue;

    /** Số danh mục (categories) hiện có */
    private Long totalCategories;

    /** Số nhà cung cấp (suppliers) hiện có */
    private Long totalSuppliers;
}
