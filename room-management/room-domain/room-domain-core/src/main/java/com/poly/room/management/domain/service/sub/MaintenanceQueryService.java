package com.poly.room.management.domain.service.sub;

import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.RoomId;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.valueobject.MaintenanceId;
import com.poly.room.management.domain.valueobject.MaintenanceTypeId;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface MaintenanceQueryService {
    /**
     * Retrieves detailed information of a maintenance request from a provided list.
     * The Application Service is responsible for providing the list.
     *
     * @param allMaintenances A list of all maintenance requests.
     * @param maintenanceId   The ID of the maintenance request to find.
     * @return An Optional containing the RoomMaintenance object if found, otherwise Optional.empty().
     */
    Optional<RoomMaintenance> getRoomMaintenanceById(List<RoomMaintenance> allMaintenances, MaintenanceId maintenanceId);

    /**
     * Retrieves a list of all maintenance requests (typically provided by Application Service).
     * This method simply returns the provided list as the domain doesn't "fetch" directly.
     *
     * @param allMaintenances A list of all maintenance requests.
     * @return A list of RoomMaintenance objects.
     */
    List<RoomMaintenance> getAllRoomMaintenances(List<RoomMaintenance> allMaintenances);

    /**
     * Filters a list of maintenance requests for a specific room.
     *
     * @param allMaintenances A list of all maintenance requests.
     * @param roomId          The ID of the room.
     * @return A list of maintenance requests related to that room.
     */
    List<RoomMaintenance> getRoomMaintenancesByRoomId(List<RoomMaintenance> allMaintenances, RoomId roomId);

    /**
     * Filters a list of maintenance requests assigned to a specific staff member.
     *
     * @param allMaintenances A list of all maintenance requests.
     * @param staffId         The ID of the staff member.
     * @return A list of maintenance requests assigned to that staff member.
     */
    List<RoomMaintenance> getRoomMaintenancesByStaffId(List<RoomMaintenance> allMaintenances, StaffId staffId);

    /**
     * Filters a list of maintenance requests by a specific status.
     *
     * @param allMaintenances A list of all maintenance requests.
     * @param status          The maintenance status (e.g., "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELED").
     * @return A list of maintenance requests with the corresponding status.
     */
    List<RoomMaintenance> getRoomMaintenancesByStatus(List<RoomMaintenance> allMaintenances, String status);

    /**
     * Filters a list of maintenance requests within a specific date range.
     *
     * @param allMaintenances A list of all maintenance requests.
     * @param startDate       The start date.
     * @param endDate         The end date.
     * @return A list of maintenance requests within that date range.
     */
    List<RoomMaintenance> getRoomMaintenancesBetweenDates(List<RoomMaintenance> allMaintenances, DateCustom startDate, DateCustom endDate);

    /**
     * Retrieves detailed information of a maintenance type from a provided list.
     *
     * @param allMaintenanceTypes A list of all maintenance types.
     * @param maintenanceTypeId   The ID of the maintenance type.
     * @return An Optional containing the MaintenanceType object if found, otherwise Optional.empty().
     */
    Optional<MaintenanceType> getMaintenanceTypeById(List<MaintenanceType> allMaintenanceTypes, MaintenanceTypeId maintenanceTypeId);

    /**
     * Retrieves a list of all maintenance types (typically provided by Application Service).
     * This method simply returns the provided list as the domain doesn't "fetch" directly.
     *
     * @param allMaintenanceTypes A list of all maintenance types.
     * @return A list of MaintenanceType objects.
     */
    List<MaintenanceType> getAllMaintenanceTypes(List<MaintenanceType> allMaintenanceTypes);
}
