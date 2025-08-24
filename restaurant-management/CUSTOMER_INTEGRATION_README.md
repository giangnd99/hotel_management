# Customer Management Integration for Restaurant Service

This document describes the integration between the Restaurant Management Service and the Customer Management Service using Feign clients, following Clean Architecture principles.

## Overview

The restaurant service now integrates with the customer management service to retrieve customer information during the order flow. This integration follows Clean Architecture principles and allows the restaurant service to:

- Validate customer existence and status
- Retrieve customer details for order processing
- Get customer profiles for personalized service
- Handle customer data in a distributed microservices architecture

## Clean Architecture Structure

### 1. Application Layer (`restaurant-application`)
- **Controllers**: Handle HTTP requests and responses
- **DTOs**: Data transfer objects for API communication
- **Feign Clients**: HTTP client implementations for external services
- **Configuration**: Feign client configurations
- **Adapters**: Implementations of port out interfaces

### 2. Domain Layer (`restaurant-domain/restaurant-application-service`)
- **Use Cases**: Business logic and orchestration
- **Port In**: Interfaces for incoming dependencies
- **Port Out**: Interfaces for outgoing dependencies
- **DTOs**: Domain-specific data transfer objects

### 3. Infrastructure Layer (`restaurant-container`)
- **Configuration**: External service configurations

## Components

### 1. Application Layer

#### CustomerIntegrationController
```java
@RestController
@RequestMapping("/api/restaurant/customers")
public class CustomerIntegrationController {
    private final CustomerIntegrationUseCase customerIntegrationUseCase;
    // REST endpoints for customer operations
}
```

#### CustomerDTO
```java
public class CustomerDTO {
    private UUID customerId;
    private UUID userId;
    private String firstName;
    private String lastName;
    private String address;
    private LocalDate dateOfBirth;
    private BigDecimal accumulatedSpending;
    private String level; // NONE, BRONZE, SILVER, GOLD, PLATINUM, DIAMOND
    private String imageUrl;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String sex; // FEMALE, MALE, OTHER
    private boolean active;
}
```

#### CustomerManagementClient (Feign Client)
```java
@FeignClient(
    name = "customer-management",
    url = "${customer.service.url:http://localhost:8099}",
    fallback = CustomerManagementClientFallback.class
)
public interface CustomerManagementClient {
    // HTTP client methods
}
```

#### CustomerManagementClientFallback
```java
@Component
public class CustomerManagementClientFallback implements CustomerManagementClient {
    // Fallback implementation for service unavailability
}
```

#### FeignConfig
```java
@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
```

### 2. Domain Layer

#### CustomerIntegrationUseCase (Port In)
```java
public interface CustomerIntegrationUseCase {
    List<CustomerDTO> getAllCustomers(int page, int size);
    CustomerDTO getCustomerById(UUID customerId);
    CustomerDTO getCustomerProfileByUserId(UUID userId);
    CustomerDTO getCustomerProfileById(UUID customerId);
    boolean isCustomerValid(UUID customerId);
    String getCustomerFullName(UUID customerId);
}
```

#### CustomerManagementPort (Port Out)
```java
public interface CustomerManagementPort {
    List<CustomerDTO> getAllCustomers(int page, int size);
    CustomerDTO getCustomerById(UUID customerId);
    CustomerDTO getCustomerProfileByUserId(UUID userId);
    CustomerDTO getCustomerProfileById(UUID customerId);
}
```

#### CustomerIntegrationUseCaseImpl
```java
@Service
public class CustomerIntegrationUseCaseImpl implements CustomerIntegrationUseCase {
    private final CustomerManagementPort customerManagementPort;
    // Business logic implementation
}
```

### 3. Application Layer

#### CustomerManagementAdapter
```java
@Component
public class CustomerManagementAdapter implements CustomerManagementPort {
    private final CustomerManagementClient customerManagementClient;
    // Implementation of port out interface with DTO mapping
}
```

## Configuration

### Application Properties

```yaml
customer:
  service:
    url: http://localhost:8099

feign:
  client:
    config:
      customer-management:
        connectTimeout: 5000
        readTimeout: 10000
        loggerLevel: full
  circuitbreaker:
    enabled: true
```

