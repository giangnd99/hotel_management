package com.poly.booking.management.dao.room.adapter;


import com.poly.booking.management.dao.room.mapper.RoomDataAccessMapper;
import com.poly.booking.management.dao.room.repository.RoomJpaRepository;
import com.poly.booking.management.domain.entity.Room;
import com.poly.booking.management.domain.port.out.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomRepositoryImpl implements RoomRepository {

    private final RoomJpaRepository roomJpaRepository;
    private final RoomDataAccessMapper roomDataAccessMapper;

    @Override
    public Optional<Room> findById(UUID roomId) {
        return roomJpaRepository.findById(roomId).map(roomDataAccessMapper::roomEntityToRoom);
    }

    @Override
    public void save(Room room) {
        roomJpaRepository.save(roomDataAccessMapper.roomToRoomEntity(room));
    }

}
