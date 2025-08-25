package com.poly.notification.management.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.poly.notification.management.client.CloudinaryClient;
import com.poly.notification.management.dto.UploadResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryQrCodeService {

    private final CloudinaryClient cloudinaryClient;

    /**
     * Tạo QR code và upload lên Cloudinary
     * @param width Chiều rộng (mặc định 300)
     * @param height Chiều cao (mặc định 300)
     * @param format Format ảnh (mặc định PNG)
     * @return URL của ảnh trên Cloudinary
     */
    public String createQrCodeAndUploadToCloudinary(BufferedImage qrCodeImage, Integer width, Integer height, String format) {
        try {

            // Set default values
            int qrWidth = width != null ? width : 300;
            int qrHeight = height != null ? height : 300;
            String qrFormat = format != null ? format : "PNG";

            // Validate dimensions
            if (qrWidth < 100 || qrWidth > 1000 || qrHeight < 100 || qrHeight > 1000) {
                throw new IllegalArgumentException("Kích thước phải từ 100 đến 1000 pixels");
            }

            // Convert image thành MultipartFile
            MultipartFile multipartFile = convertImageToMultipartFile(qrCodeImage, qrFormat);

            // Upload lên Cloudinary
            log.info("Uploading QR code lên Cloudinary...");
            UploadResponseDto uploadResponse = cloudinaryClient.uploadImage(multipartFile);

            if (uploadResponse.isSuccess()) {
                log.info("Upload thành công, URL: {}", uploadResponse.getImageUrl());
                return uploadResponse.getImageUrl();
            } else {
                throw new RuntimeException("Upload thất bại: " + uploadResponse.getMessage());
            }

        } catch (Exception e) {
            log.error("Lỗi khi tạo QR code và upload lên Cloudinary", e);
            throw new RuntimeException("Không thể tạo QR code: " + e.getMessage(), e);
        }
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
            throw new RuntimeException("Lỗi khi tạo QR code image: " + e.getMessage(), e);
        }
    }

    /**
     * Convert BufferedImage thành MultipartFile
     */
    private MultipartFile convertImageToMultipartFile(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        javax.imageio.ImageIO.write(image, format, baos);
        byte[] imageBytes = baos.toByteArray();

        String fileName = "qr_" + System.currentTimeMillis() + "." + format.toLowerCase();
        String contentType = "image/" + format.toLowerCase();

        return new MultipartFile() {
            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return contentType;
            }

            @Override
            public boolean isEmpty() {
                return imageBytes.length == 0;
            }

            @Override
            public long getSize() {
                return imageBytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return imageBytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(imageBytes);
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
                // Không cần implement cho trường hợp này
            }
        };
    }
}
