package com.poly.booking.management.domain.helper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.poly.booking.management.domain.entity.QRCodeCheckIn;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ZxingQrCodeHelper {

    private void generateQrCode(QRCodeCheckIn qrCodeCheckIn) throws Exception {
        String data = qrCodeCheckIn.getQrCodeData();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix matrix = qrCodeWriter.encode(data,
                BarcodeFormat.QR_CODE,
                QRCodeCheckIn.SIZE_QRCODE,
                QRCodeCheckIn.SIZE_QRCODE);

        String outputFile = QRCodeCheckIn.QR_CODE_PATH_SYSTEM_PROPERTY;

        Path path = FileSystems.getDefault().getPath(outputFile);
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
    }
}
