package com.poly.room.management.domain.handler.room;

import com.poly.application.handler.AppException;
import com.poly.room.management.domain.dto.response.RoomResponse;
import com.poly.room.management.domain.mapper.RoomDtoMapper;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.service.RoomDomainService;
import com.poly.service.handler.BaseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class FindAllRoomsHandler extends BaseHandler<RoomDomainService, RoomRepository> {

    private static final String ROOM_FETCH_ERROR = "Failed to fetch rooms";
    private final RoomDtoMapper roomDtoMapper;

    public FindAllRoomsHandler(RoomDomainService roomDomainService, RoomRepository roomRepository, RoomDtoMapper roomDtoMapper) {
        super(roomDomainService, roomRepository);
        this.roomDtoMapper = roomDtoMapper;
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {

        try {
//            log.debug("Fetching all rooms with pagination: {}", pageable);
            return repository.findAll().stream().map(roomDtoMapper::toResponse).toList();
        } catch (Exception e) {
            log.error("Error fetching all rooms", e);
            throw new AppException("Failed to fetch rooms", e);
        }
    }
}
