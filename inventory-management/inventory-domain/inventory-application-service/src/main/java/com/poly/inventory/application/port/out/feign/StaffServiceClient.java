package com.poly.inventory.application.port.out.feign;

import com.poly.inventory.application.dto.feign.StaffDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "staff-service", url = "${staff.service.url}")
public interface StaffServiceClient {

    @GetMapping
    ResponseEntity<List<StaffDto>> getAllStaff();

    @GetMapping("/{id}")
    ResponseEntity<StaffDto> getStaffById(@PathVariable Integer id);

    @PostMapping
    ResponseEntity<StaffDto> createStaff(@RequestBody StaffDto model);

    @PutMapping("/{id}")
    ResponseEntity<StaffDto> updateStaff(@PathVariable String id, @RequestBody StaffDto model);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteStaff(@PathVariable String id);
}
