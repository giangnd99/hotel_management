package com.poly.cloudinary.service.controller;

import com.poly.cloudinary.service.dto.UploadResponseDto;
import com.poly.cloudinary.service.service.UploadImageFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ImageUploadController {

    private final UploadImageFileService uploadImageFileService;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponseDto> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("Nhận request upload ảnh: {}", file.getOriginalFilename());
        
        UploadResponseDto response = uploadImageFileService.uploadImageFile(file);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Cloudinary Service is running!");
    }
}
