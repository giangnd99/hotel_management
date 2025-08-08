package com.poly.booking.management.domain.port.in.service;

public interface QrCodeService {

    void generateQrCode(String message);

    void deleteQrCode(String messageId);

    String getQrCode(String messageId);

    boolean isExist(String messageId);

    void sendQrCode(String message);
}
