package com.poly.ai.management.domain;

import com.poly.ai.management.domain.dto.RoomResponse;
import com.poly.ai.management.domain.port.output.feign.RoomFeign;
import com.poly.ai.management.domain.port.output.repository.ChatSessionRepository;
import com.poly.ai.management.domain.service.rag.scheduler.DataIngestionServiceImpl;
import org.testng.annotations.AfterMethod; // Thay thế AfterEach
import org.testng.annotations.BeforeMethod; // Thay thế BeforeEach
import org.testng.annotations.Test;         // Vẫn là Test
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

// @SpringBootTest vẫn hoạt động với TestNG
@SpringBootTest(
        classes = AiManagementApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.task.scheduling.fixed-rate=5000" // Giảm fixedRate xuống 5 giây cho test
        }
)
@ActiveProfiles("test") // Kích hoạt profile 'test', nếu bạn có application-test.yml
public class DataIngestionServiceImplIntegrationTest {

    @Autowired
    private DataIngestionServiceImpl dataIngestionService; // Dịch vụ cần test (bean thực)

    @Autowired
    private RoomFeign roomFeign; // Feign Client thực tế (sẽ gọi room-management-application)

    @Autowired
    private VectorStore vectorStore; // VectorStore thực tế (sẽ tương tác với PostgreSQL)

    @Autowired
    private JdbcTemplate jdbcTemplate; // Để làm sạch database

    // Mock ChatSessionRepository vì nó không liên quan trực tiếp đến DataIngestionServiceImpl
    // nhưng là dependency của HotelAiChatServiceImpl và có thể được SpringBootTest khởi tạo.
    // Việc mock giúp test tập trung hơn và không cần Redis chạy.
    @MockBean
    private ChatSessionRepository chatSessionRepository;

    private static final String VECTOR_TABLE_NAME = "vector_store"; // Tên bảng vector_store
    private static final String AI_MANAGEMENT_SCHEMA = "ai_management"; // Tên schema

    @BeforeMethod // Thay thế @BeforeEach của JUnit 5
    void setUp() {
        // Dọn dẹp bảng vector_store trước mỗi test để đảm bảo môi trường sạch sẽ.
        // Điều này rất quan trọng cho các integration test để đảm bảo tính độc lập.
        jdbcTemplate.execute("DELETE FROM " + AI_MANAGEMENT_SCHEMA + "." + VECTOR_TABLE_NAME);
    }

    @AfterMethod // Thay thế @AfterEach của JUnit 5
    void tearDown() {
        // Dọn dẹp bảng vector_store sau mỗi test (tùy chọn, vì BeforeMethod đã dọn).
        jdbcTemplate.execute("DELETE FROM " + AI_MANAGEMENT_SCHEMA + "." + VECTOR_TABLE_NAME);
    }

    /**
     * Test case kiểm tra luồng tích hợp hoàn chỉnh:
     * 1. RoomFeign gọi dịch vụ room-management-application (thực tế).
     * 2. DataIngestionService xử lý dữ liệu trả về.
     * 3. Documents được thêm vào VectorStore (thực tế trong PostgreSQL).
     *
     * Yêu cầu: Dịch vụ 'room-management-application' phải đang chạy
     * trên cổng 8087 và trả về dữ liệu hợp lệ tại endpoint /api/rooms.
     */
    @Test
    void ingestHotelData_fetchesFromRealRoomServiceAndAddsToRealVectorStore() {
        // Act: Gọi phương thức ingestHotelData() của dịch vụ
        // Phương thức này sẽ sử dụng RoomFeign thực tế để lấy dữ liệu.
        dataIngestionService.ingestHotelData();

        // Assert: Kiểm tra xem dữ liệu có được thêm vào VectorStore hay không
        // Chúng ta sẽ truy vấn trực tiếp VectorStore để xác nhận.
        List<Document> documentsInVectorStore = vectorStore.similaritySearch("any query to retrieve all data");

        // Kiểm tra xem có dữ liệu nào được thêm vào vector store không
        assertThat(documentsInVectorStore).isNotEmpty();

        // Kiểm tra nội dung của một số document (nếu có)
        // Lưu ý: Nội dung và metadata sẽ phụ thuộc vào dữ liệu thực tế từ room-service.
        // Đảm bảo dữ liệu mẫu của room-service khớp với kỳ vọng ở đây.
        Document firstDocument = documentsInVectorStore.get(0);
        assertThat(firstDocument.getMetadata()).containsKey("roomNumber");
        assertThat(firstDocument.getMetadata()).containsKey("type");
        assertThat(firstDocument.getMetadata()).containsKey("pricePerNight");
        // Bạn có thể thêm các asserts cụ thể hơn nếu biết dữ liệu mẫu từ room-service.
        // Ví dụ: assertThat(firstDocument.getMetadata().get("roomNumber")).isEqualTo("101");
    }

    /**
     * Test case kiểm tra chức năng @Scheduled.
     * Phương thức này sẽ chờ đợi @Scheduled method 'scheduleIngestion()'
     * được kích hoạt bởi Spring Boot và kiểm tra hệ quả của nó (dữ liệu được thêm vào VectorStore).
     *
     * Yêu cầu:
     * - Dịch vụ 'room-management-application' phải đang chạy.
     * - Thuộc tính `spring.task.scheduling.fixed-rate` phải được đặt thành một giá trị nhỏ
     * (ví dụ: 5000ms như đã ghi đè ở trên) trong môi trường test để test có thể hoàn thành nhanh.
     */
    @Test
    void scheduleIngestion_executesAndAddsDataToRealVectorStore() {
        // Assert: Chờ đợi cho đến khi phương thức scheduleIngestion() được gọi
        // và nó thực hiện việc thêm dữ liệu vào VectorStore.
        // Awaitility sẽ poll điều kiện này cho đến khi nó đúng hoặc hết thời gian.
        await().atMost(15, TimeUnit.SECONDS) // Tăng thời gian chờ một chút để đảm bảo schedule chạy
                .pollInterval(1, TimeUnit.SECONDS) // Kiểm tra mỗi 1 giây
                .untilAsserted(() -> {
                    // Sau khi schedule chạy, nó sẽ gọi ingestHotelData(),
                    // và ingestHotelData() sẽ thêm documents vào vector store.
                    // Chúng ta kiểm tra VectorStore có chứa dữ liệu hay không.
                    List<Document> documentsInVectorStore = vectorStore.similaritySearch("dummy query to check data presence");
                    assertThat(documentsInVectorStore).isNotEmpty();
                });

        // Tùy chọn: Sau khi xác nhận dữ liệu đã có, bạn có thể kiểm tra thêm
        // số lượng document hoặc nội dung của chúng nếu cần.
    }
}
