package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.application.port.out.SaveTransactionPort;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.entity.InventoryTransaction;
import com.poly.inventory.domain.value_object.ItemId;
import com.poly.inventory.domain.value_object.Quantity;
import com.poly.inventory.domain.value_object.TransactionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockOutHandlerImplTest {

    private SaveTransactionPort saveTransactionPort;
    private LoadInventoryPort loadInventoryPort;
    private SaveInventoryPort saveInventoryPort;
    private StockOutHandlerImpl stockOutHandler;

    @BeforeEach
    void setUp() {
        saveTransactionPort = mock(SaveTransactionPort.class);
        loadInventoryPort = mock(LoadInventoryPort.class);
        saveInventoryPort = mock(SaveInventoryPort.class);
        stockOutHandler = new StockOutHandlerImpl(saveTransactionPort, loadInventoryPort, saveInventoryPort);
    }

    @Test
    void handle_shouldDecreaseQuantityAndSaveTransaction() {
        // Arrange
        ItemId itemId = new ItemId(1);
        Quantity initialQuantity = new Quantity(10);
        Quantity outQuantity = new Quantity(3);
        InventoryItem item = new InventoryItem(itemId, "itemName", "category", initialQuantity, 100.0, 1);
        TransactionDto dto = new TransactionDto();
        dto.setItemId(itemId.getValue());
        dto.setQuantity(outQuantity.getValue());
        dto.setStaffId(5);

        when(loadInventoryPort.loadItemById(itemId.getValue())).thenReturn(Optional.of(item));
        when(saveTransactionPort.save(any())).thenReturn(
                new InventoryTransaction(
                        new TransactionId(100),
                        itemId,
                        5,
                        "OUT",
                        outQuantity,
                        LocalDateTime.now()
                )
        );

        // Act
        TransactionDto result = stockOutHandler.handle(dto);

        // Assert
        assertEquals("OUT", result.getTransactionType());
        assertEquals(itemId.getValue(), result.getItemId());
        assertEquals(100, result.getTransactionId());
        assertNotNull(result.getTransactionDate());
        assertEquals(initialQuantity.subtract(outQuantity).getValue(), item.getQuantity().getValue());

        verify(saveInventoryPort, times(1)).save(item);
        verify(saveTransactionPort, times(1)).save(any());
    }

    @Test
    void handle_shouldThrowException_whenNotEnoughStock() {
        // Arrange
        ItemId itemId = new ItemId(2);
        Quantity initialQuantity = new Quantity(2);
        TransactionDto dto = new TransactionDto();
        dto.setItemId(itemId.getValue());
        dto.setQuantity(5);

        InventoryItem item = new InventoryItem(itemId, "itemName", "category", initialQuantity, 100.0, 1);
        when(loadInventoryPort.loadItemById(itemId.getValue())).thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> stockOutHandler.handle(dto));
        verify(saveInventoryPort, never()).save(any());
        verify(saveTransactionPort, never()).save(any());
    }
}