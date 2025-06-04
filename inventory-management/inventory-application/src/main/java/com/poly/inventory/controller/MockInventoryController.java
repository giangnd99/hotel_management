package com.poly.inventory.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mock/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Mock Inventory Controller")
@Validated
public class MockInventoryController {

    @Data
    static class ItemDTO {
        private String itemCode;
        private String name;
        private String unit; // cái, bộ, chai
        private int quantity; // tồn kho hiện tại

        public ItemDTO(String itemCode, String name, String unit, int quantity) {
        }
    }

    @Data
    static class StockTransactionDTO {
        private String itemCode;
        private String type; // IN | OUT
        private int quantity;
        private LocalDateTime timestamp;
        private String note;

        public StockTransactionDTO(String itemCode, String in, int quantity, LocalDateTime now, String s) {
        }
    }

    private static final Map<String, ItemDTO> WAREHOUSE = new HashMap<>();
    private static final List<StockTransactionDTO> TRANSACTIONS = new ArrayList<>();

    @PostConstruct
    public void initWarehouseData() {
        WAREHOUSE.put("TOWEL", new ItemDTO("TOWEL", "Khăn tắm", "cái", 100));
        WAREHOUSE.put("SHAMPOO", new ItemDTO("SHAMPOO", "Dầu gội", "chai", 200));
        WAREHOUSE.put("WATER", new ItemDTO("WATER", "Nước suối", "chai", 150));
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return ResponseEntity.ok(new ArrayList<>(WAREHOUSE.values()));
    }

    @PostMapping("/items")
    public ResponseEntity<String> addItem(@RequestBody ItemDTO item) {
        if (WAREHOUSE.containsKey(item.getItemCode()))
            return ResponseEntity.badRequest().body("Item already exists");
        WAREHOUSE.put(item.getItemCode(), item);
        return ResponseEntity.ok("Item added.");
    }

    @PostMapping("/stock-in")
    public ResponseEntity<String> stockIn(@RequestParam String itemCode,
                                          @RequestParam int quantity,
                                          @RequestParam(required = false) String note) {
        ItemDTO item = WAREHOUSE.get(itemCode);
        if (item == null) return ResponseEntity.notFound().build();

        item.setQuantity(item.getQuantity() + quantity);

        TRANSACTIONS.add(new StockTransactionDTO(itemCode, "IN", quantity,
                LocalDateTime.now(), note != null ? note : "Nhập kho"));

        return ResponseEntity.ok("Stock-in successful.");
    }

    @PostMapping("/stock-out")
    public ResponseEntity<String> stockOut(@RequestParam String itemCode,
                                           @RequestParam int quantity,
                                           @RequestParam(required = false) String note) {
        ItemDTO item = WAREHOUSE.get(itemCode);
        if (item == null) return ResponseEntity.notFound().build();

        if (item.getQuantity() < quantity)
            return ResponseEntity.badRequest().body("Not enough stock");

        item.setQuantity(item.getQuantity() - quantity);

        TRANSACTIONS.add(new StockTransactionDTO(itemCode, "OUT", quantity,
                LocalDateTime.now(), note != null ? note : "Xuất kho"));

        return ResponseEntity.ok("Stock-out successful.");
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<StockTransactionDTO>> getTransactions() {
        return ResponseEntity.ok(TRANSACTIONS);
    }

    @GetMapping("/alerts/low-stock")
    public ResponseEntity<List<ItemDTO>> lowStockAlert(@RequestParam(defaultValue = "20") int threshold) {
        List<ItemDTO> lowStockItems = WAREHOUSE.values().stream()
                .filter(item -> item.getQuantity() < threshold)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lowStockItems);
    }

    @PutMapping("/items/{itemCode}")
    public ResponseEntity<String> updateItem(@PathVariable String itemCode, @RequestBody ItemDTO updatedItem) {
        ItemDTO existing = WAREHOUSE.get(itemCode);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setName(updatedItem.getName());
        existing.setUnit(updatedItem.getUnit());
        return ResponseEntity.ok("Item updated");
    }

    @DeleteMapping("/items/{itemCode}")
    public ResponseEntity<String> deleteItem(@PathVariable String itemCode) {
        if (!WAREHOUSE.containsKey(itemCode)) return ResponseEntity.notFound().build();
        WAREHOUSE.remove(itemCode);
        return ResponseEntity.ok("Item deleted");
    }

    @GetMapping("/items/{itemCode}")
    public ResponseEntity<ItemDTO> getItem(@PathVariable String itemCode) {
        ItemDTO item = WAREHOUSE.get(itemCode);
        return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
    }

    @PostMapping("/check")
    public ResponseEntity<String> inventoryCheck(@RequestParam String itemCode,
                                                 @RequestParam int actualQuantity,
                                                 @RequestParam(required = false) String note) {
        ItemDTO item = WAREHOUSE.get(itemCode);
        if (item == null) return ResponseEntity.notFound().build();
        int systemQuantity = item.getQuantity();
        item.setQuantity(actualQuantity);
        TRANSACTIONS.add(new StockTransactionDTO(itemCode, "CHECK", actualQuantity - systemQuantity,
                LocalDateTime.now(), "Kiểm kê: " + (note != null ? note : "")));
        return ResponseEntity.ok("Inventory checked. Updated from " + systemQuantity + " to " + actualQuantity);
    }

    @GetMapping("/items/search")
    public ResponseEntity<List<ItemDTO>> searchItems(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String unit) {
        List<ItemDTO> result = WAREHOUSE.values().stream()
                .filter(i -> (name == null || i.getName().toLowerCase().contains(name.toLowerCase())) &&
                        (unit == null || i.getUnit().equalsIgnoreCase(unit)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/stock-out/room")
    public ResponseEntity<String> stockOutToRoom(@RequestParam String itemCode,
                                                 @RequestParam String roomNumber,
                                                 @RequestParam int quantity,
                                                 @RequestParam(required = false) String note) {
        ItemDTO item = WAREHOUSE.get(itemCode);
        if (item == null) return ResponseEntity.notFound().build();
        if (item.getQuantity() < quantity)
            return ResponseEntity.badRequest().body("Not enough stock");
        item.setQuantity(item.getQuantity() - quantity);
        TRANSACTIONS.add(new StockTransactionDTO(itemCode, "OUT", quantity,
                LocalDateTime.now(), "Xuất cho phòng " + roomNumber + " - " + (note != null ? note : "")));
        return ResponseEntity.ok("Stock-out to room " + roomNumber + " successful.");
    }

    @GetMapping("/report")
    public ResponseEntity<List<StockTransactionDTO>> report(@RequestParam String from,
                                                            @RequestParam String to) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        List<StockTransactionDTO> result = TRANSACTIONS.stream()
                .filter(t -> {
                    LocalDate date = t.getTimestamp().toLocalDate();
                    return !date.isBefore(fromDate) && !date.isAfter(toDate);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/export-receipt")
    public ResponseEntity<String> exportReceipt(@RequestParam int index) {
        if (index < 0 || index >= TRANSACTIONS.size()) return ResponseEntity.notFound().build();
        StockTransactionDTO tx = TRANSACTIONS.get(index);
        String receipt = """
                ===== RECEIPT =====
                Type      : %s
                Item      : %s
                Quantity  : %d
                Time      : %s
                Note      : %s
                """.formatted(tx.getType(), tx.getItemCode(), tx.getQuantity(), tx.getTimestamp(), tx.getNote());
        return ResponseEntity.ok(receipt);
    }
}
