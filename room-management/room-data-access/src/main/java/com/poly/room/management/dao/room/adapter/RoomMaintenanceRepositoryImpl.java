package com.poly.room.management.dao.room.adapter;

import com.poly.room.management.dao.room.entity.RoomMaintenanceEntity;
import com.poly.room.management.dao.room.mapper.RoomMaintenanceMapper;
import com.poly.room.management.dao.room.repository.RoomMaintenanceJpaRepository;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.RoomMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoomMaintenanceRepositoryImpl implements RoomMaintenanceRepository {
    private static final String MAINTENANCE_NOT_FOUND = "Room maintenance with id %d not found";

    private final RoomMaintenanceMapper mapper;
    private final RoomMaintenanceJpaRepository jpaRepository;

    @Override
    public RoomMaintenance save(RoomMaintenance maintenance) {
        return mapToDomainEntity(jpaRepository.save(mapper.toJpaEntity(maintenance)));
    }

    @Override
    public Optional<RoomMaintenance> findById(Integer id) {
        return jpaRepository.findById(id)
                .map(this::mapToDomainEntity)
                .or(() -> {
                    throw new RoomDomainException(String.format(MAINTENANCE_NOT_FOUND, id));
                });
    }

    @Override
    public List<RoomMaintenance> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::mapToDomainEntity)
                .toList();
    }

    @Override
    public List<RoomMaintenance> findByRoomId(Integer roomId) {
        return jpaRepository.findAllByRoom_RoomId(roomId).stream()
                .map(this::mapToDomainEntity)
                .toList();
    }

    @Override
    public void deleteById(Integer id) {
        if (!jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(MAINTENANCE_NOT_FOUND, id));
        }
        jpaRepository.deleteById(id);
    }

    @Override
    public RoomMaintenance update(RoomMaintenance maintenance) {
        Integer id = maintenance.getId().getValue();
        if (!jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(MAINTENANCE_NOT_FOUND, id));
        }
        RoomMaintenanceEntity entityToUpdate = mapper.toJpaEntity(maintenance);
        entityToUpdate.setMaintenanceId(id);
        return mapToDomainEntity(jpaRepository.save(entityToUpdate));
    }

    private RoomMaintenance mapToDomainEntity(RoomMaintenanceEntity entity) {
        return mapper.toDomain(entity);
    }
}