package com.poly.customer.management.message.publisher;

import com.poly.booking.management.domain.kafka.model.CustomerModelAvro;
import com.poly.customer.management.message.mapper.CustomerKafkaDataMapper;
import com.poly.customerapplicationservice.message.CustomerBookingMessage;
import com.poly.customerapplicationservice.port.output.publisher.CustomerCreationRequestPublisher;
import com.poly.kafka.producer.AbstractKafkaPublisher;
import com.poly.kafka.producer.service.KafkaProducer;
import org.springframework.stereotype.Component;

@Component
public class CustomerCreationRequestKafkaPublisher
        extends AbstractKafkaPublisher<String, CustomerModelAvro, CustomerBookingMessage>
        implements CustomerCreationRequestPublisher {
    private final CustomerKafkaDataMapper customerKafkaDataMapper;

    protected CustomerCreationRequestKafkaPublisher(KafkaProducer<String, CustomerModelAvro> kafkaProducer, CustomerKafkaDataMapper customerKafkaDataMapper) {
        super(kafkaProducer);
        this.customerKafkaDataMapper = customerKafkaDataMapper;
    }

    @Override
    protected String getTopicName() {
        return "customer-booking-topic";
    }

    @Override
    protected String getKey(CustomerBookingMessage message) {
        return message.getCustomerId();
    }

    @Override
    protected CustomerModelAvro toAvro(CustomerBookingMessage message) {
        return customerKafkaDataMapper.customerModelAvro(message);
    }

    @Override
    protected String getMessageName() {
        return "CreateCustomerRequestToBookingDomain";
    }
}
