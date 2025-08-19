package com.poly.inventory.controller;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.application.port.in.TransactionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Controller", description = "Quản lý các giao dịch kho: nhập, xuất, kiểm kê, báo cáo...")
@Validated
public class TransactionController {

    private final TransactionUseCase useCase; // handler pattern

    @Operation(summary = "Lấy tất cả giao dịch", description = "Trả về danh sách toàn bộ giao dịch trong hệ thống.")
    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(useCase.getAllTransactions());
    }

    @Operation(summary = "Nhập kho", description = "Tạo giao dịch nhập kho với thông tin chi tiết từ DTO.")
    @PostMapping("/in")
    public ResponseEntity<TransactionDto> stockIn(@RequestBody @Valid TransactionDto dto) {
        TransactionDto result = useCase.stockIn(dto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Xuất kho", description = "Tạo giao dịch xuất kho với thông tin chi tiết từ DTO.")
    @PostMapping("/out")
    public ResponseEntity<TransactionDto> stockOut(@RequestBody @Valid TransactionDto dto) {
        TransactionDto result = useCase.stockOut(dto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Kiểm kê kho", description = "Tạo giao dịch kiểm kê số lượng tồn kho hiện tại.")
    @PostMapping("/check")
    public ResponseEntity<TransactionDto> inventoryCheck(@RequestBody @Valid TransactionDto dto) {
        TransactionDto result = useCase.inventoryCheck(dto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Xuất kho cho phòng", description = "Tạo giao dịch xuất kho trực tiếp cho một phòng cụ thể, dựa vào số phòng truyền vào.")
    @PostMapping("/out/room")
    public ResponseEntity<TransactionDto> stockOutToRoom(@RequestParam String roomNumber,
                                                         @RequestBody @Valid TransactionDto dto) {
        dto.setNote("Xuất cho phòng " + roomNumber + (dto.getNote() != null ? " - " + dto.getNote() : ""));
        TransactionDto result = useCase.stockOut(dto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Báo cáo giao dịch", description = "Lấy báo cáo các giao dịch trong khoảng thời gian (from - to).")
    @GetMapping("/report")
    public ResponseEntity<List<TransactionDto>> getReport(@RequestParam String from,
                                                          @RequestParam String to) {
        List<TransactionDto> report = useCase.getReport(LocalDate.parse(from), LocalDate.parse(to));
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Xuất hoá đơn", description = "Xuất hoá đơn giao dịch dựa vào chỉ số index trong danh sách giao dịch.")
    @GetMapping("/receipt")
    public ResponseEntity<String> exportReceipt(@RequestParam int index) {
        String receipt = useCase.exportReceipt(index);
        return receipt != null ? ResponseEntity.ok(receipt) : ResponseEntity.notFound().build();
    }
}
