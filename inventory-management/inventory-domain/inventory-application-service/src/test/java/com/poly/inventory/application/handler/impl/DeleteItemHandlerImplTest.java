//package com.poly.inventory.application.handler.impl;
//
//import com.poly.inventory.application.port.out.DeleteInventoryPort;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.mockito.Mockito.*;
//
//class DeleteItemHandlerImplTest {
//
//    private DeleteInventoryPort deletePort;
//    private DeleteItemHandlerImpl handler;
//
//    @BeforeEach
//    void setUp() {
//        deletePort = mock(DeleteInventoryPort.class);
//        handler = new DeleteItemHandlerImpl(deletePort);
//    }
//
//    @Test
//    void deleteById_shouldCallDeletePortWithCorrectId() {
//        // Arrange
//        Integer itemId = 1;
//
//        // Act
//        handler.deleteById(itemId);
//
//        // Assert
//        verify(deletePort, times(1)).deleteById(itemId);
//    }
//}