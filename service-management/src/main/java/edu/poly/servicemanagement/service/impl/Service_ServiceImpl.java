package edu.poly.servicemanagement.service.impl;

import edu.poly.servicemanagement.entity.Service_;
import edu.poly.servicemanagement.repository.Service_Repository;
import edu.poly.servicemanagement.service.Service_Service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Service_ServiceImpl implements Service_Service {

    private final Service_Repository serviceRepository;

    public Service_ServiceImpl(Service_Repository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Lấy tất cả các dịch vụ.
     *
     * @return Danh sách tất cả các dịch vụ.
     */
    public List<Service_> getAll() {
        return serviceRepository.findAll();
    }

    /**
     * Lấy một dịch vụ theo ID.
     *
     * @param id ID của dịch vụ.
     * @return Optional chứa dịch vụ nếu tìm thấy, hoặc rỗng nếu không tìm thấy.
     */
    public Optional<Service_> getById(Integer id) {
        return serviceRepository.findById(id);
    }

    /**
     * Tạo một dịch vụ mới.
     *
     * @param service Đối tượng dịch vụ cần tạo.
     * @return Dịch vụ đã được lưu vào cơ sở dữ liệu.
     */
    public Service_ create(Service_ service) {
        return serviceRepository.save(service);
    }

    /**
     * Cập nhật một dịch vụ hiện có.
     *
     * @param id             ID của dịch vụ cần cập nhật.
     * @param serviceDetails Đối tượng dịch vụ với thông tin cập nhật.
     * @return Dịch vụ đã cập nhật, hoặc null nếu không tìm thấy dịch vụ.
     */
    public Service_ update(Integer id, Service_ serviceDetails) {
        Optional<Service_> existingService = serviceRepository.findById(id);
        if (existingService.isPresent()) {
            Service_ serviceToUpdate = existingService.get();
            serviceToUpdate.setServiceName(serviceDetails.getServiceName());
            serviceToUpdate.setDescription(serviceDetails.getDescription());
            serviceToUpdate.setPrice(serviceDetails.getPrice());
            serviceToUpdate.setAvailability(serviceDetails.getAvailability());
            return serviceRepository.save(serviceToUpdate);
        }
        return null; // Hoặc ném một ngoại lệ tùy chỉnh
    }

    /**
     * Xóa một dịch vụ theo ID.
     *
     * @param id ID của dịch vụ cần xóa.
     * @return true nếu xóa thành công, false nếu không tìm thấy dịch vụ.
     */
    public boolean delete(Integer id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
