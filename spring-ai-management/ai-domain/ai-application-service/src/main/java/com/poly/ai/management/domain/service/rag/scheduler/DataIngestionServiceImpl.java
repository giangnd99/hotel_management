package com.poly.ai.management.domain.service.rag.scheduler;

import com.poly.ai.management.domain.dto.RoomResponse;
import com.poly.ai.management.domain.port.input.service.DataIngestionService;
import com.poly.ai.management.domain.port.output.feign.RoomFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataIngestionServiceImpl implements DataIngestionService {

    private final VectorStore vectorStore;
    private final RoomFeign roomFeign;

    @Override
    public void ingestHotelData() {
        try{
        List<RoomResponse> rooms = roomFeign.getAllRooms().getBody();
        if (rooms.isEmpty()) return;
        log.info("Đã tìm thấy phòng từ khách sạn");

        List<Document> documents = rooms.stream().map(room -> {
            // Tạo nội dung cho Document từ thông tin phòng
            String content = String.format("Thông tin phòng: Số phòng %s, Loại: %s, Giá: %s USD/đêm, Sức chứa: %d người. Mô tả: %s . Trạng thái: %s",
                    room.getRoomNumber(), room.getRoomType().getTypeName(), room.getRoomType().getBasePrice(), room.getRoomType().getMaxOccupancy(), room.getRoomType().getDescription(), room.getRoomStatus());

            // Thêm metadata để dễ dàng truy vấn hoặc hiển thị sau này
            Document document = new Document(content);
            document.getMetadata().put("roomNumber", room.getRoomNumber());
            document.getMetadata().put("type", room.getRoomType().getTypeName());
            document.getMetadata().put("pricePerNight", room.getRoomType().getBasePrice());
            document.getMetadata().put("capacity", room.getRoomType().getMaxOccupancy());
            document.getMetadata().put("roomStatus", room.getRoomStatus());
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
        }catch (Exception e){
            log.error(e.getMessage(), e.getCause());
        }
    }

    @Scheduled(fixedRate = 360000) // Cập nhật mỗi giờ
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
