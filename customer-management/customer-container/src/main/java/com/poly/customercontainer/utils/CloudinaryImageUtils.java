package com.poly.customercontainer.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.poly.customerapplicationservice.port.output.ImageUploadService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryImageUtils implements ImageUploadService {

    private final Cloudinary cloudinary;

    public CloudinaryImageUtils(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String upload(byte[] imageBytes) {
        try {
            Map options = ObjectUtils.asMap(
                    "format", "webp",
                    "quality", "auto",             // nén tự động
                    "width", 800,                  // resize nếu cần (tuỳ use case)
                    "crop", "limit"                // crop kiểu giới hạn, không cắt hình
            );
            Map uploadResult = cloudinary.uploader().upload(imageBytes, options); // truyền options vào đây
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Upload image failed", e);
        }
    }
}
