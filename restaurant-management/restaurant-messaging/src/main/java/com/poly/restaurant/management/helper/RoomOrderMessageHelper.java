package com.poly.restaurant.management.helper;

import com.poly.restaurant.management.message.RoomOrderRequestMessage;
import com.poly.restaurant.management.message.RoomOrderResponseMessage;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Helper class để xử lý room order messages
 * 
 * NGHIỆP VỤ:
 * - Validation room order messages
 * - Tạo room order messages
 * - Kiểm tra response status
 * - Logging và error handling
 * 
 * PATTERNS ÁP DỤNG:
 * - Utility Pattern: Tập trung các helper methods
 * - Factory Pattern: Tạo message objects
 * - Validation Pattern: Kiểm tra tính hợp lệ
 */
@Slf4j
public class RoomOrderMessageHelper {

    // Request types
    public static final String REQUEST_TYPE_ATTACH_ORDER = "ATTACH_ORDER";
    public static final String REQUEST_TYPE_DETACH_ORDER = "DETACH_ORDER";

    // Response statuses
    public static final String RESPONSE_STATUS_SUCCESS = "SUCCESS";
    public static final String RESPONSE_STATUS_FAILED = "FAILED";
    public static final String RESPONSE_STATUS_CANCELLED = "CANCELLED";

    /**
     * Kiểm tra tính hợp lệ của RoomOrderRequestMessage
     */
    public static boolean isValidRoomOrderRequestMessage(RoomOrderRequestMessage message) {
        if (message == null) {
            log.warn("RoomOrderRequestMessage is null");
            return false;
        }

        if (message.getId() == null || message.getId().trim().isEmpty()) {
            log.warn("RoomOrderRequestMessage id is null or empty");
            return false;
        }

        if (message.getOrderId() == null || message.getOrderId().trim().isEmpty()) {
            log.warn("RoomOrderRequestMessage orderId is null or empty");
            return false;
        }

        if (message.getRoomId() == null || message.getRoomId().trim().isEmpty()) {
            log.warn("RoomOrderRequestMessage roomId is null or empty");
            return false;
        }

        if (message.getCustomerId() == null || message.getCustomerId().trim().isEmpty()) {
            log.warn("RoomOrderRequestMessage customerId is null or empty");
            return false;
        }

        if (message.getAmount() == null || message.getAmount().trim().isEmpty()) {
            log.warn("RoomOrderRequestMessage amount is null or empty");
            return false;
        }

        if (!isValidAmount(message.getAmount())) {
            log.warn("RoomOrderRequestMessage amount is invalid: {}", message.getAmount());
            return false;
        }

        if (message.getRequestType() == null || message.getRequestType().trim().isEmpty()) {
            log.warn("RoomOrderRequestMessage requestType is null or empty");
            return false;
        }

        if (!isValidRequestType(message.getRequestType())) {
            log.warn("RoomOrderRequestMessage requestType is invalid: {}", message.getRequestType());
            return false;
        }

        return true;
    }

    /**
     * Kiểm tra tính hợp lệ của RoomOrderResponseMessage
     */
    public static boolean isValidRoomOrderResponseMessage(RoomOrderResponseMessage message) {
        if (message == null) {
            log.warn("RoomOrderResponseMessage is null");
            return false;
        }

        if (message.getId() == null || message.getId().trim().isEmpty()) {
            log.warn("RoomOrderResponseMessage id is null or empty");
            return false;
        }

        if (message.getOrderId() == null || message.getOrderId().trim().isEmpty()) {
            log.warn("RoomOrderResponseMessage orderId is null or empty");
            return false;
        }

        if (message.getRoomId() == null || message.getRoomId().trim().isEmpty()) {
            log.warn("RoomOrderResponseMessage roomId is null or empty");
            return false;
        }

        if (message.getCustomerId() == null || message.getCustomerId().trim().isEmpty()) {
            log.warn("RoomOrderResponseMessage customerId is null or empty");
            return false;
        }

        if (message.getResponseStatus() == null || message.getResponseStatus().trim().isEmpty()) {
            log.warn("RoomOrderResponseMessage responseStatus is null or empty");
            return false;
        }

        if (!isValidResponseStatus(message.getResponseStatus())) {
            log.warn("RoomOrderResponseMessage responseStatus is invalid: {}", message.getResponseStatus());
            return false;
        }

        return true;
    }

