//package com.poly.inventory.application.handler.impl;
//
//import com.poly.inventory.application.dto.InventoryItemDto;
//import com.poly.inventory.application.port.out.SaveInventoryPort;
//import com.poly.inventory.domain.entity.InventoryItem;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CreateItemHandlerImplTest {
//
//    private SaveInventoryPort savePort;
//    private CreateItemHandlerImpl handler;
//
//    @BeforeEach
//    void setUp() {
//        savePort = mock(SaveInventoryPort.class);
//        handler = new CreateItemHandlerImpl(savePort);
//    }
//
//    @Test
//    void create_shouldSaveAndReturnDto() {
//        // Arrange
//        InventoryItemDto inputDto = new InventoryItemDto();
//        inputDto.setItemId(1);
//        inputDto.setItemName("Test Item");
//        inputDto.setQuantity(10);
//
//        // Mock savePort to do nothing (void)
//        doNothing().when(savePort).save(any(InventoryItem.class));
//
//        // Act
//        InventoryItemDto result = handler.create(inputDto);
//
//        // Assert
//        verify(savePort, times(1)).save(any(InventoryItem.class));
//        assertEquals(inputDto.getItemId(), result.getItemId());
//        assertEquals(inputDto.getItemName(), result.getItemName());
//        assertEquals(inputDto.getQuantity(), result.getQuantity());
//    }
//}