package com.poly.customerapplicationservice.service;

import com.poly.customerapplicationservice.dto.request.UserCreationRequest;
import com.poly.customerapplicationservice.dto.response.UserResponse;
import com.poly.customerapplicationservice.message.CustomerBookingMessage;
import com.poly.customerapplicationservice.port.output.feign.NotificationClient;
import com.poly.customerapplicationservice.port.output.publisher.CustomerCreationRequestPublisher;
import com.poly.customerdomain.model.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerAsyncService {
    private final NotificationClient notificationClient;

    @Async
    public void sendNotifications(UserCreationRequest creationRequest) {
        notificationClient.sendAccountInfo(creationRequest.getEmail(), creationRequest.getPassword());
    }

}
