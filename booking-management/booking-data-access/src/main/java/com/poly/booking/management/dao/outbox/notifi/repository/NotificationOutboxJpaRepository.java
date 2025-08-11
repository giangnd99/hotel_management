package com.poly.booking.management.dao.outbox.notifi.repository;

import com.poly.booking.management.dao.outbox.notifi.entity.NotificationOutboxEntity;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationOutboxJpaRepository extends JpaRepository<NotificationOutboxEntity, UUID> {
    Optional<List<NotificationOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                                                      OutboxStatus outboxStatus,
                                                                                      List<SagaStatus> sagaStatus);

    Optional<NotificationOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type,
                                                                          UUID sagaId,
                                                                          List<SagaStatus> sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                    OutboxStatus outboxStatus,
                                                    List<SagaStatus> sagaStatus);
}
