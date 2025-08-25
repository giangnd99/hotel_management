package com.poly.ai.management.domain.service.rag.scheduler;

import com.poly.ai.management.domain.dto.*;
import com.poly.ai.management.domain.port.input.service.DataIngestionService;
import com.poly.ai.management.domain.port.output.feign.RoomFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Slf4j
@RequiredArgsConstructor
public class DataIngestionServiceImpl implements DataIngestionService {

    private final VectorStore vectorStore;
    private final RoomFeign roomFeign;
    @Value("${booking.room}")
    private String bookingRoomUrl;
    @Override
    public void ingestHotelData() {
        try {
            log.info("Bắt đầu nạp dữ liệu khách sạn vào AI Vector Store...");

            List<Document> allDocuments = new ArrayList<>();

            ingestRoomData(allDocuments);

            ingestMaintenanceData(allDocuments);

            ingestCleaningData(allDocuments);

            ingestServiceData(allDocuments);

            ingestGuestData(allDocuments);

            ingestStatisticsData(allDocuments);

            ingestAvailableRoomsAsDocuments(allDocuments);

            if (allDocuments.isEmpty()) {
                log.warn("Không có dữ liệu nào để nạp vào Vector Store");
                return;
            }

            TokenTextSplitter textSplitter = new TokenTextSplitter();
            List<Document> chunks = textSplitter.apply(allDocuments);
            log.info("Đã chia thành {} chunks để đưa vào Vector Store.", chunks.size());

            vectorStore.add(chunks);
            log.info("Đã thêm {} chunks vào Vector Store thành công.", chunks.size());

        } catch (Exception e) {
            log.error("Lỗi trong quá trình nạp dữ liệu khách sạn: {}", e.getMessage(), e);
        }
    }

    private void ingestRoomData(List<Document> documents) {
        try {
            ResponseEntity<List<RoomResponse>> response = roomFeign.getAllRooms();
            if (response.getBody() != null && !response.getBody().isEmpty()) {
                List<RoomResponse> rooms = response.getBody();
                log.info("Đã tìm thấy {} phòng từ khách sạn", rooms.size());

                for (RoomResponse room : rooms) {
                    String content = String.format("""
                                    THÔNG TIN PHÒNG KHÁCH SẠN 5 SAO VIỆT NAM
                                    Số phòng: %s
                                    Tầng: %d
                                    Loại phòng: %s
                                    Trạng thái: %s
                                    """,
                            room.getRoomNumber(),
                            room.getFloor(),
                            room.getRoomType() != null ? room.getRoomType().getTypeName() : "Chưa xác định",
                            room.getRoomStatus()
                    );


                    Document document = new Document(content);
                    document.getMetadata().put("type", "room");
                    document.getMetadata().put("roomNumber", room.getRoomNumber());
                    document.getMetadata().put("floor", String.valueOf(room.getFloor()));
                    document.getMetadata().put("roomStatus", room.getRoomStatus());
                    document.getMetadata().put("category", "room_information");

                    ingestRoomTypeData(documents, room.getRoomType());
                    rooms.forEach(
                            roomResponse -> roomResponse.getRoomType().getFurnitureRequirements().forEach(
                                    furnitureRequirementResponse -> {
                                        ingestFurnitureData(documents, furnitureRequirementResponse.getFurniture());
                                    }));

                    documents.add(document);
                }
            }
        } catch (Exception e) {
            log.error("Lỗi khi nạp dữ liệu phòng: {}", e.getMessage());
        }
    }

