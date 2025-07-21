package com.poly.room.management.domain.service.sub;

import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.exception.RoomDomainException;

import java.util.List;

public interface MaintenanceCommandService {
    /**
     * Creates a new room maintenance request. The Application Service is responsible
     * for loading the Room and MaintenanceType entities.
     * @param room The Room entity to be maintained.
     * @param staffId The ID of the responsible staff (if any).
     * @param maintenanceType The loaded MaintenanceType entity.
     * @param description Detailed description of the maintenance request.
     * @return The created and validated RoomMaintenance entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    RoomMaintenance createRoomMaintenance(Room room, StaffId staffId, MaintenanceType maintenanceType, String description) throws RoomDomainException;

    /**
     * Starts the maintenance process for an existing request. The Application Service loads
     * the RoomMaintenance entity.
     * @param roomMaintenance The RoomMaintenance entity to update.
     * @return The updated RoomMaintenance entity.
     * @throws RoomDomainException If the maintenance cannot be started based on domain rules.
     */
    RoomMaintenance startMaintenance(RoomMaintenance roomMaintenance) throws RoomDomainException;

    /**
     * Completes the maintenance process for a request. The Application Service loads
     * the RoomMaintenance and associated Room entities.
     * @param roomMaintenance The RoomMaintenance entity to update.
     * @param room The associated Room entity.
     * @return The updated RoomMaintenance entity.
     * @throws RoomDomainException If the maintenance cannot be completed based on domain rules.
     */
    RoomMaintenance completeMaintenance(RoomMaintenance roomMaintenance, Room room) throws RoomDomainException;

    /**
     * Cancels a maintenance request. The Application Service loads the RoomMaintenance
     * and associated Room entities.
     * @param roomMaintenance The RoomMaintenance entity to update.
     * @param room The associated Room entity.
     * @return The updated RoomMaintenance entity.
     * @throws RoomDomainException If the maintenance cannot be canceled based on domain rules.
     */
    RoomMaintenance cancelMaintenance(RoomMaintenance roomMaintenance, Room room) throws RoomDomainException;

    /**
     * Updates the description for a maintenance request. The Application Service loads
     * the RoomMaintenance entity.
     * @param roomMaintenance The RoomMaintenance entity to update.
     * @param newDescription The new description.
     * @return The updated RoomMaintenance entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    RoomMaintenance updateMaintenanceDescription(RoomMaintenance roomMaintenance, String newDescription) throws RoomDomainException;

    /**
     * Assigns or changes the responsible staff for a maintenance request. The Application Service loads
     * the RoomMaintenance entity.
     * @param roomMaintenance The RoomMaintenance entity to update.
     * @param newStaffId The ID of the new staff member.
     * @return The updated RoomMaintenance entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    RoomMaintenance assignStaffToMaintenance(RoomMaintenance roomMaintenance, StaffId newStaffId) throws RoomDomainException;

    /**
     * Creates a new maintenance type. The Application Service is responsible for
     * ensuring name uniqueness.
     * @param name The name of the maintenance type.
     * @return The created and validated MaintenanceType entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    MaintenanceType createMaintenanceType(String name) throws RoomDomainException;

    /**
     * Updates the name of a maintenance type. The Application Service loads
     * the MaintenanceType entity and ensures newName uniqueness.
     * @param maintenanceType The MaintenanceType entity to update.
     * @param newName The new name.
     * @return The updated and validated MaintenanceType entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    MaintenanceType updateMaintenanceTypeName(MaintenanceType maintenanceType, String newName) throws RoomDomainException;

    /**
     * Deletes a maintenance type. The Application Service loads the MaintenanceType
     * entity and ensures it's not in use by any RoomMaintenance.
     * @param maintenanceType The MaintenanceType entity to delete.
     * @param allRoomMaintenances A list of all RoomMaintenance entities (for checking usage).
     * @throws RoomDomainException If the maintenance type cannot be deleted based on domain rules.
     */
    void deleteMaintenanceType(MaintenanceType maintenanceType, List<RoomMaintenance> allRoomMaintenances) throws RoomDomainException;
}
