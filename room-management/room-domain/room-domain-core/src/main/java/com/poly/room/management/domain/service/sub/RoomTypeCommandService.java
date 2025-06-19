package com.poly.room.management.domain.service.sub;

import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.entity.FurnitureRequirement;

import java.util.List;

public interface RoomTypeCommandService {
    /**
     * Creates a new room type. The Application Service is responsible for ensuring
     * typeName uniqueness and providing loaded Furniture entities for requirements.
     * @param typeName The name of the room type.
     * @param description The description of the room type.
     * @param basePrice The base price of the room type (as a String, parsed in domain).
     * @param maxOccupancy The maximum occupancy.
     * @param furnitureItems A list of initial furniture requirements.
     * @return The created and validated RoomType entity.
     * @throws RoomDomainException If the data is invalid.
     */
    RoomType createRoomType(String typeName, String description, String basePrice, int maxOccupancy, List<FurnitureRequirement> furnitureItems) throws RoomDomainException;

    /**
     * Updates the details of a room type. The Application Service loads the RoomType entity.
     * @param roomType The existing RoomType entity to update.
     * @param newTypeName The new name.
     * @param newDescription The new description.
     * @param newBasePrice The new base price (as a String, parsed in domain).
     * @param newMaxOccupancy The new maximum occupancy.
     * @return The updated and validated RoomType entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    RoomType updateRoomTypeDetails(RoomType roomType, String newTypeName, String newDescription, String newBasePrice, int newMaxOccupancy) throws RoomDomainException;

    /**
     * Deletes a room type. The Application Service loads the RoomType entity and ensures
     * no rooms are associated with it.
     * @param roomType The RoomType entity to delete.
     * @throws RoomDomainException If the room type cannot be deleted based on domain rules.
     */
    void deleteRoomType(RoomType roomType) throws RoomDomainException;

    /**
     * Adds a type of furniture to a RoomType. The Application Service loads the RoomType
     * and Furniture entities.
     * @param roomType The RoomType entity to update.
     * @param furniture The Furniture entity to add.
     * @param quantity The required quantity.
     * @return The updated RoomType entity.
     * @throws RoomDomainException If the furniture is already present or data is invalid.
     */
    RoomType addFurnitureToRoomType(RoomType roomType, Furniture furniture, int quantity) throws RoomDomainException;

    /**
     * Removes a type of furniture from a RoomType. The Application Service loads the RoomType entity.
     * @param roomType The RoomType entity to update.
     * @param furnitureId The ID of the furniture to remove.
     * @return The updated RoomType entity.
     * @throws RoomDomainException If the furniture is not in this room type.
     */
    RoomType removeFurnitureFromRoomType(RoomType roomType, FurnitureId furnitureId) throws RoomDomainException;

    /**
     * Updates the required quantity of a furniture in a room type. The Application Service loads
     * the RoomType entity.
     * @param roomType The RoomType entity to update.
     * @param furnitureId The ID of the furniture.
     * @param newQuantity The new quantity.
     * @return The updated RoomType entity.
     * @throws RoomDomainException If the quantity is invalid.
     */
    RoomType updateFurnitureQuantityInRoomType(RoomType roomType, FurnitureId furnitureId, int newQuantity) throws RoomDomainException;
}