    private void ingestRoomTypeData(List<Document> documents, RoomTypeResponse roomType) {
        try {
            if (roomType != null) {

                String cancellationRegular = "Chính sách hủy phòng: Khách hàng hủy trước 24 giờ sẽ được hoàn lại 100% tiền. Hủy trong vòng 24 giờ sẽ mất phí 30%";

                String content = String.format("""
                                LOẠI PHÒNG KHÁCH SẠN 5 SAO VIỆT NAM
                                Tên loại: %s
                                Mô tả: %s
                                Giá cơ bản: %s VND/đêm
                                Sức chứa tối đa: %d người
                                Chính sách hủy: %s
                                """,
                        roomType.getTypeName(),
                        roomType.getDescription(),
                        roomType.getBasePrice(),
                        roomType.getMaxOccupancy(),
                        cancellationRegular
                );

                Document document = new Document(content);
                document.getMetadata().put("type", "room_type");
                document.getMetadata().put("basePrice", String.valueOf(roomType.getBasePrice()));
                document.getMetadata().put("description", roomType.getDescription());
                document.getMetadata().put("typeName", roomType.getTypeName());
                document.getMetadata().put("maxOccupancy", String.valueOf(roomType.getMaxOccupancy()));
                document.getMetadata().put("cancellationRegular", cancellationRegular);
                document.getMetadata().put("category", "room_type_information");

                documents.add(document);
            }

        } catch (Exception e) {
            log.error("Lỗi khi nạp dữ liệu loại phòng: {}", e.getMessage());
        }
    }

    private void ingestFurnitureData(List<Document> documents, FurnitureResponse furniture) {
        try {
            String content = String.format("""
                            NỘI THẤT KHÁCH SẠN 5 SAO VIỆT NAM
                            Tên nội thất: %s
                            Giá: %s
                            """,
                    furniture.getName(),
                    furniture.getPrice() != null ? furniture.getPrice() : "Chưa cập nhật"
            );

            Document document = new Document(content);
            document.getMetadata().put("type", "furniture");
            document.getMetadata().put("furnitureName", furniture.getName());
            document.getMetadata().put("category", "furniture_information");

            documents.add(document);

        } catch (Exception e) {
            log.error("Lỗi khi nạp dữ liệu nội thất: {}", e.getMessage());
        }
    }

    private void ingestAvailableRoomsAsDocuments(List<Document> allDocuments) {
        try {
            ResponseEntity<List<RoomResponse>> response = roomFeign.getAllRooms();
            if (response.getBody() == null || response.getBody().isEmpty()) {
                log.warn("Không có dữ liệu phòng để đưa vào documents.");
                return;
            }

            List<RoomResponse> availableRooms = response.getBody().stream()
                    .filter(room -> "VACANT".equals(room.getRoomStatus()))
                    .toList();

            if (availableRooms.isEmpty()) {
                log.info("Không có phòng trống nào để nạp vào documents.");
                return;
            }

            for (RoomResponse room : availableRooms) {
                String content = String.format(
                        "THÔNG TIN PHÒNG TRỐNG VÀ ĐẶT PHÒNG KHÁCH SẠN NIKKA 5 SAO VIỆT NAM\n" +
                                "Phòng số: %s\n" +
                                "Loại phòng: %s\n" +
                                "Mô tả: %s\n" +
                                "Giá: %s VND/đêm\n" +
                                "Đường link đặt phòng: %s",
                        room.getRoomNumber(),
                        room.getRoomType().getTypeName(),
                        room.getRoomType().getDescription(),
                        room.getRoomType().getBasePrice(),
                        bookingRoomUrl + room.getId()
                );

                Document document = new Document(content);
                document.getMetadata().put("type", "booking_information");
                document.getMetadata().put("roomNumber", room.getRoomNumber());
                document.getMetadata().put("roomStatus", room.getRoomStatus());
                document.getMetadata().put("basePrice", room.getRoomType().getBasePrice());
                document.getMetadata().put("bookingUrl", bookingRoomUrl + room.getRoomNumber());
                document.getMetadata().put("category", "booking_availability");

                allDocuments.add(document);
            }
            log.info("Đã tạo thành công {} documents cho các phòng trống.", availableRooms.size());

        } catch (Exception e) {
            log.error("Lỗi khi nạp dữ liệu phòng trống: {}", e.getMessage(), e);
        }
    }

