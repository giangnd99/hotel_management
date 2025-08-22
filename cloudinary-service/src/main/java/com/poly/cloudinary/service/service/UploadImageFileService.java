package com.poly.cloudinary.service.service;

import com.poly.cloudinary.service.dto.UploadResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface UploadImageFileService {

    UploadResponseDto uploadImageFile(MultipartFile file);

}
