package com.poly.staff.controller;

import com.poly.staff.model.PayrollModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@RestController
@RequestMapping("/payrolls")
public class PayrollController {

    @GetMapping
    public ResponseEntity<List<PayrollModel>> getAll() {
        List<PayrollModel> list = Arrays.asList(
            PayrollModel.builder()
                .payrollId(1)
                .staffId("S001")
                .totalSalary(2000.0)
                .payrollMonth(YearMonth.now())
                .createdAt(LocalDateTime.now())
                .paymentDate(LocalDate.now())
                .build(),
            PayrollModel.builder()
                .payrollId(2)
                .staffId("S002")
                .totalSalary(2500.0)
                .payrollMonth(YearMonth.now())
                .createdAt(LocalDateTime.now())
                .paymentDate(LocalDate.now())
                .build()
        );
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayrollModel> getById(@PathVariable Integer id) {
        PayrollModel model = PayrollModel.builder()
            .payrollId(id)
            .staffId("S00" + id)
            .totalSalary(2000.0 + id * 100)
            .payrollMonth(YearMonth.now())
            .createdAt(LocalDateTime.now())
            .paymentDate(LocalDate.now())
            .build();
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<PayrollModel> create(@RequestBody PayrollModel model) {
        PayrollModel created = PayrollModel.builder()
            .payrollId(100)
            .staffId(model.getStaffId())
            .totalSalary(model.getTotalSalary())
            .payrollMonth(model.getPayrollMonth())
            .createdAt(LocalDateTime.now())
            .paymentDate(LocalDate.now())
            .build();
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayrollModel> update(@PathVariable Integer id, @RequestBody PayrollModel model) {
        PayrollModel updated = PayrollModel.builder()
            .payrollId(id)
            .staffId(model.getStaffId())
            .totalSalary(model.getTotalSalary())
            .payrollMonth(model.getPayrollMonth())
            .createdAt(LocalDateTime.now())
            .paymentDate(LocalDate.now())
            .build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }
}