    private void ingestMaintenanceData(List<Document> documents) {
        try {
            ResponseEntity<List<RoomMaintenanceResponse>> response = roomFeign.getAllMaintenance();
            if (response.getBody() != null && !response.getBody().isEmpty()) {
                List<RoomMaintenanceResponse> maintenance = response.getBody();
                log.info("Đã tìm thấy {} yêu cầu bảo trì", maintenance.size());

                for (RoomMaintenanceResponse item : maintenance) {
                    String content = String.format("""
                                    BẢO TRÌ PHÒNG KHÁCH SẠN 5 SAO VIỆT NAM
                                    Số phòng: %s
                                    Loại vấn đề: %s
                                    Mức độ ưu tiên: %s
                                    Mô tả: %s
                                    Trạng thái: %s
                                    Yêu cầu bởi: %s
                                    Được giao cho: %s
                                    Chi phí ước tính: %s VND
                                    Ngày lên lịch: %s
                                    Bắt đầu: %s
                                    Hoàn thành: %s
                                    Ghi chú: %s
                                    """,
                            item.getRoomNumber(),
                            item.getIssueType(),
                            item.getPriority(),
                            item.getDescription(),
                            item.getStatus(),
                            item.getRequestedBy(),
                            item.getAssignedTo() != null ? item.getAssignedTo() : "Chưa phân công",
                            item.getEstimatedCost() != null ? item.getEstimatedCost() : "Chưa ước tính",
                            item.getScheduledDate() != null ? item.getScheduledDate() : "Chưa lên lịch",
                            item.getStartedAt() != null ? item.getStartedAt() : "Chưa bắt đầu",
                            item.getCompletedAt() != null ? item.getCompletedAt() : "Chưa hoàn thành",
                            item.getNotes() != null ? item.getNotes() : "Không có"
                    );

                    Document document = new Document(content);
                    document.getMetadata().put("type", "maintenance");
                    document.getMetadata().put("roomNumber", item.getRoomNumber());
                    document.getMetadata().put("status", item.getStatus());
                    document.getMetadata().put("priority", item.getPriority());
                    document.getMetadata().put("category", "maintenance_information");

                    documents.add(document);
                }
            }
        } catch (Exception e) {
            log.error("Lỗi khi nạp dữ liệu bảo trì: {}", e.getMessage());
        }
    }

    private void ingestCleaningData(List<Document> documents) {
        try {
            ResponseEntity<List<RoomCleaningResponse>> response = roomFeign.getAllCleaning();
            if (response.getBody() != null && !response.getBody().isEmpty()) {
                List<RoomCleaningResponse> cleaning = response.getBody();
                log.info("Đã tìm thấy {} yêu cầu dọn dẹp", cleaning.size());

                for (RoomCleaningResponse item : cleaning) {
                    String content = String.format("""
                                    DỌN DẸP PHÒNG KHÁCH SẠN 5 SAO VIỆT NAM
                                    Số phòng: %s
                                    Loại dọn dẹp: %s
                                    Mức độ ưu tiên: %s
                                    Mô tả: %s
                                    Trạng thái: %s
                                    Yêu cầu bởi: %s
                                    Được giao cho: %s
                                    Ngày lên lịch: %s
                                    Bắt đầu: %s
                                    Hoàn thành: %s
                                    Sản phẩm dọn dẹp: %s
                                    Hướng dẫn đặc biệt: %s
                                    Ghi chú: %s
                                    """,
                            item.getRoomNumber(),
                            item.getCleaningType(),
                            item.getPriority(),
                            item.getDescription(),
                            item.getStatus(),
                            item.getRequestedBy(),
                            item.getAssignedTo() != null ? item.getAssignedTo() : "Chưa phân công",
                            item.getScheduledDate() != null ? item.getScheduledDate() : "Chưa lên lịch",
                            item.getStartedAt() != null ? item.getStartedAt() : "Chưa bắt đầu",
                            item.getCompletedAt() != null ? item.getCompletedAt() : "Chưa hoàn thành",
                            item.getCleaningProducts() != null ? item.getCleaningProducts() : "Chưa cập nhật",
                            item.getSpecialInstructions() != null ? item.getSpecialInstructions() : "Không có",
                            item.getNotes() != null ? item.getNotes() : "Không có"
                    );

                    Document document = new Document(content);
                    document.getMetadata().put("type", "cleaning");
                    document.getMetadata().put("roomNumber", item.getRoomNumber());
                    document.getMetadata().put("status", item.getStatus());
                    document.getMetadata().put("priority", item.getPriority());
                    document.getMetadata().put("category", "cleaning_information");

                    documents.add(document);
                }
            }
        } catch (Exception e) {
            log.error("Lỗi khi nạp dữ liệu dọn dẹp: {}", e.getMessage());
        }
    }

