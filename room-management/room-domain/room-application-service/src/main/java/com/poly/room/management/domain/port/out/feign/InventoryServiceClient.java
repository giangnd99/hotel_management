package com.poly.room.management.domain.port.out.feign;

import com.poly.room.management.domain.dto.response.ItemDTO;
import com.poly.room.management.domain.dto.response.StockTransactionDTO;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "inventory-service", url = "localhost:8081/mock/api/inventory")
public interface InventoryServiceClient {

    @GetMapping("/items")
    ResponseEntity<List<ItemDTO>> getAllItems();

    @PostMapping("/items")
    ResponseEntity<String> addItem(@RequestBody ItemDTO item);

    @PostMapping("/stock-in")
    ResponseEntity<String> stockIn(@RequestParam String itemCode,
                                   @RequestParam int quantity,
                                   @RequestParam(required = false) String note);

    @PostMapping("/stock-out")
    ResponseEntity<String> stockOut(@RequestParam String itemCode,
                                    @RequestParam int quantity,
                                    @RequestParam(required = false) String note);

    @GetMapping("/transactions")
    ResponseEntity<List<StockTransactionDTO>> getTransactions();

    @GetMapping("/alerts/low-stock")
    ResponseEntity<List<ItemDTO>> lowStockAlert(@RequestParam(defaultValue = "20") int threshold);

    @PutMapping("/items/{itemCode}")
    ResponseEntity<String> updateItem(@PathVariable String itemCode, @RequestBody ItemDTO updatedItem);

    @DeleteMapping("/items/{itemCode}")
    ResponseEntity<String> deleteItem(@PathVariable String itemCode);

    @GetMapping("/items/{itemCode}")
    ResponseEntity<ItemDTO> getItem(@PathVariable String itemCode);

    @PostMapping("/check")
    ResponseEntity<String> inventoryCheck(@RequestParam String itemCode,
                                          @RequestParam int actualQuantity,
                                          @RequestParam(required = false) String note);

    @GetMapping("/items/search")
    ResponseEntity<List<ItemDTO>> searchItems(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) String unit);

    @PostMapping("/stock-out/room")
    ResponseEntity<String> stockOutToRoom(@RequestParam String itemCode,
                                          @RequestParam String roomNumber,
                                          @RequestParam int quantity,
                                          @RequestParam(required = false) String note);

    @GetMapping("/report")
    ResponseEntity<List<StockTransactionDTO>> report(@RequestParam String from,
                                                     @RequestParam String to);

    @GetMapping("/export-receipt")
    ResponseEntity<String> exportReceipt(@RequestParam int index);
}