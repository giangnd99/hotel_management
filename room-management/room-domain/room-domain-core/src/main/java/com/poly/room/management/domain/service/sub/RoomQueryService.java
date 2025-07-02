package com.poly.room.management.domain.service.sub;

import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.valueobject.RoomTypeId;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RoomQueryService {
    /**
     * Retrieves detailed information of a room from a provided list of rooms.
     * The Application Service is responsible for providing the list.
     *
     * @param allRooms A list of all available rooms.
     * @param roomId   The ID of the room to find.
     * @return An Optional containing the Room object if found, otherwise Optional.empty().
     */
    Optional<Room> getRoomById(List<Room> allRooms, RoomId roomId);

    /**
     * Filters a list of rooms to find available ones based on criteria.
     * The Application Service is responsible for providing the initial list of rooms.
     *
     * @param allRooms     A list of all rooms.
     * @param roomTypeId   Optional room type ID for filtering.
     * @param minFloor     Optional minimum floor for filtering.
     * @param maxFloor     Optional maximum floor for filtering.
     * @param checkInDate  Check-in date.
     * @param checkOutDate Check-out date.
     * @return A list of available rooms matching the criteria.
     */
    List<Room> findAvailableRooms(List<Room> allRooms, Optional<RoomTypeId> roomTypeId, Optional<Integer> minFloor, Optional<Integer> maxFloor, LocalDateTime checkInDate, LocalDateTime checkOutDate);

    /**
     * Filters a list of rooms by a specific status.
     * The Application Service is responsible for providing the initial list of rooms.
     *
     * @param allRooms   A list of all rooms.
     * @param statusName The name of the room status (e.g., "VACANT", "BOOKED", "MAINTENANCE").
     * @return A list of rooms with the corresponding status.
     */
    List<Room> getRoomsByStatus(List<Room> allRooms, String statusName);

    /**
     * Retrieves detailed information of a room type from a provided list.
     * The Application Service is responsible for providing the list.
     *
     * @param allRoomTypes A list of all room types.
     * @param roomTypeId   The ID of the room type.
     * @return An Optional containing the RoomType object if found, otherwise Optional.empty().
     */
    Optional<RoomType> getRoomTypeById(List<RoomType> allRoomTypes, RoomTypeId roomTypeId);

    /**
     * Retrieves a list of all room types (typically provided by Application Service).
     * This method simply returns the provided list as the domain doesn't "fetch" directly.
     *
     * @param allRoomTypes A list of all room types.
     * @return A list of RoomType objects.
     */
    List<RoomType> getAllRoomTypes(List<RoomType> allRoomTypes);

    /**
     * Retrieves a list of all rooms (typically provided by Application Service).
     * This method simply returns the provided list as the domain doesn't "fetch" directly.
     *
     * @param allRooms A list of all rooms.
     * @return A list of Room objects.
     */
    List<Room> getAllRooms(List<Room> allRooms);
}
