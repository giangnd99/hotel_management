package com.poly.cloudinary.service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.poly.cloudinary.service.dto.UploadResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadImageFileServiceImpl implements UploadImageFileService {

    private final Cloudinary cloudinary;

    @Override
    public UploadResponseDto uploadImageFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return UploadResponseDto.builder()
                        .success(false)
                        .message("File không được để trống")
                        .build();
            }

            assert file.getOriginalFilename() != null;

            String publicValue = generatePublicValue(file.getOriginalFilename());
            log.info("publicValue is: {}", publicValue);

            String extension = getFileName(file.getOriginalFilename())[1];
            log.info("extension is: {}", extension);

            File fileUpload = convert(file);
            log.info("fileUpload is: {}", fileUpload.getAbsolutePath());

            cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));
            String filePath = cloudinary.url().generate(StringUtils.join(publicValue, ".", extension));

            cleanDisk(fileUpload);

            return UploadResponseDto.builder()
                    .success(true)
                    .imageUrl(filePath)
                    .message("Upload ảnh thành công")
                    .build();

        } catch (Exception e) {
            log.error("Lỗi khi upload ảnh: {}", e.getMessage(), e);
            return UploadResponseDto.builder()
                    .success(false)
                    .message("Lỗi khi upload ảnh: " + e.getMessage())
                    .build();
        }
    }

    private void cleanDisk(File file) {
        Path filePath = file.toPath();
        try {
            log.info("filePath is: {}", filePath);
            Files.delete(filePath);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private String generatePublicValue(String originName) {
        String fileName = getFileName(originName)[0];
        return StringUtils.join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originName) {
        return originName.split("\\.");
    }
}
