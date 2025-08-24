package com.poly.restaurant.management.adapter;

import com.poly.message.model.payment.PaymentResponseMessage;
import com.poly.restaurant.application.port.in.message.listener.PaymentResponseListener;
import com.poly.restaurant.application.port.out.repo.OrderRepositoryPort;
import com.poly.restaurant.domain.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResponseListenerImpl implements PaymentResponseListener {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    public void onPaymentSuccess(PaymentResponseMessage paymentResponseMessage) {
        try {
            Order orderToUpdate = getOrderFromMessage(paymentResponseMessage);
            if (orderToUpdate == null) {
                return;
            }
            orderToUpdate.isCompleted();
            orderRepositoryPort.save(orderToUpdate);
            log.info("Payment successful for order with id: {}", paymentResponseMessage.getCorrelationId());
        } catch (Exception e) {
            log.error("Error occurred while processing payment response for order with id: {}",
                    paymentResponseMessage.getCorrelationId(), e);
        }
    }

    @Override
    public void onPaymentFailure(PaymentResponseMessage paymentResponseMessage) {
        try {

            Order orderToUpdate = getOrderFromMessage(paymentResponseMessage);
            if (orderToUpdate == null) {
                return;
            }
            orderToUpdate.isCancelled();
            orderRepositoryPort.save(orderToUpdate);
            log.info("Payment failed for order with id: {}", paymentResponseMessage.getCorrelationId());

        } catch (Exception e) {
            log.error("Error occurred while processing payment response for order with id: {}",
                    paymentResponseMessage.getCorrelationId(), e);
        }
    }

    private Order getOrderFromMessage(PaymentResponseMessage paymentResponseMessage) {
        Optional<Order> order =
                orderRepositoryPort.findById(paymentResponseMessage.getCorrelationId());
        if (order.isEmpty()) {
            log.error("Order not found with id: {}", paymentResponseMessage.getCorrelationId());
            return null;
        }
        return order.get();
    }
}
