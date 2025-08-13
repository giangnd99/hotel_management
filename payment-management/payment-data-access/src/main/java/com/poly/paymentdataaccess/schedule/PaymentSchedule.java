package com.poly.paymentdataaccess.schedule;

import com.poly.paymentapplicationservice.port.output.PaymentExpiredSchedule;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PaymentSchedule implements PaymentExpiredSchedule {
    @Override
    public void paymentExpiredSchedule() throws Exception {

    }

//    private PaymentUsecase paymentUsecase;
//
//    public PaymentSchedule(PaymentUsecase paymentUsecase) {
//        this.paymentUsecase = paymentUsecase;
//    }
//
//    @Override
//    @Scheduled(fixedRate = 600000)
//    public void paymentExpiredSchedule() throws Exception {
//        paymentUsecase.cancelExpiredPayments();
//        log.info("PayOs Payment Gateway has been canceled");
//    }
}
