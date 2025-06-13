package com.poly.ai.management.domain.service.rag.scheduler;

import com.poly.ai.management.domain.entity.Room;
import com.poly.ai.management.domain.port.input.service.DataIngestionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataIngestionServiceImpl implements DataIngestionService {

    private final VectorStore vectorStore;

    @PostConstruct
    public void loadSampleData() {
        if (hotelRoomRepository.count() == 0) {
            log.info("Nạp dữ liệu phòng khách sạn mẫu vào database...");
            hotelRoomRepository.save(new HotelRoom("101", "Standard", 100.0, 2,
                    "Phòng tiêu chuẩn với 1 giường đôi, view thành phố, đầy đủ tiện nghi cơ bản như TV, điều hòa, tủ lạnh mini."));
            hotelRoomRepository.save(new HotelRoom("203", "Deluxe", 150.0, 3,
                    "Phòng Deluxe rộng rãi với 1 giường King hoặc 2 giường đơn, ban công riêng, view biển tuyệt đẹp, minibar miễn phí."));
            hotelRoomRepository.save(new HotelRoom("305", "Suite", 300.0, 4,
                    "Phòng Suite sang trọng với phòng khách riêng biệt, 2 phòng ngủ, bồn tắm jacuzzi, phục vụ ăn sáng tận phòng, và quản gia riêng."));
            hotelRoomRepository.save(new HotelRoom("102", "Standard", 110.0, 2,
                    "Phòng tiêu chuẩn có 2 giường đơn, hướng vườn, thoải mái và yên tĩnh."));
            hotelRoomRepository.save(new HotelRoom("201", "Deluxe", 160.0, 2,
                    "Phòng Deluxe view hồ bơi, có khu vực làm việc, phù hợp cho khách công tác."));
            log.info("Đã nạp xong dữ liệu mẫu.");
        }
    }

    @Override
    public void ingestHotelData() {
        List<Room> rooms = List.of();
        if (rooms.isEmpty()) return;
        log.info("Đã tìm thấy phòng từ khách sạn");

        List<Document> documents = rooms.stream().map(room -> {
            // Tạo nội dung cho Document từ thông tin phòng
            String content = String.format("Thông tin phòng: Số phòng %s, Loại: %s, Giá: %.2f USD/đêm, Sức chứa: %d người. Mô tả: %s",
                    room.getRoomNumber(), room.getType(), room.getPricePerNight(), room.getCapacity(), room.getDescription());

            // Thêm metadata để dễ dàng truy vấn hoặc hiển thị sau này
            Document document = new Document(content);
            document.getMetadata().put("roomNumber", room.getRoomNumber());
            document.getMetadata().put("type", room.getType());
            document.getMetadata().put("pricePerNight", room.getPricePerNight());
            document.getMetadata().put("capacity", room.getCapacity());
            return document;
        }).collect(Collectors.toList());
        // 3. Chia nhỏ các Document thành các chunks nhỏ hơn (nếu cần)
        // TokenTextSplitter giúp đảm bảo các chunks không quá lớn cho LLM
        TokenTextSplitter textSplitter = new TokenTextSplitter(); // Default chunk size and overlap
        List<Document> chunks = textSplitter.apply(documents);
        log.info("Đã chia thành {} chunks để đưa vào Vector Store.", chunks.size());

        // 4. Thêm các chunks vào Vector Store
        // Spring AI sẽ tự động sử dụng EmbeddingModel đã cấu hình để nhúng các chunks
        vectorStore.add(chunks);
        log.info("Đã thêm {} chunks vào Vector Store.", chunks.size());

    }

    @Scheduled(fixedRate = 3600000) // Cập nhật mỗi giờ
    public void scheduleIngestion() {
        log.info("Bắt đầu cập nhật dữ liệu vào Vector Store theo lịch trình...");
        ingestHotelData();
        log.info("Cập nhật dữ liệu vào Vector Store hoàn tất.");
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Bắt đầu quá trình nạp dữ liệu từ DB vào Vector Store...");
        ingestHotelData();
        log.info("Quá trình nạp dữ liệu vào Vector Store hoàn tất.");
    }
}