    private void ingestServiceData(List<Document> documents) {
        try {
            ResponseEntity<List<RoomServiceResponse>> response = roomFeign.getAllServices();
            if (response.getBody() != null && !response.getBody().isEmpty()) {
                List<RoomServiceResponse> services = response.getBody();
                log.info("Đã tìm thấy {} dịch vụ phòng", services.size());

                for (RoomServiceResponse item : services) {
                    String content = String.format("""
                                    DỊCH VỤ PHÒNG KHÁCH SẠN 5 SAO VIỆT NAM
                                    Số phòng: %s
                                    Tên khách: %s
                                    Loại dịch vụ: %s
                                    Tên dịch vụ: %s
                                    Mô tả: %s
                                    Số lượng: %d
                                    Đơn giá: %s VND
                                    Tổng tiền: %s VND
                                    Trạng thái: %s
                                    Yêu cầu bởi: %s
                                    Thời gian yêu cầu: %s
                                    Thời gian giao: %s
                                    Hướng dẫn đặc biệt: %s
                                    Ghi chú ăn uống: %s
                                    """,
                            item.getRoomNumber(),
                            item.getGuestName(),
                            item.getServiceType(),
                            item.getServiceName(),
                            item.getDescription(),
                            item.getQuantity(),
                            item.getUnitPrice(),
                            item.getTotalPrice(),
                            item.getStatus(),
                            item.getRequestedBy(),
                            item.getRequestedAt() != null ? item.getRequestedAt() : "Chưa cập nhật",
                            item.getDeliveredAt() != null ? item.getDeliveredAt() : "Chưa giao",
                            item.getSpecialInstructions() != null ? item.getSpecialInstructions() : "Không có",
                            item.getDietaryNotes() != null ? item.getDietaryNotes() : "Không có"
                    );

                    Document document = new Document(content);
                    document.getMetadata().put("type", "room_service");
                    document.getMetadata().put("roomNumber", item.getRoomNumber());
                    document.getMetadata().put("serviceType", item.getServiceType());
                    document.getMetadata().put("status", item.getStatus());
                    document.getMetadata().put("category", "room_service_information");

                    documents.add(document);
                }
            }
        } catch (Exception e) {
            log.error("Lỗi khi nạp dữ liệu dịch vụ phòng: {}", e.getMessage());
        }
    }

