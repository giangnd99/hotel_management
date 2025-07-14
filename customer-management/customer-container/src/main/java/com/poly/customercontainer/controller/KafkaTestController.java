package com.poly.customercontainer.controller;

import com.poly.customerapplicationservice.command.CreateCustomerCommand;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-kafka")
public class KafkaTestController {

    private final KafkaTemplate<String, CreateCustomerCommand> kafkaTemplate;

    public KafkaTestController(KafkaTemplate<String, CreateCustomerCommand> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public String sendTestMessage(@RequestBody CreateCustomerCommand command) {
        kafkaTemplate.send("customer-created", command);
        return "Sent to Kafka topic: customer-created";
    }
}