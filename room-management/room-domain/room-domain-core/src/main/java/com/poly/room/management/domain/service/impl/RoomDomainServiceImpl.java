package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.ERoomStatus;
import com.poly.domain.valueobject.InventoryItemId;
import com.poly.domain.valueobject.MaintenanceStatus;
import com.poly.domain.valueobject.Money; // Assuming Money is a domain value object
import com.poly.domain.valueobject.RoomId;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.service.RoomDomainService;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.MaintenanceId;
import com.poly.room.management.domain.valueobject.MaintenanceTypeId;
import com.poly.room.management.domain.valueobject.RoomTypeId; // Still used for filtering logic
import com.poly.room.management.domain.entity.FurnitureRequirement;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Implementation of RoomDomainService. This class contains the core domain logic
 * and orchestrates interactions between domain entities. It is technology-agnostic
 * and operates solely on domain entities and value objects passed to it.
 * It does NOT interact with any persistence mechanisms directly.
 */
public class RoomDomainServiceImpl implements RoomDomainService {

    // No dependencies on any persistence ports or Spring annotations here.
    // All required entities/data are passed as parameters to the methods.

    public RoomDomainServiceImpl() {
        // Default constructor if no dependencies are needed (e.g., for simple instantiation)
    }

    /* --- RoomCommandService Implementations --- */

    @Override
    public Room createRoom(String roomNumber, int floor, RoomType roomType) throws RoomDomainException {
        // NOTE: The check for roomNumber uniqueness must be done by the Application Service
        // before calling this domain method, as this domain service has no persistence access.

        // Create Room entity, let it apply its own internal validation
        Room newRoom = Room.Builder.builder()
//                .id(new RoomId(UUID.randomUUID().toString())) // Generate new domain ID
                .roomNumber(roomNumber)
                .floor(floor)
                .roomType(roomType)
                .build();
        newRoom.setVacantRoomStatus();
        newRoom.validate(); // Perform domain-specific validation on the entity
        return newRoom; // Return the created entity for the Application Service to persist
    }

    @Override
    public Room updateRoomDetails(Room room, String newRoomNumber, int newFloor, RoomType newRoomType) throws RoomDomainException {
        // NOTE: The check for newRoomNumber uniqueness must be done by the Application Service
        // before calling this domain method if the room number is changed.

        room.setRoomNumber(newRoomNumber); // Update entity's internal state
        room.setFloor(newFloor); // Update entity's internal state
        room.setRoomType(newRoomType); // Update entity's internal state

        room.validate(); // Re-validate the entity after updates
        return room; // Return the updated entity for the Application Service to persist
    }

    @Override
    public void deleteRoom(Room room) throws RoomDomainException {
        // Business logic: Cannot delete occupied or booked rooms
        if (room.getRoomStatus().getRoomStatus().equals(ERoomStatus.OCCUPIED.name()) ||
                room.getRoomStatus().getRoomStatus().equalsIgnoreCase(ERoomStatus.BOOKED.name())) {
            throw new RoomDomainException("Cannot delete room with status " + room.getRoomStatus().getRoomStatus() + ". Room must be VACANT or MAINTENANCE to be deleted.");
        }
        // No persistence call here, the Application Service will handle the actual delete operation.
    }

    @Override
    public Room setRoomVacant(Room room) throws RoomDomainException {
        room.setVacantRoomStatus(); // Domain logic executed on the entity
        return room; // Return the updated entity
    }

    @Override
    public Room setRoomBooked(Room room) throws RoomDomainException {
        room.setBookedRoomStatus(); // Domain logic executed on the entity
        return room;
    }

    @Override
    public Room setRoomOccupied(Room room) throws RoomDomainException {
        room.setOccupiedRoomStatus(); // Domain logic executed on the entity
        return room;
    }

    @Override
    public Room setRoomCleaning(Room room) throws RoomDomainException {
        room.setCleanRoomStatus(); // Domain logic executed on the entity
        return room;
    }

    @Override
    public Room setRoomMaintenance(Room room) throws RoomDomainException {
        room.setMaintenanceRoomStatus(); // Domain logic executed on the entity
        return room;
    }

    @Override
    public void requestInventoryItemForRoom(Room room, InventoryItemId inventoryItemId, int quantity, StaffId requestedByStaffId) throws RoomDomainException {
        // Basic validation for quantity
        if (quantity <= 0) {
            throw new RoomDomainException("Requested quantity for inventory item must be positive.");
        }
        // NOTE: The actual interaction with an external Inventory system or persistence
        // would be handled by the Application Service or an integration layer.
        // This method only encapsulates the domain's understanding of the request.
        System.out.println("LOG: Domain received request for " + quantity + " of item " + inventoryItemId.getValue() + " for room " + room.getRoomNumber() + " by staff " + requestedByStaffId.getValue());
    }

