package com.poly.staff.controller;

import com.poly.staff.model.DepartmentModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @GetMapping
    public ResponseEntity<List<DepartmentModel>> getAll() {
        List<DepartmentModel> list = Arrays.asList(
            DepartmentModel.builder()
                .departmentId(1)
                .name("HR")
                .description("Human Resources")
                .build(),
            DepartmentModel.builder()
                .departmentId(2)
                .name("IT")
                .description("Information Technology")
                .build()
        );
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentModel> getById(@PathVariable Integer id) {
        DepartmentModel model = DepartmentModel.builder()
            .departmentId(id)
            .name("Department " + id)
            .description("Description for department " + id)
            .build();
        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<DepartmentModel> create(@RequestBody DepartmentModel model) {
        DepartmentModel created = DepartmentModel.builder()
            .departmentId(100)
            .name(model.getName())
            .description(model.getDescription())
            .build();
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentModel> update(@PathVariable Integer id, @RequestBody DepartmentModel model) {
        DepartmentModel updated = DepartmentModel.builder()
            .departmentId(id)
            .name(model.getName())
            .description(model.getDescription())
            .build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return ResponseEntity.noContent().build();
    }
}
