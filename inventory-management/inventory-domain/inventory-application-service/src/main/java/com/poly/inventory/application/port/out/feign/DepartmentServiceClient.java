package com.poly.inventory.application.port.out.feign;

import com.poly.inventory.application.dto.feign.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "department-service")
public interface DepartmentServiceClient {

    @GetMapping("/departments")
    ResponseEntity<List<DepartmentDto>> getAllDepartments();

    @GetMapping("/departments/{id}")
    ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Integer id);

    @PostMapping("/departments")
    ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto departmentDto);

    @PutMapping("/departments/{id}")
    ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Integer id, @RequestBody DepartmentDto departmentDto);

    @DeleteMapping("/departments/{id}")
    ResponseEntity<Void> deleteDepartment(@PathVariable Integer id);
}
