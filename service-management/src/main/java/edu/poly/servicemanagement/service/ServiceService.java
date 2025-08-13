package edu.poly.servicemanagement.service;

import edu.poly.servicemanagement.model.Services;
import edu.poly.servicemanagement.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    /**
     * Lấy tất cả các dịch vụ.
     * @return Danh sách tất cả các dịch vụ.
     */
    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    /**
     * Lấy một dịch vụ theo ID.
     * @param id ID của dịch vụ.
     * @return Optional chứa dịch vụ nếu tìm thấy, hoặc rỗng nếu không tìm thấy.
     */
    public Optional<Services> getServiceById(Integer id) {
        return serviceRepository.findById(id);
    }

    /**
     * Tạo một dịch vụ mới.
     * @param service Đối tượng dịch vụ cần tạo.
     * @return Dịch vụ đã được lưu vào cơ sở dữ liệu.
     */
    public Services createService(Services service) {
        return serviceRepository.save(service);
    }

    /**
     * Cập nhật một dịch vụ hiện có.
     * @param id ID của dịch vụ cần cập nhật.
     * @param serviceDetails Đối tượng dịch vụ với thông tin cập nhật.
     * @return Dịch vụ đã cập nhật, hoặc null nếu không tìm thấy dịch vụ.
     */
    public Services updateService(Integer id, Services serviceDetails) {
        Optional<Services> existingService = serviceRepository.findById(id);
        if (existingService.isPresent()) {
            Services serviceToUpdate = existingService.get();
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
     * @param id ID của dịch vụ cần xóa.
     * @return true nếu xóa thành công, false nếu không tìm thấy dịch vụ.
     */
    public boolean deleteService(Integer id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
