package com.poly.booking.management.dao.outbox.notifi.adapter;

import com.poly.booking.management.dao.outbox.notifi.mapper.NotificationOutboxDataMapper;
import com.poly.booking.management.dao.outbox.notifi.repository.NotificationOutboxJpaRepository;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.booking.management.domain.port.out.repository.NotificationOutboxRepository;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationOutboxRepositoryImpl implements NotificationOutboxRepository {

    private final NotificationOutboxJpaRepository notificationOutboxJpaRepository;
    private final NotificationOutboxDataMapper notificationOutboxDataMapper;

    @Override
    public NotifiOutboxMessage save(NotifiOutboxMessage notifiOutboxMessage) {
        return notificationOutboxDataMapper.toModel(notificationOutboxJpaRepository.save(
                notificationOutboxDataMapper.toEntity(notifiOutboxMessage)));
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(OutboxStatus outboxStatus, SagaStatus... status) {
        notificationOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(
                outboxStatus.name(),
                outboxStatus,
                List.of(status)
        );
    }

    @Override
    public Optional<NotifiOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String bookingSagaName, UUID sagaId, SagaStatus... status) {
        return notificationOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(
                bookingSagaName,
                sagaId,
                Arrays.asList(status)
        ).map(notificationOutboxDataMapper::toModel);
    }

    @Override
    public Optional<List<NotifiOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus... status) {
        return Optional.of(notificationOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(
                        bookingSagaName,
                        outboxStatus,
                        Arrays.asList(status)
                ).orElseThrow(() -> new BookingDomainException("Notification outbox not found")).
                stream().map(notificationOutboxDataMapper::toModel).toList());
    }
}
