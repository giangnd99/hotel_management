package com.poly.room.management.dao.room.adapter;

import com.poly.room.management.dao.room.entity.RoomStatusEntity;
import com.poly.room.management.dao.room.mapper.RoomStatusMapper;
import com.poly.room.management.dao.room.repository.RoomStatusJpaRepository;
import com.poly.room.management.domain.entity.RoomStatus;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.RoomStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomStatusRepositoryImpl implements RoomStatusRepository {
    private static final String STATUS_NOT_FOUND = "Room status with id %d not found";

    private final RoomStatusMapper mapper;
    private final RoomStatusJpaRepository jpaRepository;

    @Override
    public RoomStatus save(RoomStatus status) {
        return mapToDomainEntity(jpaRepository.save(mapper.toEntity(status)));
    }

    @Override
    public Optional<RoomStatus> findById(Integer id) {
        return jpaRepository.findById(id)
                .map(this::mapToDomainEntity)
                .or(() -> {
                    throw new RoomDomainException(String.format(STATUS_NOT_FOUND, id));
                });
    }

    @Override
    public List<RoomStatus> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::mapToDomainEntity)
                .toList();
    }


    @Override
    public void delete(RoomStatus roomStatus) {
        Integer id = roomStatus.getId().getValue();
        if (!jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(STATUS_NOT_FOUND, id));
        }
        jpaRepository.deleteById(id);
    }

    @Override
    public RoomStatus update(RoomStatus status) {
        Integer id = status.getId().getValue();
        if (!jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(STATUS_NOT_FOUND, id));
        }
        RoomStatusEntity entityToUpdate = mapper.toEntity(status);
        entityToUpdate.setStatusId(id);
        return mapToDomainEntity(jpaRepository.save(entityToUpdate));
    }

    private RoomStatus mapToDomainEntity(RoomStatusEntity entity) {
        return mapper.toDomain(entity);
    }
}