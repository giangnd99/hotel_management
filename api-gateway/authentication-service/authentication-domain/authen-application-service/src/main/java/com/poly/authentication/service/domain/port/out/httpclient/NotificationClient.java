package com.poly.authentication.service.domain.port.out.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-management", url = "localhost:8190", path = "/notifications")
public interface NotificationClient {

    @PostMapping("/send-account-info")
    void sendAccountInfo(@RequestParam("userEmail") String userEmail,
                         @RequestParam("password") String password);
}
