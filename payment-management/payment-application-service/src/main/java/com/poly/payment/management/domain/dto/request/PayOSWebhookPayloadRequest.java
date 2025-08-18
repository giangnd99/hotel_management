package com.poly.payment.management.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PayOSWebhookPayloadRequest {
    private String code;
    private String desc;
    private boolean success;
    private PayOSWebhookData data;
    private String signature;

    @Data
    public static class PayOSWebhookData {
        private String orderCode;
        private long amount;
        private String description;
        private String accountNumber;
        private String reference;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime transactionDateTime;
        private String paymentLinkId;
        private String code;
        private String desc;
    }
}