### Feign Configuration

```java
@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
}
```

## Error Handling

### Fallback Implementation
The integration includes a fallback mechanism (`CustomerManagementClientFallback`) that handles service unavailability gracefully.

### Custom Error Decoder
Custom error handling for different HTTP status codes:
- 4xx errors: Client errors
- 5xx errors: Server errors

## Usage in Order Flow

### Example: Creating an Order with Customer Validation

```java
@Service
public class OrderService {
    private final CustomerIntegrationUseCase customerIntegrationUseCase;
    
    public OrderDTO createOrder(OrderRequest request) {
        // Validate customer exists and is active
        if (!customerIntegrationUseCase.isCustomerValid(request.getCustomerId())) {
            throw new CustomerNotFoundException("Customer not found or inactive");
        }
        
        // Get customer details for order
        CustomerDTO customer = customerIntegrationUseCase.getCustomerById(request.getCustomerId());
        
        // Create order with customer information
        OrderDTO order = new OrderDTO();
        order.setCustomerId(customer.getCustomerId());
        order.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        // ... other order creation logic
        
        return order;
    }
}
```

## Testing

### Test Endpoints

1. **Get All Customers:**
   ```bash
   GET http://localhost:8082/api/restaurant/customers?page=0&size=10
   ```

2. **Get Customer by ID:**
   ```bash
   GET http://localhost:8082/api/restaurant/customers/{customerId}
   ```

3. **Validate Customer:**
   ```bash
   GET http://localhost:8082/api/restaurant/customers/{customerId}/validate
   ```

### Expected Response Format

```json
{
  "customerId": "11111111-1111-1111-1111-111111111111",
  "userId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "firstName": "Nguyen",
  "lastName": "Van A",
  "address": "123A Lê Lợi, Q1, TP.HCM",
  "dateOfBirth": "2000-01-01",
  "accumulatedSpending": 1200.50,
  "level": "BRONZE",
  "imageUrl": null,
  "createdDate": "2025-01-01T10:00:00",
  "updatedDate": "2025-01-01T10:00:00",
  "sex": "MALE",
  "active": true
}
```

## Dependencies

### Required Dependencies

#### Application Module (`restaurant-application/pom.xml`)
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

#### Domain Module (`restaurant-domain/restaurant-application-service/pom.xml`)
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

## Clean Architecture Benefits

1. **Separation of Concerns**: Each layer has a specific responsibility
2. **Dependency Inversion**: Domain layer doesn't depend on infrastructure
3. **Testability**: Easy to mock dependencies and test business logic
4. **Maintainability**: Changes in one layer don't affect others
5. **Flexibility**: Easy to swap implementations (e.g., different HTTP clients)

## File Structure

```
restaurant-management/
├── restaurant-application/
│   └── src/main/java/com/poly/restaurant/
│       ├── controller/
│       │   └── CustomerIntegrationController.java
│       ├── client/
│       │   ├── CustomerManagementClient.java
│       │   └── CustomerManagementClientFallback.java
│       ├── config/
│       │   └── FeignConfig.java
│       ├── adapter/
│       │   └── CustomerManagementAdapter.java
│       └── dto/
│           ├── CustomerDTO.java
│           ├── ApiResponseDTO.java
│           └── PageResultDTO.java
├── restaurant-domain/
│   └── restaurant-application-service/
│       └── src/main/java/com/poly/restaurant/application/
│           ├── port/in/
│           │   ├── CustomerIntegrationUseCase.java
│           │   └── impl/CustomerIntegrationUseCaseImpl.java
│           ├── port/out/
│           │   └── CustomerManagementPort.java
│           └── dto/
│               └── CustomerDTO.java
└── restaurant-container/
    └── src/main/java/com/poly/restaurant/
        └── RestaurantApplication.java
```

## Notes

- The customer management service must be running on port 8099
- The integration includes circuit breaker support for resilience
- All customer operations are logged for debugging purposes
- The service gracefully handles customer management service unavailability
- The architecture follows Clean Architecture principles with proper separation of concerns
- Feign clients and adapters are now located in the application module for better organization
- The adapter includes DTO mapping between application and domain layers
