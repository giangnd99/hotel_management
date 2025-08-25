//package com.poly.inventory.application.handler.impl;
//
//import com.poly.inventory.application.dto.InventoryItemDto;
//import com.poly.inventory.application.port.out.LoadInventoryPort;
//import com.poly.inventory.domain.entity.InventoryItem;
//import com.poly.inventory.domain.value_object.ItemId;
//import com.poly.inventory.domain.value_object.Quantity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class GetItemByIdHandlerImplTest {
//
//    private LoadInventoryPort loadInventoryPort;
//    private GetItemByIdHandlerImpl handler;
//
//    @BeforeEach
//    void setUp() {
//        loadInventoryPort = mock(LoadInventoryPort.class);
//        handler = new GetItemByIdHandlerImpl(loadInventoryPort);
//    }
//
//    @Test
//    void getItemById_shouldReturnMappedDto_whenItemExists() {
//        // Arrange
//        Integer itemId = 1;
//        InventoryItem item = new InventoryItem(new ItemId(itemId), "Item1", "Category1", new Quantity(10), 100.0, 1);
//        when(loadInventoryPort.loadItemById(itemId)).thenReturn(Optional.of(item));
//
//        // Act
//        Optional<InventoryItemDto> result = handler.getItemById(itemId);
//
//        // Assert
//        assertTrue(result.isPresent());
//        assertEquals(itemId, result.get().getItemId());
//        assertEquals(item.getItemName(), result.get().getItemName());
//        verify(loadInventoryPort, times(1)).loadItemById(itemId);
//    }
//
//    @Test
//    void getItemById_shouldReturnEmpty_whenItemDoesNotExist() {
//        // Arrange
//        Integer itemId = 2;
//        when(loadInventoryPort.loadItemById(itemId)).thenReturn(Optional.empty());
//
//        // Act
//        Optional<InventoryItemDto> result = handler.getItemById(itemId);
//
//        // Assert
//        assertFalse(result.isPresent());
//        verify(loadInventoryPort, times(1)).loadItemById(itemId);
//    }
//}