    /* --- RoomQueryService Implementations --- */

    @Override
    public Optional<Room> getRoomById(List<Room> allRooms, RoomId roomId) {
        // This method performs filtering on a list of entities provided by the Application Service.
        return allRooms.stream()
                .filter(room -> room.getId().equals(roomId))
                .findFirst();
    }

    @Override
    public List<Room> getAllRooms(List<Room> allRooms) {
        // This method simply returns the provided list as the domain doesn't "fetch" directly.
        return allRooms;
    }

    @Override
    public List<Room> findAvailableRooms(List<Room> allRooms, Optional<RoomTypeId> roomTypeId, Optional<Integer> minFloor, Optional<Integer> maxFloor, Timestamp checkInDate, Timestamp checkOutDate) {
        // This is a complex query that involves business logic (availability based on bookings).
        // It filters based on the provided list of all rooms.
        List<Room> filteredRooms = allRooms.stream()
                .filter(room -> room.getRoomStatus().getRoomStatus() == ERoomStatus.VACANT.name()) // Only consider vacant rooms initially
                .filter(room -> roomTypeId.map(id -> room.getRoomType().getId().equals(id)).orElse(true)) // Filter by room type
                .filter(room -> minFloor.map(floor -> room.getFloor() >= floor).orElse(true)) // Filter by min floor
                .filter(room -> maxFloor.map(floor -> room.getFloor() <= floor).orElse(true)) // Filter by max floor
                // TODO: Add actual booking calendar logic here. This would require
                // the Application Service to pass booking data or the domain service
                // to evaluate a conceptual "booking schedule" passed to it.
                .collect(Collectors.toList());
        return filteredRooms;
    }

    @Override
    public List<Room> getRoomsByStatus(List<Room> allRooms, String statusName) {
        try {
            return allRooms.stream()
                    .filter(room -> room.getRoomStatus().getRoomStatus().equalsIgnoreCase(statusName))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RoomDomainException("Invalid room status name: " + statusName, e);
        }
    }

    @Override
    public Optional<RoomType> getRoomTypeById(List<RoomType> allRoomTypes, RoomTypeId roomTypeId) {
        return allRoomTypes.stream()
                .filter(roomType -> roomType.getId().equals(roomTypeId))
                .findFirst();
    }

    @Override
    public List<RoomType> getAllRoomTypes(List<RoomType> allRoomTypes) {
        return allRoomTypes;
    }

    /* --- RoomTypeCommandService Implementations --- */

    @Override
    public RoomType createRoomType(String typeName, String description, String basePriceStr, int maxOccupancy, List<FurnitureRequirement> furnitureItems) throws RoomDomainException {
        // NOTE: The check for typeName uniqueness must be done by the Application Service.

        // Parse and validate basePrice string
        BigDecimal basePriceAmount;
        try {
            basePriceAmount = new BigDecimal(basePriceStr);
            if (basePriceAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RoomDomainException("Base price must be positive.");
            }
        } catch (NumberFormatException e) {
            throw new RoomDomainException("Invalid base price format: " + basePriceStr, e);
        }

        // Domain validation for FurnitureRequirement: ensure Furniture entities are provided
        for (FurnitureRequirement fr : furnitureItems) {
            if (fr.getFurniture() == null || fr.getFurniture().getId() == null) {
                throw new RoomDomainException("Furniture in requirement cannot be null or have null ID.");
            }
            if (fr.getRequiredQuantity() <= 0) {
                throw new RoomDomainException("Required quantity for furniture must be positive.");
            }
        }

        RoomType newRoomType = new RoomType(typeName, description, new Money(basePriceAmount), maxOccupancy, new ArrayList<>(furnitureItems));

        newRoomType.validateRoomType(); // Perform domain validation
        return newRoomType; // Return for Application Service to persist
    }

    @Override
    public RoomType updateRoomTypeDetails(RoomType roomType, String newTypeName, String newDescription, String newBasePriceStr, int newMaxOccupancy) throws RoomDomainException {
        // NOTE: The check for newTypeName uniqueness must be done by the Application Service.

        roomType.setTypeName(newTypeName);
        roomType.setDescription(newDescription);

        BigDecimal newBasePriceAmount;
        try {
            newBasePriceAmount = new BigDecimal(newBasePriceStr);
            if (newBasePriceAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RoomDomainException("New base price must be positive.");
            }
        } catch (NumberFormatException e) {
            throw new RoomDomainException("Invalid new base price format: " + newBasePriceStr, e);
        }
        roomType.setBasePrice(new Money(newBasePriceAmount));
        roomType.setMaxOccupancy(newMaxOccupancy);

        roomType.validateRoomType(); // Re-validate entity
        return roomType;
    }

