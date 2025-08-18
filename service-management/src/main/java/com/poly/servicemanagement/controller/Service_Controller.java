package com.poly.servicemanagement.controller;

import com.poly.servicemanagement.entity.Service_;
import com.poly.servicemanagement.service.Service_Service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Slf4j
public class Service_Controller {

    private final Service_Service service;

    // ========== CRUD APIs ==========

    @GetMapping
    @Operation(summary = "Lấy tất cả các dịch vụ")
    public ResponseEntity<List<Service_>> getAllServices() {
        log.info("Getting all services");
        List<Service_> services = service.getAll();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy một dịch vụ theo ID")
    public ResponseEntity<Service_> getServiceById(@PathVariable Integer id) {
        log.info("Getting service by id: {}", id);
        Optional<Service_> service = this.service.getById(id);
        return service.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Tạo một dịch vụ mới")
    public ResponseEntity<Service_> createService(@RequestBody Service_ sv) {
        log.info("Creating new service: {}", sv.getServiceName());
        Service_ createdService = service.create(sv);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật một dịch vụ hiện có")
    public ResponseEntity<Service_> updateService(@PathVariable Integer id, @RequestBody Service_ serviceDetails) {
        log.info("Updating service: {}", id);
        Service_ updatedService = service.update(id, serviceDetails);
        if (updatedService != null) {
            return new ResponseEntity<>(updatedService, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa một dịch vụ theo ID")
    public ResponseEntity<HttpStatus> deleteService(@PathVariable Integer id) {
        log.info("Deleting service: {}", id);
        boolean deleted = service.delete(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // ========== NEW DASHBOARD & STATISTICS APIs ==========

    @GetMapping("/revenue")
    @Operation(summary = "Lấy doanh thu dịch vụ")
    public ResponseEntity<Double> getServiceRevenue() {
        log.info("Getting service revenue");
        Double revenue = service.getServiceRevenue();
        return ResponseEntity.ok(revenue);
    }

    // ========== SEARCH & FILTER APIs ==========

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm dịch vụ")
    public ResponseEntity<List<Service_>> searchServices(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        log.info("Searching services with filters");
        List<Service_> services = service.searchServices(name, description, category, minPrice, maxPrice);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/filter/department/{department}")
    @Operation(summary = "Lọc dịch vụ theo phòng ban")
    public ResponseEntity<List<Service_>> filterServicesByDepartment(@PathVariable String department) {
        log.info("Filtering services by department: {}", department);
        List<Service_> services = service.filterByDepartment(department);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/filter/state/{state}")
    @Operation(summary = "Lọc dịch vụ theo trạng thái")
    public ResponseEntity<List<Service_>> filterServicesByState(@PathVariable String state) {
        log.info("Filtering services by state: {}", state);
        List<Service_> services = service.filterByState(state);
        return ResponseEntity.ok(services);
    }

    // ========== CATEGORY MANAGEMENT APIs ==========

    @GetMapping("/categories")
    @Operation(summary = "Lấy danh sách tất cả danh mục dịch vụ")
    public ResponseEntity<List<String>> getAllServiceCategories() {
        log.info("Getting all service categories");
        List<String> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{category}")
    @Operation(summary = "Lấy dịch vụ theo danh mục")
    public ResponseEntity<List<Service_>> getServicesByCategory(@PathVariable String category) {
        log.info("Getting services by category: {}", category);
        List<Service_> services = service.getServicesByCategory(category);
        return ResponseEntity.ok(services);
    }

    // ========== PRICING MANAGEMENT APIs ==========

    @GetMapping("/price-range")
    @Operation(summary = "Lấy khoảng giá dịch vụ")
    public ResponseEntity<Object> getServicePriceRange() {
        log.info("Getting service price range");
        Object priceRange = service.getPriceRange();
        return ResponseEntity.ok(priceRange);
    }

    @PutMapping("/{id}/price")
    @Operation(summary = "Cập nhật giá dịch vụ")
    public ResponseEntity<Service_> updateServicePrice(
            @PathVariable Integer id,
            @RequestParam Double newPrice) {
        log.info("Updating service price: {} to {}", id, newPrice);
        Service_ updatedService = service.updatePrice(id, newPrice);
        return ResponseEntity.ok(updatedService);
    }

    // ========== AVAILABILITY MANAGEMENT APIs ==========

    @GetMapping("/available")
    @Operation(summary = "Lấy danh sách dịch vụ khả dụng")
    public ResponseEntity<List<Service_>> getAvailableServices() {
        log.info("Getting available services");
        List<Service_> services = service.getAvailableServices();
        return ResponseEntity.ok(services);
    }

    @PutMapping("/{id}/availability")
    @Operation(summary = "Cập nhật trạng thái khả dụng của dịch vụ")
    public ResponseEntity<Service_> updateServiceAvailability(
            @PathVariable Integer id,
            @RequestParam Boolean available) {
        log.info("Updating service availability: {} to {}", id, available);
        Service_ updatedService = service.updateAvailability(id, available);
        return ResponseEntity.ok(updatedService);
    }


    // ========== STATISTICS APIs ==========

    @GetMapping("/statistics/usage")
    @Operation(summary = "Lấy thống kê sử dụng dịch vụ")
    public ResponseEntity<Object> getServiceUsageStatistics() {
        log.info("Getting service usage statistics");
        Object statistics = service.getUsageStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/statistics/revenue")
    @Operation(summary = "Lấy thống kê doanh thu dịch vụ")
    public ResponseEntity<Object> getServiceRevenueStatistics() {
        log.info("Getting service revenue statistics");
        Object statistics = service.getRevenueStatistics();
        return ResponseEntity.ok(statistics);
    }
}
