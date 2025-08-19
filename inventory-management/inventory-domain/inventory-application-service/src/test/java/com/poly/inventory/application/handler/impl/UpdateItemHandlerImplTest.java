//package com.poly.inventory.application.handler.impl;
//
//import com.poly.inventory.application.dto.InventoryItemDto;
//import com.poly.inventory.application.port.out.LoadInventoryPort;
//import com.poly.inventory.application.port.out.SaveInventoryPort;
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
//
//class UpdateItemHandlerImplTest {
//
//    private SaveInventoryPort savePort;
//    private LoadInventoryPort loadPort;
//    private UpdateItemHandlerImpl handler;
//
//    @BeforeEach
//    void setUp() {
//        savePort = mock(SaveInventoryPort.class);
//        loadPort = mock(LoadInventoryPort.class);
//        handler = new UpdateItemHandlerImpl(savePort, loadPort);
//    }
//
//    @Test
//    void update_shouldUpdateAndReturnUpdatedDto() {
//        // Arrange
//        Integer itemId = 1;
//        InventoryItem existingItem = new InventoryItem(new ItemId(itemId), "Old Name", "Category", new Quantity(10), 100.0, 1);
//        InventoryItemDto updateDto = new InventoryItemDto();
//        updateDto.setItemId(itemId);
//        updateDto.setItemName("Updated Name");
//        updateDto.setQuantity(20);
//
//        when(loadPort.loadItemById(itemId)).thenReturn(Optional.of(existingItem));
//        doNothing().when(savePort).save(any(InventoryItem.class));
//
//        // Act
//        Optional<InventoryItemDto> result = handler.update(itemId, updateDto);
//
//        // Assert
//        assertTrue(result.isPresent());
//        assertEquals(updateDto.getItemName(), result.get().getItemName());
//        assertEquals(updateDto.getQuantity(), result.get().getQuantity());
//        verify(savePort, times(1)).save(any(InventoryItem.class));
//    }
//
//    @Test
//    void update_shouldReturnEmptyOptional_whenItemNotFound() {
//        // Arrange
//        Integer itemId = 1;
//        InventoryItemDto updateDto = new InventoryItemDto();
//        updateDto.setItemId(itemId);
//        updateDto.setItemName("Updated Name");
//        updateDto.setQuantity(20);
//
//        when(loadPort.loadItemById(itemId)).thenReturn(Optional.empty());
//
//        // Act
//        Optional<InventoryItemDto> result = handler.update(itemId, updateDto);
//
//        // Assert
//        assertFalse(result.isPresent());
//        verify(savePort, never()).save(any(InventoryItem.class));
//    }
//}