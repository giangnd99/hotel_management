//package com.poly.inventory.application.handler.impl;
//
//import com.poly.inventory.application.dto.TransactionDto;
//import com.poly.inventory.application.port.out.LoadTransactionPort;
//import com.poly.inventory.domain.entity.InventoryTransaction;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.times;
//
//
//class GetReportHandlerImplTest {
//
//    private LoadTransactionPort loadTransactionPort;
//    private GetReportHandlerImpl handler;
//
//    @BeforeEach
//    void setUp() {
//        loadTransactionPort = mock(LoadTransactionPort.class);
//        handler = new GetReportHandlerImpl(loadTransactionPort);
//    }
//
//    @Test
//    void handle_shouldReturnMappedTransactionDtos() {
//        // Arrange
//        LocalDate from = LocalDate.of(2024, 1, 1);
//        LocalDate to = LocalDate.of(2024, 1, 31);
//
//        InventoryTransaction tx1 = mock(InventoryTransaction.class);
//        InventoryTransaction tx2 = mock(InventoryTransaction.class);
//
//        when(loadTransactionPort.findByDateRange(from, to)).thenReturn(List.of(tx1, tx2));
//
//        // Act
//        List<TransactionDto> result = handler.handle(from, to);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        verify(loadTransactionPort, times(1)).findByDateRange(from, to);
//    }
//}