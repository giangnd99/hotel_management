package com.poly.ai.management.domain;

import com.poly.ai.management.domain.port.input.service.DataIngestionService;
import com.poly.ai.management.domain.port.output.feign.RoomFeign;
import com.poly.ai.management.domain.port.output.repository.ChatSessionRepository;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration; // THÊM IMPORT NÀY
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests; // THÊM IMPORT NÀY

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

// Kế thừa AbstractTestNGSpringContextTests để TestNG biết rằng đây là test Spring
@ContextConfiguration(classes = AiManagementApplication.class) // Chỉ định lớp cấu hình của Spring
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.task.scheduling.enalbe=true",
                "spring.task.scheduling.fixed-rate=5000", // Giảm fixedRate xuống 5 giây cho test
                // Đảm bảo cấu hình DataSource cho test context
                "spring.datasource.url=jdbc:postgresql://localhost:5433/postgres?currentSchema=ai_management&binaryTransfer=true&reWriteBatchedInserts=true&stringtype=unspecified",
                "spring.datasource.username=postgres",
                "spring.datasource.password=admin",
                "spring.datasource.driver-class-name=org.postgresql.Driver"
        }
)
@ActiveProfiles("test") // Kích hoạt profile 'test', nếu bạn có application-test.yml
public class DataIngestionServiceImplIntegrationTest extends AbstractTestNGSpringContextTests { // KẾ THỪA LỚP NÀY

    @Autowired
    private DataIngestionService dataIngestionService;

    @Autowired
    private RoomFeign roomFeign;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    private static final String VECTOR_TABLE_NAME = "vector_store";
    private static final String AI_MANAGEMENT_SCHEMA = "ai_management";

    @BeforeMethod
    void setUp() {
        // Các kiểm tra null sẽ được thực hiện sau khi Spring đã inject các dependencies.
        // Nếu các bean này vẫn null sau khi kế thừa AbstractTestNGSpringContextTests,
        // thì có vấn đề nghiêm trọng với cấu hình Spring Boot Test của bạn.
        assertThat(dataIngestionService).isNotNull();
        assertThat(roomFeign).isNotNull();
        assertThat(vectorStore).isNotNull();
        assertThat(jdbcTemplate).isNotNull();

        try {
            jdbcTemplate.execute("DELETE FROM " + AI_MANAGEMENT_SCHEMA + "." + VECTOR_TABLE_NAME);
        } catch (Exception e) {
            System.err.println("Error cleaning vector_store table: " + e.getMessage());
        }
    }

    @AfterMethod
    void tearDown() {
        try {
            jdbcTemplate.execute("DELETE FROM " + AI_MANAGEMENT_SCHEMA + "." + VECTOR_TABLE_NAME);
        } catch (Exception e) {
            System.err.println("Error cleaning vector_store table after test: " + e.getMessage());
        }
    }

    /**
     * Test case kiểm tra luồng tích hợp hoàn chỉnh:
     * 1. RoomFeign gọi dịch vụ room-management-application (thực tế).
     * 2. DataIngestionService xử lý dữ liệu trả về.
     * 3. Documents được thêm vào VectorStore (thực tế trong PostgreSQL).
     * <p>
     * Yêu cầu: Dịch vụ 'room-management-application' phải đang chạy
     * trên cổng 8087 và trả về dữ liệu hợp lệ tại endpoint /api/rooms.
     */
    @Test
    void ingestHotelData_fetchesFromRealRoomServiceAndAddsToRealVectorStore() {
        dataIngestionService.ingestHotelData();
        List<Document> documentsInVectorStore = vectorStore.similaritySearch("any query to retrieve all data");
        assertThat(documentsInVectorStore).isNotEmpty();
        Document firstDocument = documentsInVectorStore.get(0);
        assertThat(firstDocument.getMetadata()).containsKey("roomNumber");
        assertThat(firstDocument.getMetadata()).containsKey("type");
        assertThat(firstDocument.getMetadata()).containsKey("pricePerNight");
    }

    /**
     * Test case kiểm tra chức năng @Scheduled.
     * Phương thức này sẽ chờ đợi @Scheduled method 'scheduleIngestion()'
     * được kích hoạt bởi Spring Boot và kiểm tra hệ quả của nó (dữ liệu được thêm vào VectorStore).
     * <p>
     * Yêu cầu:
     * - Dịch vụ 'room-management-application' phải đang chạy.
     * - Thuộc tính `spring.task.scheduling.fixed-rate` phải được đặt thành một giá trị nhỏ
     * (ví dụ: 5000ms như đã ghi đè ở trên) trong môi trường test để test có thể hoàn thành nhanh.
     */
    @Test
    void scheduleIngestion_executesAndAddsDataToRealVectorStore() {
        await().atMost(15, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    List<Document> documentsInVectorStore = vectorStore.similaritySearch("dummy query to check data presence");
                    assertThat(documentsInVectorStore).isNotEmpty();
                });
    }
}
