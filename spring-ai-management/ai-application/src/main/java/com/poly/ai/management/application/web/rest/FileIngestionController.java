package com.poly.ai.management.application.web.rest;

import com.poly.ai.management.domain.port.input.service.DataIngestionService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai/ingestion")
@RequiredArgsConstructor
@Slf4j
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class FileIngestionController {

    private final DataIngestionService dataIngestionService;

    @PostMapping("/file")
    public ResponseEntity<String> ingestFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Vui lòng chọn một file để tải lên.");
        }
        try {
            log.info("Bắt đầu xử lý file: {}", file.getOriginalFilename());
            dataIngestionService.ingestFile(file.getInputStream(), file.getOriginalFilename());
            return ResponseEntity.ok("File '" + file.getOriginalFilename() + "' đã được nạp thành công vào Vector Store.");
        } catch (Exception e) {
            log.error("Lỗi khi xử lý file '" + file.getOriginalFilename() + "'", e);
            return ResponseEntity.status(500).body("Lỗi trong quá trình nạp dữ liệu: " + e.getMessage());
        }
    }
}
