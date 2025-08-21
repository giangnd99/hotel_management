package com.poly.room.management.dao.room.adapter;

import com.poly.room.management.dao.room.entity.RoomTypeEntity;
import com.poly.room.management.dao.room.mapper.RoomTypeMapper;
import com.poly.room.management.dao.room.repository.RoomTypeJpaRepository;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoomTypeRepositoryImpl implements RoomTypeRepository {
    private static final String ROOM_TYPE_NOT_FOUND = "Room type with id %d not found";

    private final RoomTypeMapper mapper;
    private final RoomTypeJpaRepository jpaRepository;

    @Override
    public RoomType save(RoomType roomType) {
        return mapToRoomType(jpaRepository.save(mapper.toEntity(roomType)));
    }

    @Override
    public Optional<RoomType> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(this::mapToRoomType)
                .or(() -> {
                    throw new RoomDomainException(String.format(ROOM_TYPE_NOT_FOUND, id));
                });
    }

    @Override
    public List<RoomType> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::mapToRoomType)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        if (!jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(ROOM_TYPE_NOT_FOUND, id));
        }
        jpaRepository.deleteById(id);
    }

    @Override
    public RoomType update(RoomType roomType, UUID id) {
        if (!jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(ROOM_TYPE_NOT_FOUND, id));
        }
        RoomTypeEntity entityToUpdate = mapper.toEntity(roomType);
        entityToUpdate.setRoomTypeId(id);
        return mapToRoomType(jpaRepository.save(entityToUpdate));
    }

    private RoomType mapToRoomType(RoomTypeEntity entity) {
        return mapper.toDomainEntity(entity);
    }

}
