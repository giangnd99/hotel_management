package com.poly.staff.controller;

import com.poly.staff.model.StaffModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/staffs")
public class StaffController {

    @GetMapping
    public ResponseEntity<List<StaffModel>> getAll() {
        List<StaffModel> list = Arrays.asList(
            StaffModel.builder()
                .staffId("S001")
                .userId("U001")
                .departmentId(1)
                .permissions(new HashSet<>(Arrays.asList("READ", "WRITE")))
                .name("Alice")
                .email("alice@example.com")
                .phone("123456789")
                .address("123 Main St")
                .bankName("Bank A")
                .bankAccount("111111")
                .avatar("avatar1.png")
                .hireDate(LocalDate.now())
                .baseSalary(2000.0)
                .status(1)
                .build(),
            StaffModel.builder()
                .staffId("S002")
                .userId("U002")
                .departmentId(2)
                .permissions(new HashSet<>(Arrays.asList("READ")))
                .name("Bob")
                .email("bob@example.com")
                .phone("987654321")
                .address("456 Second St")
                .bankName("Bank B")
                .bankAccount("222222")
                .avatar("avatar2.png")
                .hireDate(LocalDate.now())
                .baseSalary(2500.0)
                .status(1)
                .build()
        );
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffModel> getById(@PathVariable String id) {
        StaffModel model = StaffModel.builder()
            .staffId(id)
            .userId("U" + id)
            .departmentId(1)
            .permissions(new HashSet<>(Arrays.asList("READ")))
            .name("Staff " + id)
            .email(id.toLowerCase() + "@example.com")
            .phone("000000000")
            .address("Address for " + id)
            .bankName("Bank X")
            .bankAccount("999999")
            .avatar("avatar.png")
            .hireDate(LocalDate.now())
            .baseSalary(2000.0)
            .status(1)
            .build();
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<StaffModel> create(@RequestBody StaffModel model) {
        StaffModel created = StaffModel.builder()
            .staffId("S100")
            .userId(model.getUserId())
            .departmentId(model.getDepartmentId())
            .permissions(model.getPermissions())
            .name(model.getName())
            .email(model.getEmail())
            .phone(model.getPhone())
            .address(model.getAddress())
            .bankName(model.getBankName())
            .bankAccount(model.getBankAccount())
            .avatar(model.getAvatar())
            .hireDate(model.getHireDate())
            .baseSalary(model.getBaseSalary())
            .status(model.getStatus())
            .build();
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffModel> update(@PathVariable String id, @RequestBody StaffModel model) {
        StaffModel updated = StaffModel.builder()
            .staffId(id)
            .userId(model.getUserId())
            .departmentId(model.getDepartmentId())
            .permissions(model.getPermissions())
            .name(model.getName())
            .email(model.getEmail())
            .phone(model.getPhone())
            .address(model.getAddress())
            .bankName(model.getBankName())
            .bankAccount(model.getBankAccount())
            .avatar(model.getAvatar())
            .hireDate(model.getHireDate())
            .baseSalary(model.getBaseSalary())
            .status(model.getStatus())
            .build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return ResponseEntity.noContent().build();
    }
}
