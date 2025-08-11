package com.poly.booking.management.dao.outbox.room.repository;

import com.poly.booking.management.dao.outbox.room.entity.RoomOutboxEntity;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomOutboxJpaRepository extends JpaRepository<RoomOutboxEntity, UUID> {

    Optional<List<RoomOutboxEntity>> findByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                                                 OutboxStatus outboxStatus,
                                                                                 List<SagaStatus> sagaStatus);

    Optional<RoomOutboxEntity> findByTypeAndSagaIdAndSagaStatusIn(String type,
                                                                     UUID sagaId,
                                                                     List<SagaStatus> sagaStatus);

    void deleteByTypeAndOutboxStatusAndSagaStatusIn(String type,
                                                    OutboxStatus outboxStatus,
                                                    List<SagaStatus> sagaStatus);
}