    @Override
    public void deleteRoomType(RoomType roomType) throws RoomDomainException {
        // NOTE: The check for associated rooms must be done by the Application Service.
        // This domain method assumes such checks passed.
        // If domain had a strong aggregate boundary or knew about "room usage", this would be here.
        // For example, if RoomType held references to Rooms and could query its children.
        // As per the architecture, Room entity holds RoomTypeId, so Application Service checks.
    }

    @Override
    public RoomType addFurnitureToRoomType(RoomType roomType, Furniture furniture, int quantity) throws RoomDomainException {
        roomType.addFurnitureRequirement(new FurnitureRequirement(furniture, quantity)); // Domain logic
        roomType.validateRoomType(); // Re-validate entity
        return roomType;
    }

    @Override
    public RoomType removeFurnitureFromRoomType(RoomType roomType, FurnitureId furnitureId) throws RoomDomainException {
        roomType.removeFurnitureRequirement(furnitureId); // Domain logic
        roomType.validateRoomType(); // Re-validate entity
        return roomType;
    }

    @Override
    public RoomType updateFurnitureQuantityInRoomType(RoomType roomType, FurnitureId furnitureId, int newQuantity) throws RoomDomainException {
        roomType.updateFurnitureRequirementQuantity(furnitureId, newQuantity); // Domain logic
        roomType.validateRoomType(); // Re-validate entity
        return roomType;
    }

    /* --- FurnitureCommandService Implementations --- */

    @Override
    public Furniture createFurniture(InventoryItemId inventoryItemId) throws RoomDomainException {
        // NOTE: The check for inventoryItemId uniqueness must be done by the Application Service.
        Furniture newFurniture = new Furniture(inventoryItemId);
        newFurniture.validate(); // Domain validation
        return newFurniture;
    }

    @Override
    public Furniture updateFurnitureInventoryItem(Furniture furniture, InventoryItemId newInventoryItemId) throws RoomDomainException {
        // NOTE: The check for newInventoryItemId uniqueness must be done by the Application Service.
        furniture.setInventoryItemId(newInventoryItemId); // Update entity's internal state
        return furniture;
    }

    @Override
    public void deleteFurniture(Furniture furniture) throws RoomDomainException {
        // NOTE: The check for usage by RoomTypes must be done by the Application Service.
        // This domain method assumes such checks passed.
    }

    /* --- FurnitureQueryService Implementations --- */

    @Override
    public Optional<Furniture> getFurnitureById(List<Furniture> allFurnitures, FurnitureId furnitureId) {
        return allFurnitures.stream()
                .filter(f -> f.getId().equals(furnitureId))
                .findFirst();
    }

    @Override
    public Optional<Furniture> getFurnitureByInventoryItemId(List<Furniture> allFurnitures, InventoryItemId inventoryItemId) {
        return allFurnitures.stream()
                .filter(f -> f.getInventoryItemId().equals(inventoryItemId))
                .findFirst();
    }

    @Override
    public List<Furniture> getAllFurnitures(List<Furniture> allFurnitures) {
        return allFurnitures;
    }

    /* --- MaintenanceCommandService Implementations --- */

    @Override
    public RoomMaintenance createRoomMaintenance(Room room, StaffId staffId, MaintenanceType maintenanceType, String description) throws RoomDomainException {
        // Create RoomMaintenance entity
        RoomMaintenance newMaintenance = RoomMaintenance.Builder.builder()
//                .id(new MaintenanceId(UUID.randomUUID().toString()))
                .room(room.getId()) // Reference to the room's ID
                .staffId(staffId)
                .maintenanceDate(Timestamp.from(Instant.now()))
                .maintenanceType(maintenanceType)
                .description(description)
                .maintenanceStatus(MaintenanceStatus.PENDING)
                .build();
        newMaintenance.validate();

        // Business logic: Set Room status to MAINTENANCE (inter-entity interaction)
        room.setMaintenanceRoomStatus(); // Domain logic on Room entity

        // Return both entities for Application Service to persist
        return newMaintenance;
    }

    @Override
    public RoomMaintenance startMaintenance(RoomMaintenance roomMaintenance) throws RoomDomainException {
        roomMaintenance.startMaintenance(); // Domain logic on RoomMaintenance entity
        return roomMaintenance;
    }

    @Override
    public RoomMaintenance completeMaintenance(RoomMaintenance roomMaintenance, Room room) throws RoomDomainException {
        roomMaintenance.completeMaintenance(); // Domain logic on RoomMaintenance entity

        // Business logic: Set Room status back to VACANT after maintenance completion
        room.setVacantRoomStatus(); // Domain logic on Room entity
        return roomMaintenance; // Return for Application Service to persist both
    }