    /**
     * Kiểm tra tính hợp lệ của amount
     */
    public static boolean isValidAmount(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return false;
        }

        try {
            BigDecimal amountValue = new BigDecimal(amount);
            return amountValue.compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Kiểm tra tính hợp lệ của request type
     */
    public static boolean isValidRequestType(String requestType) {
        return REQUEST_TYPE_ATTACH_ORDER.equals(requestType) || 
               REQUEST_TYPE_DETACH_ORDER.equals(requestType);
    }

    /**
     * Kiểm tra tính hợp lệ của response status
     */
    public static boolean isValidResponseStatus(String responseStatus) {
        return RESPONSE_STATUS_SUCCESS.equals(responseStatus) ||
               RESPONSE_STATUS_FAILED.equals(responseStatus) ||
               RESPONSE_STATUS_CANCELLED.equals(responseStatus);
    }

    /**
     * Kiểm tra xem response có thành công không
     */
    public static boolean isResponseSuccessful(String responseStatus) {
        return RESPONSE_STATUS_SUCCESS.equals(responseStatus);
    }

    /**
     * Kiểm tra xem response có thất bại không
     */
    public static boolean isResponseFailed(String responseStatus) {
        return RESPONSE_STATUS_FAILED.equals(responseStatus) ||
               RESPONSE_STATUS_CANCELLED.equals(responseStatus);
    }

    /**
     * Tạo RoomOrderRequestMessage để đính kèm order vào room
     */
    public static RoomOrderRequestMessage createAttachOrderRequest(
            String orderId, String roomId, String customerId, String amount) {
        return RoomOrderRequestMessage.builder()
                .id(UUID.randomUUID().toString())
                .orderId(orderId)
                .roomId(roomId)
                .customerId(customerId)
                .amount(amount)
                .orderStatus("NEW")
                .requestType(REQUEST_TYPE_ATTACH_ORDER)
                .build();
    }

    /**
     * Tạo RoomOrderRequestMessage để gỡ order khỏi room
     */
    public static RoomOrderRequestMessage createDetachOrderRequest(
            String orderId, String roomId, String customerId) {
        return RoomOrderRequestMessage.builder()
                .id(UUID.randomUUID().toString())
                .orderId(orderId)
                .roomId(roomId)
                .customerId(customerId)
                .amount("0")
                .orderStatus("COMPLETED")
                .requestType(REQUEST_TYPE_DETACH_ORDER)
                .build();
    }

    /**
     * Log room order request message details
     */
    public static void logRoomOrderRequestDetails(RoomOrderRequestMessage message) {
        log.info("Room Order Request - ID: {}, Order: {}, Room: {}, Customer: {}, Amount: {}, Type: {}",
                message.getId(), message.getOrderId(), message.getRoomId(), 
                message.getCustomerId(), message.getAmount(), message.getRequestType());
    }

    /**
     * Log room order response message details
     */
    public static void logRoomOrderResponseDetails(RoomOrderResponseMessage message) {
        log.info("Room Order Response - ID: {}, Order: {}, Room: {}, Customer: {}, Status: {}, Message: {}",
                message.getId(), message.getOrderId(), message.getRoomId(), 
                message.getCustomerId(), message.getResponseStatus(), message.getMessage());
    }

    /**
     * Log room order error
     */
    public static void logRoomOrderError(String operation, String orderId, String error) {
        log.error("Room Order {} failed for order {}: {}", operation, orderId, error);
    }
}
