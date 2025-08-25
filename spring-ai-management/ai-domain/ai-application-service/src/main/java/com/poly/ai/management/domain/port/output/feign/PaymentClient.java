package com.poly.ai.management.domain.port.output.feign;

import com.poly.ai.management.domain.dto.InvoiceDto;
import com.poly.ai.management.domain.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "payment-service" , url = "localhost:8094")
public interface PaymentClient {

    @GetMapping(value = "/payment")
    List<PaymentDto> getAll();

    @GetMapping(value = "/api/invoice")
    List<InvoiceDto> getInvoice();
}
