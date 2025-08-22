package com.poly.notification.management.client;

import com.poly.notification.management.dto.UploadResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "cloudinary-service", url = "${cloudinary.service.url:http://localhost:8085}")
public interface CloudinaryClient {

    @PostMapping(value = "/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    UploadResponseDto uploadImage(@RequestPart("file") MultipartFile file);
}