    private void ingestGuestData(List<Document> documents) {
        try {
            ResponseEntity<List<GuestResponse>> response = roomFeign.getAllGuests(0, 50);
            if (response.getBody() != null && !response.getBody().isEmpty()) {
                List<GuestResponse> guests = response.getBody();
                log.info("Đã tìm thấy {} khách hàng", guests.size());

                for (GuestResponse guest : guests) {
                    String content = String.format("""
                                    THÔNG TIN KHÁCH HÀNG KHÁCH SẠN 5 SAO VIỆT NAM
                                    Họ và tên: %s %s
                                    Tên đầy đủ: %s
                                    Số điện thoại: %s
                                    Email: %s
                                    Quốc tịch: %s
                                    Địa chỉ: %s
                                    Ngày sinh: %s
                                    Giới tính: %s
                                    Yêu cầu đặc biệt: %s
                                    Cấp độ thành viên: %s
                                    Ngôn ngữ ưa thích: %s
                                    Hạn chế ăn uống: %s
                                    """,
                            guest.getFirstName(),
                            guest.getLastName(),
                            guest.getFullName(),
                            guest.getPhone(),
                            guest.getEmail(),
                            guest.getNationality(),
                            guest.getAddress(),
                            guest.getDateOfBirth() != null ? guest.getDateOfBirth() : "Chưa cập nhật",
                            guest.getGender(),
                            guest.getSpecialRequests() != null ? guest.getSpecialRequests() : "Không có",
                            guest.getLoyaltyLevel() != null ? guest.getLoyaltyLevel() : "Chưa xác định",
                            guest.getPreferredLanguage() != null ? guest.getPreferredLanguage() : "Tiếng Việt",
                            guest.getDietaryRestrictions() != null ? guest.getDietaryRestrictions() : "Không có"
                    );

                    Document document = new Document(content);
                    document.getMetadata().put("type", "guest");
                    document.getMetadata().put("guestName", guest.getFullName());
                    document.getMetadata().put("nationality", guest.getNationality());
                    document.getMetadata().put("category", "guest_information");

                    documents.add(document);
                }
            }
        } catch (Exception e) {
            log.error("Lỗi khi nạp dữ liệu khách hàng: {}", e.getMessage());
        }
    }

    private void ingestStatisticsData(List<Document> documents) {
        try {
            String content = """
                    THỐNG KÊ TỔNG QUAN KHÁCH SẠN NIKKA 5 SAO VIỆT NAM
                    THÔNG TIN CHỦ KHÁCH SẠN : NGUYỄN ĐẰNG GIANG Số điện thoại 0779755739
                    Email: nguyendanggiang99@gmail.com
                    Dịa chỉ duy nhất: Hồ Chí Minh, Việt Nam
                    THÔNG TIN CHUNG:
                    - Loại khách sạn: 5 sao quốc tế
                    - Tiêu chuẩn: ISO 9001, ISO 14001
                    - Chứng nhận: Green Hotel, Safe Travel
                    - Ngôn ngữ phục vụ: Tiếng Việt, Tiếng Anh, Tiếng Pháp, Tiếng Nhật
                    
                    DỊCH VỤ ĐẶC BIỆT:
                    - Spa & Wellness Center
                    - Bể bơi vô cực với view sông
                    - Phòng gym 24/7
                    - Nhà hàng ẩm thực Việt Nam và quốc tế
                    - Bar rooftop với view toàn cảnh thành phố
                    - Dịch vụ đưa đón sân bay
                    - Dịch vụ tour du lịch nội thành
                    
                    TIỆN ÍCH PHÒNG:
                    - Wi-Fi tốc độ cao miễn phí
                    - Smart TV với Netflix
                    - Mini bar với đồ uống địa phương
                    - Bộ đồ tắm cao cấp
                    - Dịch vụ phòng 24/7
                    - Hệ thống an ninh hiện đại
                    
                    CHÍNH SÁCH ĐẶC BIỆT:
                    - Early check-in từ 8:00 sáng
                    - Late check-out đến 2:00 chiều
                    - Dịch vụ giặt ủi trong ngày
                    - Dịch vụ đặt tour và vé máy bay
                    - Hỗ trợ đặc biệt cho khách hàng VIP
                    *****cung cấp link booking khi được hỏi đến booking
                    #####Không được tiết lộ bất kì mã uuid nào hoặc Id trực tiếp cho khách hàng #######
                    """;

            Document document = new Document(content);
            document.getMetadata().put("type", "hotel_statistics");
            document.getMetadata().put("category", "hotel_overview");

            documents.add(document);
        } catch (Exception e) {
            log.error("Lỗi khi nạp dữ liệu thống kê: {}", e.getMessage());
        }
    }

    @Scheduled(fixedRate = 3600000)
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
