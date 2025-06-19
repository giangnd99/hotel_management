package com.poly.room.management.domain.service.sub;

import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.FurnitureId;

public interface FurnitureCommandService {
    /**
     * Creates a new furniture type. The Application Service is responsible for
     * ensuring inventoryItemId uniqueness.
     * @param inventoryItemId The ID of the related inventory item.
     * @return The created and validated Furniture entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    Furniture createFurniture(InventoryItemId inventoryItemId) throws RoomDomainException;

    /**
     * Updates the related inventory item for a furniture. The Application Service loads
     * the Furniture entity and ensures newInventoryItemId uniqueness.
     * @param furniture The Furniture entity to update.
     * @param newInventoryItemId The new inventory item ID.
     * @return The updated and validated Furniture entity.
     * @throws RoomDomainException If internal domain validation fails.
     */
    Furniture updateFurnitureInventoryItem(Furniture furniture, InventoryItemId newInventoryItemId) throws RoomDomainException;

    /**
     * Deletes a furniture type. The Application Service loads the Furniture entity and ensures
     * it's not in use by any RoomType.
     * @param furniture The Furniture entity to delete.
     * @throws RoomDomainException If the furniture cannot be deleted based on domain rules.
     */
    void deleteFurniture(Furniture furniture) throws RoomDomainException;
}
