package com.poly.room.management.dao.room.adapter;

import com.poly.domain.exception.DomainException;
import com.poly.domain.valueobject.RoomStatus;
import com.poly.room.management.dao.room.repository.RoomJpaRepository;
import com.poly.room.management.domain.entity.*;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.dao.room.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {
    private static final String ROOM_ID_NOT_FOUND = "Room with ID %d not found";
    private static final String ROOM_ID_EXISTS = "Room with ID %d already exists";

    private final RoomJpaRepository jpaRepository;
    private final RoomMapper roomMapper;

    @Override
    public Room save(Room room) {
        UUID roomId = room.getId().getValue();
        checkRoomNotExists(roomId);
        return saveAndMapRoom(room);
    }

    @Override
    public Room update(Room room) {
        UUID roomId = room.getId().getValue();
        checkRoomExists(roomId);
        return saveAndMapRoom(room);
    }

    @Override
    public Optional<Room> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(roomMapper::toDomain);
    }

    @Override
    public List<Room> findAll() {
        return jpaRepository.findAll().stream()
                .map(roomMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(Room room) {
        jpaRepository.delete(roomMapper.toEntity(room));
    }

    private Room saveAndMapRoom(Room room) {
        return roomMapper.toDomain(
                jpaRepository.save(roomMapper.toEntity(room))
        );
    }

    private void checkRoomExists(UUID id) {
        if (!jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(ROOM_ID_NOT_FOUND, id));
        }
    }

    private void checkRoomNotExists(UUID id) {
        if (jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(ROOM_ID_EXISTS, id));
        }
    }
}
