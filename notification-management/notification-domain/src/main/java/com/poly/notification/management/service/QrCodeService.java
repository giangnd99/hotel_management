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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class QrCodeService {

    @Autowired
    private QrCodeRepository qrCodeRepository;

    @Value("${qr.code.image.format:PNG}")
    private String defaultImageFormat;

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final String DEFAULT_FORMAT = "PNG";

    private final CloudinaryQrCodeService cloudinaryQrCodeService;

    /**
     * Tạo QR code mới và upload lên Cloudinary
     */
    @Transactional
    public String createQrCode(String data) {
        try {


            Optional<QrCode> existingQrCode = qrCodeRepository.findByDataAndIsActiveTrue(data);
            if (existingQrCode.isPresent()) {
                throw new QrCodeException("QR code với data này đã tồn tại");
            }

            // Tạo QR code entity
            QrCode qrCode = new QrCode();
            qrCode.setData(data);
            qrCode.setWidth(DEFAULT_WIDTH);
            qrCode.setHeight(DEFAULT_HEIGHT);
            qrCode.setFormat(DEFAULT_FORMAT);
            qrCode.setDescription("Deposit for payment with booking ID: " + data);


            String cloudinaryUrl = cloudinaryQrCodeService.createQrCodeAndUploadToCloudinary(
                    qrCode.getData(),
                    qrCode.getWidth(),
                    qrCode.getHeight(),
                    qrCode.getFormat()
            );

            qrCode.setQrCodeImagePath(cloudinaryUrl);
            log.info("QR CODE CLOUDINARY URL: {}", cloudinaryUrl);

            // Lưu vào database
            QrCode savedQrCode = qrCodeRepository.save(qrCode);

            return cloudinaryUrl;

        } catch (Exception e) {
            throw new QrCodeException("Lỗi khi tạo QR code: " + e.getMessage(), e);
        }
    }

    /**
     * Tạo QR code đơn giản với chỉ data
     */
    public String createSimpleQrCode(String data) {

        return createQrCode(data);
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

        return createQrCodeResponse(qrCode);
    }

    /**
     * Lấy QR code theo data
     */
    @Transactional(readOnly = true)
    public QrCodeResponse getQrCodeByData(String data) {
        QrCode qrCode = qrCodeRepository.findByDataAndIsActiveTrue(data)
                .orElseThrow(() -> new QrCodeException("Không tìm thấy QR code với data: " + data));

        return createQrCodeResponse(qrCode);
    }

    /**
     * Lấy tất cả QR code đang hoạt động
     */
    @Transactional(readOnly = true)
    public List<QrCodeResponse> getAllActiveQrCodes() {
        List<QrCode> qrCodes = qrCodeRepository.findByIsActiveTrue();
        return qrCodes.stream()
                .map(this::createQrCodeResponse)
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

        // Nếu thay đổi kích thước hoặc data, tạo lại image và upload lên Cloudinary
        if ((request.getWidth() != null && !request.getWidth().equals(qrCode.getWidth())) ||
                (request.getHeight() != null && !request.getHeight().equals(qrCode.getHeight())) ||
                (request.getData() != null && !request.getData().equals(qrCode.getData()))) {

            String newCloudinaryUrl = cloudinaryQrCodeService.createQrCodeAndUploadToCloudinary(
                    qrCode.getData(),
                    qrCode.getWidth(),
                    qrCode.getHeight(),
                    qrCode.getFormat()
            );

            qrCode.setQrCodeImagePath(newCloudinaryUrl);
        }

        qrCode.setUpdatedAt(LocalDateTime.now());
        QrCode updatedQrCode = qrCodeRepository.save(qrCode);

        return createQrCodeResponse(updatedQrCode);
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

        // TODO: Có thể xóa ảnh khỏi Cloudinary nếu cần
        // cloudinaryService.deleteImage(qrCode.getQrCodeImagePath());
    }

    /**
     * Tìm kiếm QR code theo mô tả
     */
    @Transactional(readOnly = true)
    public List<QrCodeResponse> searchQrCodesByDescription(String keyword) {
        List<QrCode> qrCodes = qrCodeRepository.findByDescriptionContaining(keyword);
        return qrCodes.stream()
                .map(this::createQrCodeResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy QR code theo khoảng thời gian
     */
    @Transactional(readOnly = true)
    public List<QrCodeResponse> getQrCodesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<QrCode> qrCodes = qrCodeRepository.findByCreatedAtBetween(startDate, endDate);
        return qrCodes.stream()
                .map(this::createQrCodeResponse)
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
     * Tạo QrCodeResponse từ entity
     */
    private QrCodeResponse createQrCodeResponse(QrCode qrCode) {
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
        // Không còn trả về base64 nữa
        response.setQrCodeBase64(null);
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
            qrCode.setIsActive(false);
            qrCode.setScannedAt(LocalDateTime.now());
            qrCode.setUpdatedAt(LocalDateTime.now());

            // Lưu vào database
            QrCode savedQrCode = qrCodeRepository.save(qrCode);
            log.info("Đã đánh dấu QR code đã được quét cho booking: {}", bookingId);

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
