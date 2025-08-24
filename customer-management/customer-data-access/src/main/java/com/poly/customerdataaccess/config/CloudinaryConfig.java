package com.poly.customerdataaccess.config;

import com.cloudinary.Cloudinary;
import com.poly.customerapplicationservice.port.output.ImageUploadService;
import com.poly.customerdataaccess.image.CloudinaryImage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary("cloudinary://574523193735564:sHwwRZQfoldu5EIyd60_Sb2WieU@dhbjvvn87");
        cloudinary.config.secure = true;
        return cloudinary;
    }

    @Bean
    public ImageUploadService imageUploadService() {
        return new CloudinaryImage(cloudinary());
    }
}

