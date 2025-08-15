package edu.poly.servicemanagement.messaging.listener;

import edu.poly.servicemanagement.enums.PaymentStatus;
import edu.poly.servicemanagement.enums.ServiceOrderStatus;
import edu.poly.servicemanagement.messaging.message.PaymentResponseMessage;
import edu.poly.servicemanagement.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResponseListenerImpl implements PaymentResponseListener {

    private final ServiceOrderService serviceOrderService;

    @Override
    public void paymentCompleted(PaymentResponseMessage paymentResponseMessage) {
        log.info("Payment completed for order: {}", paymentResponseMessage.getOrderNumber());
        
        try {
            // Update payment status to COMPLETED
            serviceOrderService.updatePaymentStatus(
                    Integer.valueOf(paymentResponseMessage.getOrderId()), 
                    PaymentStatus.COMPLETED.name()
            );
            
            // Update order status to CONFIRMED
            serviceOrderService.updateServiceOrderStatus(
                    Integer.valueOf(paymentResponseMessage.getOrderId()), 
                    ServiceOrderStatus.CONFIRMED.name()
            );
            
            log.info("Successfully updated order status for payment completed: {}", 
                    paymentResponseMessage.getOrderNumber());
                    
        } catch (Exception e) {
            log.error("Error updating order status for payment completed: {}", 
                    paymentResponseMessage.getOrderNumber(), e);
        }
    }

    @Override
    public void paymentCancelled(PaymentResponseMessage paymentResponseMessage) {
        log.info("Payment cancelled for order: {}", paymentResponseMessage.getOrderNumber());
        
        try {
            // Update payment status to CANCELLED
            serviceOrderService.updatePaymentStatus(
                    Integer.valueOf(paymentResponseMessage.getOrderId()), 
                    PaymentStatus.CANCELLED.name()
            );
            
            // Update order status to CANCELLED
            serviceOrderService.updateServiceOrderStatus(
                    Integer.valueOf(paymentResponseMessage.getOrderId()), 
                    ServiceOrderStatus.CANCELLED.name()
            );
            
            log.info("Successfully updated order status for payment cancelled: {}", 
                    paymentResponseMessage.getOrderNumber());
                    
        } catch (Exception e) {
            log.error("Error updating order status for payment cancelled: {}", 
                    paymentResponseMessage.getOrderNumber(), e);
        }
    }

    @Override
    public void paymentFailed(PaymentResponseMessage paymentResponseMessage) {
        log.info("Payment failed for order: {}", paymentResponseMessage.getOrderNumber());
        
        try {
            // Update payment status to FAILED
            serviceOrderService.updatePaymentStatus(
                    Integer.valueOf(paymentResponseMessage.getOrderId()), 
                    PaymentStatus.FAILED.name()
            );
            
            // Keep order status as NEW for retry
            log.info("Payment failed, order status remains NEW for retry: {}", 
                    paymentResponseMessage.getOrderNumber());
                    
        } catch (Exception e) {
            log.error("Error updating order status for payment failed: {}", 
                    paymentResponseMessage.getOrderNumber(), e);
        }
    }
}