    @Override
    public RoomMaintenance cancelMaintenance(RoomMaintenance roomMaintenance, Room room) throws RoomDomainException {
        roomMaintenance.cancelMaintenance(); // Domain logic on RoomMaintenance entity

        // Business logic: Set Room status back to VACANT if maintenance is canceled
        room.setVacantRoomStatus(); // Domain logic on Room entity
        return roomMaintenance; // Return for Application Service to persist both
    }

    @Override
    public RoomMaintenance updateMaintenanceDescription(RoomMaintenance roomMaintenance, String newDescription) throws RoomDomainException {
        roomMaintenance.updateDescription(newDescription); // Update entity's internal state
        return roomMaintenance;
    }

    @Override
    public RoomMaintenance assignStaffToMaintenance(RoomMaintenance roomMaintenance, StaffId newStaffId) throws RoomDomainException {
        roomMaintenance.assignStaff(newStaffId); // Update entity's internal state
        return roomMaintenance;
    }

    @Override
    public MaintenanceType createMaintenanceType(String name) throws RoomDomainException {
        // NOTE: The check for name uniqueness must be done by the Application Service.
        MaintenanceType newType = new MaintenanceType(name);
        newType.validate(); // Domain validation
        return newType;
    }

    @Override
    public MaintenanceType updateMaintenanceTypeName(MaintenanceType maintenanceType, String newName) throws RoomDomainException {
        // NOTE: The check for newName uniqueness must be done by the Application Service.
        maintenanceType.setName(newName); // Update entity's internal state
        return maintenanceType;
    }

    @Override
    public void deleteMaintenanceType(MaintenanceType maintenanceType, List<RoomMaintenance> allRoomMaintenances) throws RoomDomainException {
        // Business logic: Cannot delete if it's currently in use by any RoomMaintenance request
        boolean isInUse = allRoomMaintenances.stream()
                .anyMatch(rm -> rm.getMaintenanceType() != null && rm.getMaintenanceType().getId().equals(maintenanceType.getId()));
        if (isInUse) {
            throw new RoomDomainException("Cannot delete MaintenanceType with ID " + maintenanceType.getId().getValue() + " as it is currently in use.");
        }
        // No persistence call here, the Application Service will handle the actual delete operation.
    }

    /* --- MaintenanceQueryService Implementations --- */

    @Override
    public Optional<RoomMaintenance> getRoomMaintenanceById(List<RoomMaintenance> allMaintenances, MaintenanceId maintenanceId) {
        return allMaintenances.stream()
                .filter(rm -> rm.getId().equals(maintenanceId))
                .findFirst();
    }

    @Override
    public List<RoomMaintenance> getAllRoomMaintenances(List<RoomMaintenance> allMaintenances) {
        return allMaintenances;
    }

    @Override
    public List<RoomMaintenance> getRoomMaintenancesByRoomId(List<RoomMaintenance> allMaintenances, RoomId roomId) {
        return allMaintenances.stream()
                .filter(rm -> rm.getRoom().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> getRoomMaintenancesByStaffId(List<RoomMaintenance> allMaintenances, StaffId staffId) {
        return allMaintenances.stream()
                .filter(rm -> rm.getStaffId() != null && rm.getStaffId().equals(staffId))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> getRoomMaintenancesByStatus(List<RoomMaintenance> allMaintenances, String status) {
        try {
            MaintenanceStatus maintenanceStatus = MaintenanceStatus.valueOf(status.toUpperCase());
            return allMaintenances.stream()
                    .filter(rm -> rm.getMaintenanceStatus() == maintenanceStatus)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RoomDomainException("Invalid maintenance status name: " + status, e);
        }
    }

    @Override
    public List<RoomMaintenance> getRoomMaintenancesBetweenDates(List<RoomMaintenance> allMaintenances, Timestamp startDate, Timestamp endDate) {
        return allMaintenances.stream()
                .filter(rm -> rm.getMaintenanceDate().after(startDate) && rm.getMaintenanceDate().before(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MaintenanceType> getMaintenanceTypeById(List<MaintenanceType> allMaintenanceTypes, MaintenanceTypeId maintenanceTypeId) {
        return allMaintenanceTypes.stream()
                .filter(mt -> mt.getId().equals(maintenanceTypeId))
                .findFirst();
    }

    @Override
    public List<MaintenanceType> getAllMaintenanceTypes(List<MaintenanceType> allMaintenanceTypes) {
        return allMaintenanceTypes;
    }
}
