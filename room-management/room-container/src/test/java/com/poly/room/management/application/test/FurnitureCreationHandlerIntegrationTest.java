import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.dto.response.ItemDTO;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.handler.FurnitureCreationHandler;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.client.TestRestTemplate;
import com.poly.application.handler.ApplicationServiceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FurnitureCreationHandlerIntegrationTest {

    @Autowired
    private FurnitureCreationHandler furnitureCreationHandler;

    @Autowired
    private FurnitureRepository furnitureRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    // Assuming these are valid inventory item IDs that exist in your inventory service
    private static final String VALID_INVENTORY_ID = "INV-001";
    private static final String NON_EXISTENT_INVENTORY_ID = "INV-999";

    @BeforeEach
    void setUp() {
        // Clean up the furniture repository before each test
        furnitureRepository.deleteAll();
    }

    @Test
    @DisplayName("Should successfully create furniture with existing inventory item")
    @Transactional
    void createFurniture_WithValidInventoryItem_ShouldSucceed() {
        // Act
        FurnitureResponse response = furnitureCreationHandler.createFurniture(VALID_INVENTORY_ID);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getInventoryItemId()).isEqualTo(VALID_INVENTORY_ID);
        
        // Verify the furniture was saved in the database
        Furniture savedFurniture = furnitureRepository.findById(response.getId())
                .orElseThrow();
        assertThat(savedFurniture.getInventoryItemId().getValue()).isEqualTo(VALID_INVENTORY_ID);
    }

    @Test
    @DisplayName("Should throw exception when inventory item doesn't exist")
    void createFurniture_WithNonExistentInventoryItem_ShouldThrowException() {
        // Act & Assert
        assertThrows(ApplicationServiceException.class, 
            () -> furnitureCreationHandler.createFurniture(NON_EXISTENT_INVENTORY_ID),
            "Item not found"
        );
    }

    @Test
    @DisplayName("Should throw exception when inventory item ID is empty")
    void createFurniture_WithEmptyInventoryId_ShouldThrowException() {
        // Act & Assert
        assertThrows(ApplicationServiceException.class, 
            () -> furnitureCreationHandler.createFurniture(""),
            "inventoryItemId cannot be null or empty"
        );
    }

    @Test
    @DisplayName("Verify inventory item details after furniture creation")
    @Transactional
    void createFurniture_ShouldHaveCorrectInventoryDetails() {
        // First create the furniture
        FurnitureResponse furnitureResponse = furnitureCreationHandler.createFurniture(VALID_INVENTORY_ID);
        
        // Then verify the inventory item details using REST template
        String inventoryServiceUrl = "http://localhost:8081/mock/api/inventory/items/" + VALID_INVENTORY_ID;
        ItemDTO inventoryItem = restTemplate.getForObject(inventoryServiceUrl, ItemDTO.class);
        
        assertThat(inventoryItem).isNotNull();
        assertThat(inventoryItem.getItemCode()).isEqualTo(VALID_INVENTORY_ID);
        assertThat(furnitureResponse.getInventoryItemId()).isEqualTo(inventoryItem.getItemCode());
    }

    @Test
    @DisplayName("Should create multiple furniture items with same inventory item")
    @Transactional
    void createMultipleFurniture_WithSameInventoryItem_ShouldSucceed() {
        // Create first furniture
        FurnitureResponse response1 = furnitureCreationHandler.createFurniture(VALID_INVENTORY_ID);
        
        // Create second furniture
        FurnitureResponse response2 = furnitureCreationHandler.createFurniture(VALID_INVENTORY_ID);
        
        // Assert
        assertThat(response1).isNotNull();
        assertThat(response2).isNotNull();
        assertThat(response1.getId()).isNotEqualTo(response2.getId());
        assertThat(response1.getInventoryItemId()).isEqualTo(response2.getInventoryItemId());
        
        // Verify both furniture items exist in database
        assertThat(furnitureRepository.findById(response1.getId())).isPresent();
        assertThat(furnitureRepository.findById(response2.getId())).isPresent();
    }
}