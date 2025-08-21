package com.poly.booking.management.dao.outbox.room.adapter;

import com.poly.booking.management.dao.outbox.room.mapper.RoomOutboxDataMapper;
import com.poly.booking.management.dao.outbox.room.repository.RoomOutboxJpaRepository;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.port.out.repository.RoomReserveOutBoxRepository;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomOutboxRepositoryImpl implements RoomReserveOutBoxRepository {

    private final RoomOutboxJpaRepository roomOutboxJpaRepository;
    private final RoomOutboxDataMapper roomOutboxDataMapper;

    @Override
    public RoomOutboxMessage save(RoomOutboxMessage roomOutboxMessage) {
        return roomOutboxDataMapper.toModel(
                roomOutboxJpaRepository.save(
                        roomOutboxDataMapper.toEntity(roomOutboxMessage)));
    }

    @Override
    public Optional<RoomOutboxMessage> findByTypeAndSagaIdAndSagaStatus(String bookingSagaName, UUID sagaId, SagaStatus... statuses) {
        return roomOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(
                bookingSagaName,
                sagaId,
                Arrays.asList(statuses)).map(roomOutboxDataMapper::toModel);
    }

    @Override
    public Optional<List<RoomOutboxMessage>> findAllByTypeAndSagaIdAndSagaStatus(String bookingSagaName, UUID sagaId, SagaStatus... statuses) {
        return Optional.of(roomOutboxJpaRepository.findAllByTypeAndSagaIdAndSagaStatusIn(bookingSagaName, sagaId, Arrays.asList(statuses))
                .orElseThrow(() -> new BookingDomainException("Not found list room outbox message"))
                .stream().map(roomOutboxDataMapper::toModel).toList());
    }

    @Override
    public Optional<List<RoomOutboxMessage>> findByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus[] sagaStatus) {
        return Optional.of(roomOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(
                        bookingSagaName,
                        outboxStatus,
                        Arrays.asList(sagaStatus)
                ).orElseThrow(() -> new BookingDomainException("Room outbox message not found"))
                .stream().map(roomOutboxDataMapper::toModel).toList());
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatus(String bookingSagaName, OutboxStatus outboxStatus, SagaStatus[] sagaStatus) {
        roomOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(
                bookingSagaName,
                outboxStatus,
                Arrays.asList(sagaStatus)
        );
    }
}
