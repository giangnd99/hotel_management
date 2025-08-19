//package com.poly.inventory.application.handler.impl;
//
//import com.poly.inventory.application.dto.TransactionDto;
//import com.poly.inventory.application.port.out.LoadInventoryPort;
//import com.poly.inventory.application.port.out.SaveInventoryPort;
//import com.poly.inventory.application.port.out.SaveTransactionPort;
//import com.poly.inventory.domain.entity.InventoryItem;
//import com.poly.inventory.domain.entity.InventoryTransaction;
//import com.poly.inventory.domain.value_object.ItemId;
//import com.poly.inventory.domain.value_object.Quantity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class InventoryCheckHandlerImplTest {
//
//    private SaveTransactionPort saveTransactionPort;
//    private LoadInventoryPort loadInventoryPort;
//    private SaveInventoryPort saveInventoryPort;
//    private InventoryCheckHandlerImpl handler;
//
//    @BeforeEach
//    void setUp() {
//        saveTransactionPort = mock(SaveTransactionPort.class);
//        loadInventoryPort = mock(LoadInventoryPort.class);
//        saveInventoryPort = mock(SaveInventoryPort.class);
//        handler = new InventoryCheckHandlerImpl(saveTransactionPort, loadInventoryPort, saveInventoryPort);
//    }
//
//    @Test
//    void handle_shouldUpdateQuantityAndSaveTransaction() {
//        // Arrange
//        Integer itemId = 1;
//        InventoryItem item = new InventoryItem(new ItemId(itemId), "Item1", "Category1", new Quantity(5), 100.0, 1);
//        TransactionDto dto = new TransactionDto();
//        dto.setItemId(itemId);
//        dto.setQuantity(10);
//
//        when(loadInventoryPort.loadItemById(itemId)).thenReturn(Optional.of(item));
//        when(saveTransactionPort.save(any())).thenReturn(mock(InventoryTransaction.class));
//
//        // Act
//        TransactionDto result = handler.handle(dto);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("CHECK", result.getTransactionType());
//        assertNotNull(result.getTransactionDate());
//        verify(loadInventoryPort, times(1)).loadItemById(itemId);
//        verify(saveInventoryPort, times(1)).save(item);
//        verify(saveTransactionPort, times(1)).save(any());
//        assertEquals(10, item.getQuantity().getValue());
//    }
//
//    @Test
//    void handle_shouldThrowException_whenItemNotFound() {
//        // Arrange
//        Integer itemId = 2;
//        TransactionDto dto = new TransactionDto();
//        dto.setItemId(itemId);
//        dto.setQuantity(10);
//
//        when(loadInventoryPort.loadItemById(itemId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(RuntimeException.class, () -> handler.handle(dto));
//        verify(loadInventoryPort, times(1)).loadItemById(itemId);
//        verifyNoMoreInteractions(saveInventoryPort, saveTransactionPort);
//    }
//}