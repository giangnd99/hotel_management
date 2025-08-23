package com.poly.ai.management.domain.port.output.feign;

import com.poly.ai.management.domain.dto.InvoiceDto;
import com.poly.ai.management.domain.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "payment-service" , url = "localhost:8094/payment")
public interface PaymentClient {

    @GetMapping
    List<PaymentDto> getAll();

    @GetMapping
    List<InvoiceDto> getInvoice();
}
