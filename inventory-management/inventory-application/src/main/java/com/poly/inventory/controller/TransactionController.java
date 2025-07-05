package com.poly.inventory.controller;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.application.port.in.TransactionUseCase;
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
@Tag(name = "Transaction Controller")
@Validated
public class TransactionController {

    private final TransactionUseCase useCase;

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(useCase.getAllTransactions());
    }

    @PostMapping("/in")
    public ResponseEntity<TransactionDto> stockIn(@RequestBody @Valid TransactionDto dto) {
        TransactionDto result = useCase.stockIn(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/out")
    public ResponseEntity<TransactionDto> stockOut(@RequestBody @Valid TransactionDto dto) {
        TransactionDto result = useCase.stockOut(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/check")
    public ResponseEntity<TransactionDto> inventoryCheck(@RequestBody @Valid TransactionDto dto) {
        TransactionDto result = useCase.inventoryCheck(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/out/room")
    public ResponseEntity<TransactionDto> stockOutToRoom(@RequestParam String roomNumber,
                                                         @RequestBody @Valid TransactionDto dto) {
        dto.setNote("Xuất cho phòng " + roomNumber + (dto.getNote() != null ? " - " + dto.getNote() : ""));
        // String role = authenticationService.getCurrentUserRole();
        TransactionDto result = useCase.stockOut(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/report")
    public ResponseEntity<List<TransactionDto>> getReport(@RequestParam String from,
                                                          @RequestParam String to) {
        List<TransactionDto> report = useCase.getReport(LocalDate.parse(from), LocalDate.parse(to));
        return ResponseEntity.ok(report);
    }

    @GetMapping("/receipt")
    public ResponseEntity<String> exportReceipt(@RequestParam int index) {
        String receipt = useCase.exportReceipt(index);
        return receipt != null ? ResponseEntity.ok(receipt) : ResponseEntity.notFound().build();
    }
}
