package com.poly.booking.management.dao.outbox.payment.adapter;

import com.poly.booking.management.dao.outbox.payment.mapper.PaymentOutboxDataMapper;
import com.poly.booking.management.dao.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.booking.management.domain.port.out.repository.PaymentOutBoxRepository;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.Lombok;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentOutboxRepositoryImpl implements PaymentOutBoxRepository {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;
    private final PaymentOutboxDataMapper paymentOutboxDataMapper;

    @Override
    public PaymentOutboxMessage save(PaymentOutboxMessage updatePaymentOutboxMessage) {
        return paymentOutboxDataMapper.toModel(paymentOutboxJpaRepository.save(
                paymentOutboxDataMapper.toEntity(updatePaymentOutboxMessage)));
    }

    @Override
    public Optional<PaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String bookingSagaName, UUID sagaId, SagaStatus... sagaStatus) {
        return paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(
                bookingSagaName,
                sagaId,
                Arrays.asList(sagaStatus)
        ).map(paymentOutboxDataMapper::toModel);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(
                bookingSagaName,
                outboxStatus,
                Arrays.asList(sagaStatus)
        );
    }

    @Override
    public Optional<List<PaymentOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        return Optional.of(paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(
                        bookingSagaName,
                        outboxStatus,
                        Arrays.asList(sagaStatus)
                ).orElseThrow(() -> new BookingDomainException("Payment outbox not found"))
                .stream().map(paymentOutboxDataMapper::toModel).toList());
    }
}
