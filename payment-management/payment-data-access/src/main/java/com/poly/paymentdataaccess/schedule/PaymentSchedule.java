package com.poly.paymentdataaccess.schedule;

import com.poly.paymentapplicationservice.port.input.AutoExpirePaymentsUsecase;
import com.poly.paymentapplicationservice.port.output.PaymentExpiredSchedule;
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
