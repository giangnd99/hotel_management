package com.poly.booking.management.domain.entity;

import com.poly.booking.management.domain.exception.QRCodeDomainException;

import java.io.File;
import java.nio.file.Path;

public class QRCodeCheckIn {

    private String qrCodeData;
    private Path qrCodePath;
    private String qrCodeUrl;
    private File qrCodeFile;

    public static final String QRCODE_FORMAT = "PNG";
    public static final int SIZE_QRCODE = 200;
    public static final String QR_CODE_PATH_SYSTEM_PROPERTY = "src/main/resources/static/images/qr_code_check_in/";

    public void initQRCode(String qrCodeData) {
        if (qrCodeFile.exists())
            throw new QRCodeDomainException("QRCode file already exist");
        if (qrCodeData == null)
            throw new QRCodeDomainException("QRCode data is null");
        this.qrCodeData = qrCodeData;
        initFilePath();
    }


    public File initQRCodeFile() {
        if (qrCodePath == null)
            throw new QRCodeDomainException("QRCode path is null");

        this.qrCodeFile = new File(qrCodePath.toString());
        return qrCodeFile;

    }

    private boolean isFolderExist() {
        if (qrCodeData == null || qrCodeData.isEmpty()) {
            File folder = new File(QR_CODE_PATH_SYSTEM_PROPERTY);
            if (!folder.exists()) {
                return folder.mkdirs();
            }
        }
        throw new QRCodeDomainException("QRCode data is empty");
    }

    public void initFilePath() {
        if (isFolderExist()) {
            this.qrCodePath = Path.of(QR_CODE_PATH_SYSTEM_PROPERTY + qrCodeData + ".png");
        }
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        if (qrCodePath == null)
            throw new QRCodeDomainException("QRCode path is null");
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }
}
