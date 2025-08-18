package com.poly.payment.management.data.access.schedule;

import com.poly.payment.management.domain.port.input.service.AutoExpirePaymentsUsecase;
import com.poly.payment.management.domain.service.PaymentExpiredSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class PaymentSchedule implements PaymentExpiredSchedule {

    private final AutoExpirePaymentsUsecase autoExpirePaymentUsecase;

    @Override
    @Scheduled(fixedRate = 60000)
    public void paymentExpiredSchedule() throws Exception {
        autoExpirePaymentUsecase.execute();
        log.info("PayOs Payment Gateway has been canceled");
    }
}
