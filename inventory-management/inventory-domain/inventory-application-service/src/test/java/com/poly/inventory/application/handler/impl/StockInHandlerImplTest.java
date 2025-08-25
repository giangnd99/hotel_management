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
//import com.poly.inventory.domain.value_object.TransactionId;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class StockInHandlerImplTest {
//    private static final ItemId ITEM_ID = new ItemId(1);
//    private static final Quantity INITIAL_QUANTITY = new Quantity(2);
//    private static final Quantity ADDED_QUANTITY = new Quantity(2);
//    private static final String ITEM_NAME = "itemName";
//    private static final String ITEM_CATEGORY = "Tiêu hao";
//    private static final double UNIT_PRICE = 100.0;
//    private static final Quantity MIN_QUANTITY = new Quantity(1);
//    private static final String TRANSACTION_TYPE_IN = "IN";
//    private static final TransactionId TRANSACTION_ID = new TransactionId(42);
//
//    private SaveTransactionPort saveTransactionPort;
//    private LoadInventoryPort loadInventoryPort;
//    private SaveInventoryPort saveInventoryPort;
//    private StockInHandlerImpl stockInHandler;
//
//    @BeforeEach
//    void setUp() {
//        saveTransactionPort = mock(SaveTransactionPort.class);
//        loadInventoryPort = mock(LoadInventoryPort.class);
//        saveInventoryPort = mock(SaveInventoryPort.class);
//        stockInHandler = new StockInHandlerImpl(saveTransactionPort, loadInventoryPort, saveInventoryPort);
//    }
//
//    @Test
//    void handle_shouldIncreaseQuantityAndSaveTransaction() {
//        // Arrange
//        ItemId itemId = ItemId.from(ITEM_ID.getValue());
//        InventoryItem inventoryItem = createInventoryItem(itemId);
//        TransactionDto inputDto = createTransactionDto(itemId);
//
//        when(saveTransactionPort.save(any()))
//                .thenReturn(new InventoryTransaction(
//                        TRANSACTION_ID,
//                        ITEM_ID,
//                        23, // ví dụ staffId
//                        TRANSACTION_TYPE_IN,
//                        ADDED_QUANTITY,
//                        LocalDateTime.now()
//                ));
//
//        setupMocks(itemId, inventoryItem);
//
//        // Act
//        TransactionDto resultDto = stockInHandler.handle(inputDto);
//
//        // Assert
//        assertEquals(TRANSACTION_ID.getValue(), resultDto.getTransactionId());
//        assertEquals("IN", resultDto.getTransactionType());
//        assertEquals(ITEM_ID.getValue(), resultDto.getItemId());
//    }
//
//    private InventoryItem createInventoryItem(ItemId itemId) {
//        return new InventoryItem(itemId, ITEM_NAME, ITEM_CATEGORY,
//                INITIAL_QUANTITY, UNIT_PRICE, MIN_QUANTITY.value());
//    }
//
//    private TransactionDto createTransactionDto(ItemId itemId) {
//        TransactionDto dto = new TransactionDto();
//        dto.setItemId(itemId.getValue());
//        dto.setQuantity(ADDED_QUANTITY.value());
//        dto.setStaffId(23);
//        return dto;
//    }
//
//
//    private void setupMocks(ItemId itemId, InventoryItem item) {
//        when(loadInventoryPort.loadItemById(itemId.getValue()))
//                .thenReturn(Optional.of(item));
//    }
//
//    private void assertTransactionDetails(TransactionDto result, ItemId expectedItemId) {
//        assertEquals(TRANSACTION_TYPE_IN, result.getTransactionType(),
//                "Transaction type should be IN");
//        assertNotNull(result.getTransactionDate(),
//                "Transaction date should not be null");
//        assertEquals(expectedItemId.getValue(), result.getItemId(),
//                "Item ID should match");
//    }
//
//    private void assertInventoryQuantity(InventoryItem item, int expectedQuantity) {
//        assertEquals(expectedQuantity, item.getQuantity().getValue(),
//                "Inventory quantity should be updated correctly");
//    }
//
//    private void verifyPortInteractions(InventoryItem item) {
//        verify(saveInventoryPort).save(item);
//        verify(saveTransactionPort).save(any());
//    }
//}