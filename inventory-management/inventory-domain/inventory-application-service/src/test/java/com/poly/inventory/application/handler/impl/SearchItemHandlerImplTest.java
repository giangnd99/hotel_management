package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.ItemId;
import com.poly.inventory.domain.value_object.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchItemHandlerImplTest {

    private LoadInventoryPort loadInventoryPort;
    private SearchItemHandlerImpl handler;

    @BeforeEach
    void setUp() {
        loadInventoryPort = mock(LoadInventoryPort.class);
        handler = new SearchItemHandlerImpl(loadInventoryPort);
    }

    @Test
    void searchItemsByName_shouldReturnFilteredItems_whenNameIsProvided() {
        // Arrange
        String name = "Item1";
        InventoryItem item1 = new InventoryItem(new ItemId(1), "Item1", "Category1", new Quantity(10), 100.0, 1);
        when(loadInventoryPort.searchByName(name)).thenReturn(List.of(item1));

        // Act
        List<InventoryItemDto> result = handler.searchItemsByName(name);

        // Assert
        assertEquals(1, result.size());
        assertEquals(item1.getItemName(), result.get(0).getItemName());
        verify(loadInventoryPort, times(1)).searchByName(name);
        verify(loadInventoryPort, never()).loadAllItems();
    }

    @Test
    void searchItemsByName_shouldReturnAllItems_whenNameIsNullOrEmpty() {
        // Arrange
        InventoryItem item1 = new InventoryItem(new ItemId(1), "Item1", "Category1", new Quantity(10), 100.0, 1);
        InventoryItem item2 = new InventoryItem(new ItemId(2), "Item2", "Category2", new Quantity(5), 50.0, 1);
        when(loadInventoryPort.loadAllItems()).thenReturn(List.of(item1, item2));

        // Act
        List<InventoryItemDto> result = handler.searchItemsByName("");

        // Assert
        assertEquals(2, result.size());
        assertEquals(item1.getItemName(), result.get(0).getItemName());
        assertEquals(item2.getItemName(), result.get(1).getItemName());
        verify(loadInventoryPort, times(1)).loadAllItems();
        verify(loadInventoryPort, never()).searchByName(anyString());
    }
}