package edu.poly.servicemanagement.controller;

import edu.poly.servicemanagement.entity.Service_;
import edu.poly.servicemanagement.service.Service_Service;
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

    /**
     * Lấy tất cả các dịch vụ.
     * GET /api/services
     *
     * @return Danh sách các dịch vụ.
     */
    @GetMapping
    public ResponseEntity<List<Service_>> getAllServices() {
        List<Service_> services = service.getAll();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    /**
     * Lấy một dịch vụ theo ID.
     * GET /api/services/{id}
     *
     * @param id ID của dịch vụ.
     * @return Dịch vụ nếu tìm thấy, hoặc 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Service_> getServiceById(@PathVariable Integer id) {
        Optional<Service_> service = this.service.getById(id);
        return service.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Tạo một dịch vụ mới.
     * POST /api/services
     *
     * @param sv Đối tượng dịch vụ cần tạo.
     * @return Dịch vụ đã tạo.
     */
    @PostMapping
    public ResponseEntity<Service_> createService(@RequestBody Service_ sv) {
        Service_ createdService = service.create(sv);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    /**
     * Cập nhật một dịch vụ hiện có.
     * PUT /api/services/{id}
     *
     * @param id             ID của dịch vụ cần cập nhật.
     * @param serviceDetails Đối tượng dịch vụ với thông tin cập nhật.
     * @return Dịch vụ đã cập nhật nếu tìm thấy, hoặc 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Service_> updateService(@PathVariable Integer id, @RequestBody Service_ serviceDetails) {
        Service_ updatedService = service.update(id, serviceDetails);
        if (updatedService != null) {
            return new ResponseEntity<>(updatedService, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Xóa một dịch vụ theo ID.
     * DELETE /api/services/{id}
     *
     * @param id ID của dịch vụ cần xóa.
     * @return 204 No Content nếu xóa thành công, hoặc 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteService(@PathVariable Integer id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
