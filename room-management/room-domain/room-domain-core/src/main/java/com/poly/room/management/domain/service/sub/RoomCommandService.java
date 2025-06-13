package com.poly.room.management.domain.service.sub;

import com.poly.domain.valueobject.InventoryItemId;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;

public interface RoomCommandService {
    /**
     * Creates a new room. The Application Service is responsible for ensuring
     * roomNumber uniqueness and providing the loaded RoomType.
     * @param roomNumber The room number.
     * @param floor The floor of the room.
     * @param roomType The loaded RoomType entity.
     * @return The newly created and validated Room entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    Room createRoom(String roomNumber, int floor, RoomType roomType) throws RoomDomainException;

    /**
     * Updates the details of an existing room. The Application Service is responsible
     * for loading the existing Room and new RoomType, and ensuring newRoomNumber uniqueness.
     * @param room The existing Room entity to update.
     * @param newRoomNumber The new room number.
     * @param newFloor The new floor of the room.
     * @param newRoomType The new RoomType entity.
     * @return The updated and validated Room entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    Room updateRoomDetails(Room room, String newRoomNumber, int newFloor, RoomType newRoomType) throws RoomDomainException;

    /**
     * Deletes a room. The Application Service loads the Room entity.
     * @param room The Room entity to delete.
     * @throws RoomDomainException If the room cannot be deleted based on domain rules.
     */
    void deleteRoom(Room room) throws RoomDomainException;

    /**
     * Changes the room status to VACANT. The Application Service loads the Room entity.
     * @param room The Room entity to update.
     * @return The updated Room entity.
     * @throws RoomDomainException If the room status cannot be changed based on domain rules.
     */
    Room setRoomVacant(Room room) throws RoomDomainException;

    /**
     * Changes the room status to BOOKED. The Application Service loads the Room entity.
     * @param room The Room entity to update.
     * @return The updated Room entity.
     * @throws RoomDomainException If the room status cannot be changed based on domain rules.
     */
    Room setRoomBooked(Room room) throws RoomDomainException;

    /**
     * Changes the room status to OCCUPIED. The Application Service loads the Room entity.
     * @param room The Room entity to update.
     * @return The updated Room entity.
     * @throws RoomDomainException If the room status cannot be changed based on domain rules.
     */
    Room setRoomOccupied(Room room) throws RoomDomainException;

    /**
     * Changes the room status to CLEANING. The Application Service loads the Room entity.
     * @param room The Room entity to update.
     * @return The updated Room entity.
     * @throws RoomDomainException If the room status cannot be changed based on domain rules.
     */
    Room setRoomCleaning(Room room) throws RoomDomainException;

    /**
     * Changes the room status to MAINTENANCE. The Application Service loads the Room entity.
     * @param room The Room entity to update.
     * @return The updated Room entity.
     * @throws RoomDomainException If the room status cannot be changed based on domain rules.
     */
    Room setRoomMaintenance(Room room) throws RoomDomainException;

    /**
     * Creates a request to issue inventory items for a room.
     * This method assumes the Room entity is loaded by the Application Service.
     * The actual interaction with an external Inventory system (if any) would be
     * handled by the Application Service or an integration layer.
     * @param room The Room entity requiring inventory items.
     * @param inventoryItemId The ID of the inventory item needed.
     * @param quantity The requested quantity.
     * @param requestedByStaffId The ID of the requesting staff member.
     * @throws RoomDomainException If the request is invalid.
     */
    void requestInventoryItemForRoom(Room room, InventoryItemId inventoryItemId, int quantity, StaffId requestedByStaffId) throws RoomDomainException;
}
