package com.poly.notification.management.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.poly.notification.management.dto.QrCodeRequest;
import com.poly.notification.management.dto.QrCodeResponse;
import com.poly.notification.management.dto.QrCodeScanResponse;
import com.poly.notification.management.entity.QrCode;
import com.poly.notification.management.exception.QrCodeException;
import com.poly.notification.management.repository.QrCodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class QrCodeService {
    
    @Autowired
    private QrCodeRepository qrCodeRepository;
    
    @Value("${qr.code.storage.path:./qr-codes}")
    private String qrCodeStoragePath;
    
    @Value("${qr.code.image.format:PNG}")
    private String defaultImageFormat;
    
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final String DEFAULT_FORMAT = "PNG";
    
    /**
     * Tạo QR code mới
     */
    public QrCodeResponse createQrCode(QrCodeRequest request) {
        try {
            // Validate input
            validateQrCodeRequest(request);
            
            // Kiểm tra xem QR code với data này đã tồn tại chưa
            Optional<QrCode> existingQrCode = qrCodeRepository.findByDataAndIsActiveTrue(request.getData());
            if (existingQrCode.isPresent()) {
                throw new QrCodeException("QR code với data này đã tồn tại");
            }
            
            // Tạo QR code entity
            QrCode qrCode = new QrCode();
            qrCode.setData(request.getData());
            qrCode.setWidth(request.getWidth() != null ? request.getWidth() : DEFAULT_WIDTH);
            qrCode.setHeight(request.getHeight() != null ? request.getHeight() : DEFAULT_HEIGHT);
            qrCode.setFormat(request.getFormat() != null ? request.getFormat() : DEFAULT_FORMAT);
            qrCode.setDescription(request.getDescription());
            
            // Tạo QR code image
            BufferedImage qrCodeImage = generateQrCodeImage(
                request.getData(), 
                qrCode.getWidth(), 
                qrCode.getHeight()
            );
            
            // Lưu image vào file system
            String imagePath = saveQrCodeImage(qrCodeImage, qrCode.getFormat());
            qrCode.setQrCodeImagePath(imagePath);
            
            // Lưu vào database
            QrCode savedQrCode = qrCodeRepository.save(qrCode);
            
            // Convert image thành base64 để trả về
            String base64Image = convertImageToBase64(qrCodeImage, qrCode.getFormat());
            
            return createQrCodeResponse(savedQrCode, base64Image);
            
        } catch (Exception e) {
            throw new QrCodeException("Lỗi khi tạo QR code: " + e.getMessage(), e);
        }
    }
    
    /**
     * Tạo QR code đơn giản với chỉ data
     */
    public QrCodeResponse createSimpleQrCode(String data) {
        QrCodeRequest request = new QrCodeRequest(data);
        return createQrCode(request);
    }
    
    /**
     * Lấy QR code theo ID
     */
    @Transactional(readOnly = true)
    public QrCodeResponse getQrCodeById(Long id) {
        QrCode qrCode = qrCodeRepository.findById(id)
            .orElseThrow(() -> new QrCodeException("Không tìm thấy QR code với ID: " + id));
        
        if (!qrCode.getIsActive()) {
            throw new QrCodeException("QR code đã bị xóa");
        }
        
        String base64Image = getQrCodeImageAsBase64(qrCode);
        return createQrCodeResponse(qrCode, base64Image);
    }
    
    /**
     * Lấy QR code theo data
     */
    @Transactional(readOnly = true)
    public QrCodeResponse getQrCodeByData(String data) {
        QrCode qrCode = qrCodeRepository.findByDataAndIsActiveTrue(data)
            .orElseThrow(() -> new QrCodeException("Không tìm thấy QR code với data: " + data));
        
        String base64Image = getQrCodeImageAsBase64(qrCode);
        return createQrCodeResponse(qrCode, base64Image);
    }
    
    /**
     * Lấy tất cả QR code đang hoạt động
     */
    @Transactional(readOnly = true)
    public List<QrCodeResponse> getAllActiveQrCodes() {
        List<QrCode> qrCodes = qrCodeRepository.findByIsActiveTrue();
        return qrCodes.stream()
            .map(qrCode -> {
                String base64Image = getQrCodeImageAsBase64(qrCode);
                return createQrCodeResponse(qrCode, base64Image);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Cập nhật QR code
     */
    public QrCodeResponse updateQrCode(Long id, QrCodeRequest request) {
        QrCode qrCode = qrCodeRepository.findById(id)
            .orElseThrow(() -> new QrCodeException("Không tìm thấy QR code với ID: " + id));
        
        if (!qrCode.getIsActive()) {
            throw new QrCodeException("Không thể cập nhật QR code đã bị xóa");
        }
        
        // Cập nhật thông tin
        if (request.getData() != null && !request.getData().equals(qrCode.getData())) {
            // Kiểm tra xem data mới có trùng với QR code khác không
            Optional<QrCode> existingQrCode = qrCodeRepository.findByDataAndIsActiveTrue(request.getData());
            if (existingQrCode.isPresent() && !existingQrCode.get().getId().equals(id)) {
                throw new QrCodeException("QR code với data này đã tồn tại");
            }
            qrCode.setData(request.getData());
        }
        
        if (request.getWidth() != null) qrCode.setWidth(request.getWidth());
        if (request.getHeight() != null) qrCode.setHeight(request.getHeight());
        if (request.getFormat() != null) qrCode.setFormat(request.getFormat());
        if (request.getDescription() != null) qrCode.setDescription(request.getDescription());
        
        // Nếu thay đổi kích thước hoặc data, tạo lại image
        if ((request.getWidth() != null && !request.getWidth().equals(qrCode.getWidth())) ||
            (request.getHeight() != null && !request.getHeight().equals(qrCode.getHeight())) ||
            (request.getData() != null && !request.getData().equals(qrCode.getData()))) {
            
            BufferedImage newImage = generateQrCodeImage(qrCode.getData(), qrCode.getWidth(), qrCode.getHeight());
            String newImagePath = saveQrCodeImage(newImage, qrCode.getFormat());
            
            // Xóa file cũ
            deleteQrCodeImage(qrCode.getQrCodeImagePath());
            
            qrCode.setQrCodeImagePath(newImagePath);
        }
        
        qrCode.setUpdatedAt(LocalDateTime.now());
        QrCode updatedQrCode = qrCodeRepository.save(qrCode);
        
        String base64Image = getQrCodeImageAsBase64(updatedQrCode);
        return createQrCodeResponse(updatedQrCode, base64Image);
    }
    
    /**
     * Xóa mềm QR code
     */
    public void deleteQrCode(Long id) {
        QrCode qrCode = qrCodeRepository.findById(id)
            .orElseThrow(() -> new QrCodeException("Không tìm thấy QR code với ID: " + id));
        
        if (!qrCode.getIsActive()) {
            throw new QrCodeException("QR code đã bị xóa");
        }
        
        // Xóa mềm
        qrCode.setIsActive(false);
        qrCode.setUpdatedAt(LocalDateTime.now());
        qrCodeRepository.save(qrCode);
        
        // Xóa file image
        deleteQrCodeImage(qrCode.getQrCodeImagePath());
    }
    
    /**
     * Tìm kiếm QR code theo mô tả
     */
    @Transactional(readOnly = true)
    public List<QrCodeResponse> searchQrCodesByDescription(String keyword) {
        List<QrCode> qrCodes = qrCodeRepository.findByDescriptionContaining(keyword);
        return qrCodes.stream()
            .map(qrCode -> {
                String base64Image = getQrCodeImageAsBase64(qrCode);
                return createQrCodeResponse(qrCode, base64Image);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Lấy QR code theo khoảng thời gian
     */
    @Transactional(readOnly = true)
    public List<QrCodeResponse> getQrCodesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<QrCode> qrCodes = qrCodeRepository.findByCreatedAtBetween(startDate, endDate);
        return qrCodes.stream()
            .map(qrCode -> {
                String base64Image = getQrCodeImageAsBase64(qrCode);
                return createQrCodeResponse(qrCode, base64Image);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Đếm số lượng QR code đang hoạt động
     */
    @Transactional(readOnly = true)
    public long countActiveQrCodes() {
        return qrCodeRepository.countByIsActiveTrue();
    }
    
    /**
     * Tạo QR code image sử dụng ZXing
     */
    private BufferedImage generateQrCodeImage(String data, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 2);
            
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hints);
            
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (bitMatrix.get(x, y)) {
                        graphics.fillRect(x, y, 1, 1);
                    }
                }
            }
            
            graphics.dispose();
            return image;
            
        } catch (WriterException e) {
            throw new QrCodeException("Lỗi khi tạo QR code image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lưu QR code image vào file system
     */
    private String saveQrCodeImage(BufferedImage image, String format) {
        try {
            // Tạo thư mục nếu chưa tồn tại
            Path storagePath = Paths.get(qrCodeStoragePath);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
            
            // Tạo tên file duy nhất
            String fileName = "qr_" + System.currentTimeMillis() + "." + format.toLowerCase();
            Path filePath = storagePath.resolve(fileName);
            
            // Lưu image
            ImageIO.write(image, format, filePath.toFile());
            
            return filePath.toString();
            
        } catch (IOException e) {
            throw new QrCodeException("Lỗi khi lưu QR code image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Xóa QR code image file
     */
    private void deleteQrCodeImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Path path = Paths.get(imagePath);
                if (Files.exists(path)) {
                    Files.delete(path);
                }
            } catch (IOException e) {
                // Log lỗi nhưng không throw exception
                System.err.println("Lỗi khi xóa file image: " + e.getMessage());
            }
        }
    }
    
    /**
     * Convert image thành base64 string
     */
    private String convertImageToBase64(BufferedImage image, String format) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, format, baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new QrCodeException("Lỗi khi convert image thành base64: " + e.getMessage(), e);
        }
    }
    
    /**
     * Lấy QR code image từ file system và convert thành base64
     */
    private String getQrCodeImageAsBase64(QrCode qrCode) {
        if (qrCode.getQrCodeImagePath() == null || qrCode.getQrCodeImagePath().isEmpty()) {
            return null;
        }
        
        try {
            Path imagePath = Paths.get(qrCode.getQrCodeImagePath());
            if (!Files.exists(imagePath)) {
                return null;
            }
            
            BufferedImage image = ImageIO.read(imagePath.toFile());
            return convertImageToBase64(image, qrCode.getFormat());
            
        } catch (IOException e) {
            throw new QrCodeException("Lỗi khi đọc QR code image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Tạo QrCodeResponse từ entity
     */
    private QrCodeResponse createQrCodeResponse(QrCode qrCode, String base64Image) {
        QrCodeResponse response = new QrCodeResponse();
        response.setId(qrCode.getId());
        response.setData(qrCode.getData());
        response.setQrCodeImagePath(qrCode.getQrCodeImagePath());
        response.setWidth(qrCode.getWidth());
        response.setHeight(qrCode.getHeight());
        response.setFormat(qrCode.getFormat());
        response.setCreatedAt(qrCode.getCreatedAt());
        response.setUpdatedAt(qrCode.getUpdatedAt());
        response.setIsActive(qrCode.getIsActive());
        response.setDescription(qrCode.getDescription());
        response.setQrCodeBase64(base64Image);
        return response;
    }
    
    /**
     * Validate QrCodeRequest
     */
    private void validateQrCodeRequest(QrCodeRequest request) {
        if (request == null) {
            throw new QrCodeException("Request không được null");
        }
        
        if (request.getData() == null || request.getData().trim().isEmpty()) {
            throw new QrCodeException("Data không được để trống");
        }
        
        if (request.getData().length() > 1000) {
            throw new QrCodeException("Data quá dài (tối đa 1000 ký tự)");
        }
        
        if (request.getWidth() != null && (request.getWidth() < 100 || request.getWidth() > 1000)) {
            throw new QrCodeException("Width phải từ 100 đến 1000 pixels");
        }
        
        if (request.getHeight() != null && (request.getHeight() < 100 || request.getHeight() > 1000)) {
            throw new QrCodeException("Height phải từ 100 đến 1000 pixels");
        }
        
        if (request.getFormat() != null && !isValidImageFormat(request.getFormat())) {
            throw new QrCodeException("Format không hợp lệ. Hỗ trợ: PNG, JPG, JPEG, GIF");
        }
    }
    
    /**
     * Kiểm tra format image có hợp lệ không
     */
    private boolean isValidImageFormat(String format) {
        if (format == null) return false;
        String upperFormat = format.toUpperCase();
        return upperFormat.equals("PNG") || upperFormat.equals("JPG") || 
               upperFormat.equals("JPEG") || upperFormat.equals("GIF");
    }
    
    /**
     * Quét QR code và đánh dấu đã được quét
     * Đây là method duy nhất để xử lý việc quét QR code từ frontend
     * 
     * @param bookingId ID của booking từ QR code
     * @return QrCodeScanResponse chứa thông tin đầy đủ về booking và customer
     */
    public QrCodeScanResponse scanQrCodeAndMarkAsScanned(String bookingId) {
        try {
            log.info("Bắt đầu xử lý quét QR code cho booking: {}", bookingId);
            
            // Validate input
            if (bookingId == null || bookingId.trim().isEmpty()) {
                throw new QrCodeException("Booking ID không được để trống");
            }
            
            // Tìm QR code theo bookingId
            Optional<QrCode> qrCodeOpt = qrCodeRepository.findByDataAndIsActiveTrue(bookingId);
            if (qrCodeOpt.isEmpty()) {
                log.warn("Không tìm thấy QR code cho booking: {}", bookingId);
                return new QrCodeScanResponse(bookingId, "Không tìm thấy QR code cho booking này");
            }
            
            QrCode qrCode = qrCodeOpt.get();
            
            // Kiểm tra xem QR code đã được quét chưa
            if (qrCode.getIsScanned()) {
                log.warn("QR code đã được quét trước đó cho booking: {}", bookingId);
                return new QrCodeScanResponse(bookingId, "QR code này đã được quét trước đó");
            }
            
            // Đánh dấu QR code đã được quét
            qrCode.setIsScanned(true);
            qrCode.setScannedAt(LocalDateTime.now());
            qrCode.setUpdatedAt(LocalDateTime.now());
            
            // Lưu vào database
            QrCode savedQrCode = qrCodeRepository.save(qrCode);
            log.info("Đã đánh dấu QR code đã được quét cho booking: {}", bookingId);
            
            // Tạo response với thông tin đầy đủ
            QrCodeScanResponse response = createQrCodeScanResponse(savedQrCode, bookingId);
            
            log.info("Hoàn thành xử lý quét QR code cho booking: {}", bookingId);
            return response;
            
        } catch (Exception e) {
            log.error("Lỗi khi xử lý quét QR code cho booking: {}", bookingId, e);
            throw new QrCodeException("Không thể xử lý quét QR code: " + e.getMessage(), e);
        }
    }
    
    /**
     * Tạo QrCodeScanResponse từ QrCode entity
     * Trong thực tế, bạn sẽ cần gọi các service khác để lấy thông tin booking và customer
     */
    private QrCodeScanResponse createQrCodeScanResponse(QrCode qrCode, String bookingId) {
        // TODO: Trong thực tế, bạn sẽ cần gọi:
        // - BookingService để lấy thông tin booking
        // - CustomerService để lấy thông tin customer
        // - RoomService để lấy thông tin phòng
        
        // Hiện tại tôi sẽ tạo mock data để demo
        return new QrCodeScanResponse(bookingId, "QR code đã được quét thành công!")
                .withQrCodeId(qrCode.getId().toString())
                .withQrCodeStatus("SCANNED")
                .withQrCodeScannedAt(qrCode.getScannedAt())
                .withCustomerId("CUST001")
                .withCustomerName("Nguyễn Văn A")
                .withCustomerEmail("nguyenvana@example.com")
                .withCustomerPhone("+84 123 456 789")
                .withRoomNumber("101")
                .withRoomType("Deluxe")
                .withCheckInDate(LocalDateTime.now().plusDays(1))
                .withCheckOutDate(LocalDateTime.now().plusDays(3))
                .withBookingStatus("CONFIRMED");
    }
}
