package com.poly.restaurant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock/api/restaurant")
@RequiredArgsConstructor
@Tag(name = "Mock Restaurant Controller", description = "Giả lập các API của nhà hàng")
public class MockRestaurantController {

    @Operation(summary = "Lấy danh sách bàn ăn", description = "Trả về danh sách các bàn với trạng thái hiện tại")
    @ApiResponse(responseCode = "200", description = "Danh sách bàn ăn", content = @Content(mediaType = "application/json"))
    @GetMapping("/tables")
    public ResponseEntity<List<Map<String, Object>>> getTables() {
        List<Map<String, Object>> tables = List.of(
                Map.of("id", "T001", "name", "Bàn 1", "status", "AVAILABLE"),
                Map.of("id", "T002", "name", "Bàn 2", "status", "OCCUPIED"),
                Map.of("id", "T003", "name", "Bàn 3", "status", "RESERVED")
        );
        return ResponseEntity.ok(tables);
    }

    @Operation(summary = "Lấy danh sách món ăn", description = "Trả về menu món ăn của nhà hàng")
    @ApiResponse(responseCode = "200", description = "Danh sách món ăn", content = @Content(mediaType = "application/json"))
    @GetMapping("/menu")
    public ResponseEntity<List<Map<String, Object>>> getMenu() {
        List<Map<String, Object>> menu = List.of(
                Map.of("id", "M001", "name", "Phở bò", "price", 85000, "category", "Món chính"),
                Map.of("id", "M002", "name", "Trà đá", "price", 35000, "category", "Đồ uống"),
                Map.of("id", "M003", "name", "Cà phê sữa", "price", 45000, "category", "Đồ uống")
        );
        return ResponseEntity.ok(menu);
    }

    @Operation(summary = "Tạo đơn hàng", description = "Tạo đơn hàng mới dựa trên dữ liệu gửi lên")
    @ApiResponse(responseCode = "200", description = "Đơn hàng đã tạo", content = @Content(mediaType = "application/json"))
    @PostMapping("/order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> orderRequest) {
        Map<String, Object> mockResponse = Map.of(
                "orderId", "O999",
                "status", "CREATED",
                "timestamp", LocalDateTime.now().toString(),
                "details", orderRequest
        );
        return ResponseEntity.ok(mockResponse);
    }

    @Operation(summary = "Lấy danh sách đơn hàng", description = "Trả về các đơn hàng đã đặt")
    @ApiResponse(responseCode = "200", description = "Danh sách đơn hàng", content = @Content(mediaType = "application/json"))
    @GetMapping("/orders")
    public ResponseEntity<List<Map<String, Object>>> getOrders() {
        List<Map<String, Object>> orders = List.of(
                Map.of(
                        "orderId", "O001",
                        "tableId", "T001",
                        "items", List.of(
                                Map.of("menuId", "M001", "quantity", 2),
                                Map.of("menuId", "M002", "quantity", 2)
                        ),
                        "status", "COMPLETED"
                ),
                Map.of(
                        "orderId", "O002",
                        "tableId", "T002",
                        "items", List.of(
                                Map.of("menuId", "M003", "quantity", 1)
                        ),
                        "status", "IN_PROGRESS"
                )
        );
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Lấy danh sách nhân viên", description = "Danh sách nhân viên trong nhà hàng")
    @ApiResponse(responseCode = "200", description = "Danh sách nhân viên", content = @Content(mediaType = "application/json"))
    @GetMapping("/staff")
    public ResponseEntity<List<Map<String, Object>>> getStaff() {
        List<Map<String, Object>> staff = List.of(
                Map.of("id", 201, "name", "Nguyễn Văn A", "role", "Phục vụ"),
                Map.of("id", 202, "name", "Trần Thị B", "role", "Thu ngân"),
                Map.of("id", 203, "name", "Lê Văn C", "role", "Bếp")
        );
        return ResponseEntity.ok(staff);
    }
}
