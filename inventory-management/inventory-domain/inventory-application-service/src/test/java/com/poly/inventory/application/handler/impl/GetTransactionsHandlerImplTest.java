//package com.poly.inventory.application.handler.impl;
//
//import com.poly.inventory.application.dto.TransactionDto;
//import com.poly.inventory.application.port.out.LoadTransactionPort;
//import com.poly.inventory.domain.entity.InventoryTransaction;
//import com.poly.inventory.domain.value_object.ItemId;
//import com.poly.inventory.domain.value_object.Quantity;
//import com.poly.inventory.domain.value_object.TransactionId;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class GetTransactionsHandlerImplTest {
//
//    private LoadTransactionPort loadTransactionPort;
//    private GetTransactionsHandlerImpl handler;
//
//    @BeforeEach
//    void setUp() {
//        loadTransactionPort = mock(LoadTransactionPort.class);
//        handler = new GetTransactionsHandlerImpl(loadTransactionPort);
//    }
//
//    @Test
//    void handle_shouldReturnMappedTransactionDtos() {
//        // Arrange
//        InventoryTransaction tx1 = new InventoryTransaction(
//                new TransactionId(1),
//                new ItemId(10),
//                100,
//                "IN",
//                new Quantity(5),
//                LocalDateTime.now()
//        );
//        InventoryTransaction tx2 = new InventoryTransaction(
//                new TransactionId(2),
//                new ItemId(20),
//                200,
//                "OUT",
//                new Quantity(3),
//                LocalDateTime.now()
//        );
//        when(loadTransactionPort.findAll()).thenReturn(List.of(tx1, tx2));
//
//        // Act
//        List<TransactionDto> result = handler.handle();
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals(tx1.getTransactionId().getValue(), result.get(0).getTransactionId());
//        assertEquals(tx2.getTransactionId().getValue(), result.get(1).getTransactionId());
//        assertEquals(tx1.getTransactionType(), result.get(0).getTransactionType());
//        assertEquals(tx2.getTransactionType(), result.get(1).getTransactionType());
//        verify(loadTransactionPort, times(1)).findAll();
//    }
//}