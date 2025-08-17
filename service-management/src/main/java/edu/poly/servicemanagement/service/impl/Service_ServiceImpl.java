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

    @Override
    public Double getServiceRevenue() {
        return 0.0;
    }

    @Override
    public List<Service_> searchServices(String name, String description, String category, Double minPrice, Double maxPrice) {
        return List.of();
    }

    @Override
    public List<Service_> filterByDepartment(String department) {
        return List.of();
    }

    @Override
    public List<Service_> filterByState(String state) {
        return List.of();
    }

    @Override
    public List<String> getAllCategories() {
        return List.of();
    }

    @Override
    public List<Service_> getServicesByCategory(String category) {
        return List.of();
    }

    @Override
    public Object getPriceRange() {
        return null;
    }

    @Override
    public Service_ updatePrice(Integer id, Double newPrice) {
        return null;
    }

    @Override
    public List<Service_> getAvailableServices() {
        return List.of();
    }

    @Override
    public Service_ updateAvailability(Integer id, Boolean available) {
        return null;
    }

    @Override
    public Object getUsageStatistics() {
        return null;
    }

    @Override
    public Object getRevenueStatistics() {
        return null;
    }
}
