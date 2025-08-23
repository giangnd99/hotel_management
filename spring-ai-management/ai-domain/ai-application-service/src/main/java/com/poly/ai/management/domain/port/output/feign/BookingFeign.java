package com.poly.ai.management.domain.port.output.feign;

import com.poly.ai.management.domain.dto.BookingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "booking-service" , url = "localhost:8083/bookings")
public interface BookingFeign {

    @GetMapping
    List<BookingDto> getAll();
}
