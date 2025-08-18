package com.poly.notification.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class QrCodeRequest {
    
    @NotBlank(message = "Data không được để trống")
    private String data;
    
    @Min(value = 100, message = "Width phải lớn hơn hoặc bằng 100")
    @Max(value = 1000, message = "Width phải nhỏ hơn hoặc bằng 1000")
    private Integer width = 300;
    
    @Min(value = 100, message = "Height phải lớn hơn hoặc bằng 100")
    @Max(value = 1000, message = "Height phải nhỏ hơn hoặc bằng 1000")
    private Integer height = 300;
    
    private String format = "PNG";
    
    private String description;
    
    // Constructors
    public QrCodeRequest() {}
    
    public QrCodeRequest(String data) {
        this.data = data;
    }
    
    public QrCodeRequest(String data, Integer width, Integer height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }
    
    // Getters and Setters
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public Integer getWidth() {
        return width;
    }
    
    public void setWidth(Integer width) {
        this.width = width;
    }
    
    public Integer getHeight() {
        return height;
    }
    
    public void setHeight(Integer height) {
        this.height = height;
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
