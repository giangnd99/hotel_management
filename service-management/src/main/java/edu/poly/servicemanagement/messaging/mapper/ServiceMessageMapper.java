package edu.poly.servicemanagement.messaging.mapper;

import edu.poly.servicemanagement.messaging.message.PaymentRequestMessage;
import edu.poly.servicemanagement.messaging.message.ServiceOrderRequestMessage;
import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class ServiceMessageMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Chuyển đổi ServiceOrderRequestMessage thành Avro message
     */
    public static Object toAvroMessage(ServiceOrderRequestMessage domainMessage) {
        // Trong thực tế, đây sẽ là việc tạo Avro object từ domain message
        // Ví dụ: ServiceOrderRequestMessageAvro.newBuilder()...
        return domainMessage;
    }

    /**
     * Chuyển đổi PaymentRequestMessage thành Avro message
     */
    public static Object toAvroMessage(PaymentRequestMessage domainMessage) {
        // Trong thực tế, đây sẽ là việc tạo Avro object từ domain message
        // Ví dụ: ServicePaymentRequestMessageAvro.newBuilder()...
        return domainMessage;
    }

    /**
     * Chuyển đổi Avro message thành ServiceOrderRequestMessage
     */
    public static ServiceOrderRequestMessage toDomainMessage(Object avroMessage) {
        // Trong thực tế, đây sẽ là việc chuyển đổi từ Avro object sang domain message
        return null;
    }

    /**
     * Chuyển đổi Avro message thành PaymentRequestMessage
     */
    public static PaymentRequestMessage toDomainMessage(Object avroMessage, Class<PaymentRequestMessage> clazz) {
        // Trong thực tế, đây sẽ là việc chuyển đổi từ Avro object sang domain message
        return null;
    }

    /**
     * Format LocalDateTime thành String cho Avro
     */
    public static String formatDateTime(java.time.LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : null;
    }

    /**
     * Parse String thành LocalDateTime từ Avro
     */
    public static java.time.LocalDateTime parseDateTime(String dateTimeString) {
        return dateTimeString != null ? java.time.LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER) : null;
    }
}
