package edu.poly.servicemanagement;

import edu.poly.servicemanagement.model.Services;
import edu.poly.servicemanagement.repository.ServiceRepository;
import edu.poly.servicemanagement.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Kích hoạt Mockito cho JUnit 5
public class ServiceServiceTest {

    @Mock // Tạo một mock object cho ServiceRepository
    private ServiceRepository serviceRepository;

    @InjectMocks // Tiêm các mock object vào ServiceService
    private ServiceService serviceService;

    private Services service1;
    private Services service2;

    @BeforeEach
    void setUp() {
        // Khởi tạo dữ liệu mock trước mỗi bài kiểm thử
        service1 = new Services(1, "Phòng đơn", "Phòng đơn tiện nghi", new BigDecimal("100.00"), "Available");
        service2 = new Services(2, "Phòng đôi", "Phòng đôi rộng rãi", new BigDecimal("150.00"), "Available");
    }

    @Test
    void getAllServices_shouldReturnAllServices() {
        // Định nghĩa hành vi của mock: khi findAll() được gọi, trả về danh sách service1 và service2
        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service1, service2));

        List<Services> services = serviceService.getAllServices();

        // Xác nhận kết quả
        assertNotNull(services);
        assertEquals(2, services.size());
        assertEquals("Phòng đơn", services.get(0).getServiceName());
        assertEquals("Phòng đôi", services.get(1).getServiceName());

        // Xác minh rằng phương thức findAll() của repository đã được gọi đúng một lần
        verify(serviceRepository, times(1)).findAll();
    }

    @Test
    void getServiceById_shouldReturnServiceWhenFound() {
        // Định nghĩa hành vi của mock: khi findById(1) được gọi, trả về Optional chứa service1
        when(serviceRepository.findById(1)).thenReturn(Optional.of(service1));

        Optional<Services> foundService = serviceService.getServiceById(1);

        // Xác nhận kết quả
        assertTrue(foundService.isPresent());
        assertEquals(service1.getServiceName(), foundService.get().getServiceName());

        // Xác minh rằng phương thức findById() của repository đã được gọi đúng một lần
        verify(serviceRepository, times(1)).findById(1);
    }

    @Test
    void getServiceById_shouldReturnEmptyWhenNotFound() {
        // Định nghĩa hành vi của mock: khi findById(99) được gọi, trả về Optional rỗng
        when(serviceRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Services> foundService = serviceService.getServiceById(99);

        // Xác nhận kết quả
        assertFalse(foundService.isPresent());

        // Xác minh rằng phương thức findById() của repository đã được gọi đúng một lần
        verify(serviceRepository, times(1)).findById(99);
    }

    @Test
    void createService_shouldReturnCreatedService() {
        Services newService = new Services(null, "Phòng VIP", "Phòng cao cấp", new BigDecimal("300.00"), "Available");
        // Định nghĩa hành vi của mock: khi save() được gọi với bất kỳ đối tượng Services nào, trả về đối tượng đó
        when(serviceRepository.save(any(Services.class))).thenReturn(service1); // Giả sử save trả về service1 sau khi gán ID

        Services createdService = serviceService.createService(newService);

        // Xác nhận kết quả
        assertNotNull(createdService);
        assertEquals(service1.getServiceName(), createdService.getServiceName()); // Kiểm tra tên dịch vụ

        // Xác minh rằng phương thức save() của repository đã được gọi đúng một lần với đối tượng newService
        verify(serviceRepository, times(1)).save(newService);
    }

    @Test
    void updateService_shouldReturnUpdatedServiceWhenFound() {
        Services updatedDetails = new Services(1, "Phòng đơn VIP", "Phòng đơn đã nâng cấp", new BigDecimal("120.00"), "Unavailable");

        // Định nghĩa hành vi của mock
        when(serviceRepository.findById(1)).thenReturn(Optional.of(service1));
        when(serviceRepository.save(any(Services.class))).thenReturn(updatedDetails); // Giả sử save trả về updatedDetails

        Services result = serviceService.updateService(1, updatedDetails);

        // Xác nhận kết quả
        assertNotNull(result);
        assertEquals("Phòng đơn VIP", result.getServiceName());
        assertEquals("Phòng đơn đã nâng cấp", result.getDescription());
        assertEquals(new BigDecimal("120.00"), result.getPrice());
        assertEquals("Unavailable", result.getAvailability());

        // Xác minh các cuộc gọi phương thức
        verify(serviceRepository, times(1)).findById(1);
        verify(serviceRepository, times(1)).save(service1); // service1 được cập nhật và lưu
    }

    @Test
    void updateService_shouldReturnNullWhenNotFound() {
        Services updatedDetails = new Services(99, "Phòng không tồn tại", "Mô tả", new BigDecimal("100.00"), "Available");

        // Định nghĩa hành vi của mock: khi findById(99) được gọi, trả về Optional rỗng
        when(serviceRepository.findById(99)).thenReturn(Optional.empty());

        Services result = serviceService.updateService(99, updatedDetails);

        // Xác nhận kết quả
        assertNull(result);

        // Xác minh rằng phương thức findById() đã được gọi, nhưng save() thì không
        verify(serviceRepository, times(1)).findById(99);
        verify(serviceRepository, never()).save(any(Services.class));
    }

    @Test
    void deleteService_shouldReturnTrueWhenFound() {
        // Định nghĩa hành vi của mock: khi existsById(1) được gọi, trả về true
        when(serviceRepository.existsById(1)).thenReturn(true);
        // Không cần định nghĩa hành vi cho deleteById vì nó là void

        boolean deleted = serviceService.deleteService(1);

        // Xác nhận kết quả
        assertTrue(deleted);

        // Xác minh rằng các phương thức đã được gọi đúng một lần
        verify(serviceRepository, times(1)).existsById(1);
        verify(serviceRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteService_shouldReturnFalseWhenNotFound() {
        // Định nghĩa hành vi của mock: khi existsById(99) được gọi, trả về false
        when(serviceRepository.existsById(99)).thenReturn(false);

        boolean deleted = serviceService.deleteService(99);

        // Xác nhận kết quả
        assertFalse(deleted);

        // Xác minh rằng existsById() đã được gọi, nhưng deleteById() thì không
        verify(serviceRepository, times(1)).existsById(99);
        verify(serviceRepository, never()).deleteById(anyInt());
    }
}
