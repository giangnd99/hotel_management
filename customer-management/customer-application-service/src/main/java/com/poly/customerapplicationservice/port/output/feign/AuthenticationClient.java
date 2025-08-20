package com.poly.customerapplicationservice.port.output.feign;

import com.poly.customerapplicationservice.dto.ApiResponse;
import com.poly.customerapplicationservice.dto.request.UserCreationRequest;
import com.poly.customerapplicationservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authentication-management", url = "localhost:8092", path = "/users")
public interface AuthenticationClient {

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request);
}
