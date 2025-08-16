package com.poly.room.management.domain.service.sub;

import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.valueobject.FurnitureId;

import java.util.List;
import java.util.Optional;

public interface FurnitureQueryService {
    /**
     * Retrieves detailed information of a furniture item from a provided list.
     * The Application Service is responsible for providing the list.
     * @param allFurnitures A list of all furniture items.
     * @param furnitureId The ID of the furniture to find.
     * @return An Optional containing the Furniture object if found, otherwise Optional.empty().
     */
    Optional<Furniture> getFurnitureById(List<Furniture> allFurnitures, FurnitureId furnitureId);
    /**
     * Retrieves a list of all furniture types (typically provided by Application Service).
     * This method simply returns the provided list as the domain doesn't "fetch" directly.
     * @param allFurnitures A list of all furniture types.
     * @return A list of Furniture objects.
     */
    List<Furniture> getAllFurnitures(List<Furniture> allFurnitures);
}
