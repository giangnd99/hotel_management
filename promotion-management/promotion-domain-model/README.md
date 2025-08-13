# Promotion Domain Model Module

## Overview

The Promotion Domain Model Module is a comprehensive implementation of a hotel promotion system built using **Domain-Driven Design (DDD)** principles and **Hexagonal Architecture**. This module provides the core business logic for managing voucher packs, individual vouchers, and promotional campaigns.

## Architecture

### Hexagonal Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                    External Interfaces                      │
│  (REST APIs, Event Handlers, External Systems)            │
├─────────────────────────────────────────────────────────────┤
│                 Application Layer                           │
│  (Services, DTOs, API Controllers)                         │
├─────────────────────────────────────────────────────────────┤
│                   Domain Layer                             │
│  (Entities, Value Objects, Domain Events, Business Rules) │
├─────────────────────────────────────────────────────────────┤
│                 Infrastructure Layer                        │
│  (Repository Implementations, Database, External APIs)     │
└─────────────────────────────────────────────────────────────┘
```

### Module Structure

```
promotion-domain-model/
├── promotion-domain-core/           # Core domain entities and business logic
│   ├── entity/                      # Domain entities
│   ├── valueobject/                 # Value objects
│   └── event/                       # Domain events
├── promotion-domain-application/    # Application services and DTOs
│   ├── service/                     # Service interfaces and implementations
│   ├── dto/                         # Data Transfer Objects
│   ├── api/                         # API implementations
│   └── spi/                         # Service Provider Interfaces (repositories)
└── pom.xml                         # Maven configuration
```

## Core Domain Concepts

### 1. VoucherPack Entity

**Purpose**: Represents a collection of vouchers that share common characteristics.

**Key Features**:
- **Stock Management**: Tracks available quantity for redemption
- **Validity Periods**: Configurable pack and voucher validity dates
- **Status Lifecycle**: PENDING → PUBLISHED → CLOSED/EXPIRED
- **Business Rules**: Enforces quantity, points, and date validations

**Business Rules**:
- Voucher packs must have positive quantity
- Required points must be positive
- Pack validity dates must be logical (validFrom ≤ validTo)
- Only published packs can have vouchers redeemed
- Packs are automatically closed when stock reaches zero

### 2. Voucher Entity

**Purpose**: Represents an individual voucher that a customer has redeemed.

**Key Features**:
- **Unique Identification**: Each voucher has a unique code
- **Customer Ownership**: Links vouchers to specific customers
- **Validity Management**: Automatic expiration based on dates
- **Status Transitions**: PENDING → REDEEMED → USED/EXPIRED

**Business Rules**:
- Vouchers are created with PENDING status during redemption
- Only REDEEMED vouchers can be used in transactions
- Vouchers automatically expire when they pass their validity date
- Each voucher has a unique voucher code for identification

### 3. Value Objects

#### Discount Interface
- **Purpose**: Common contract for different discount types
- **Implementations**: 
  - `DiscountPercentage`: Percentage-based discounts (0-100%)
  - `DiscountAmount`: Fixed-amount discounts (≥1000 VND)
- **Features**: Precise calculations using BigDecimal, automatic capping

#### DateRange
- **Purpose**: Represents duration or time spans
- **Usage**: Voucher validity periods, promotion durations
- **Features**: Immutable, validation, unit conversion utilities

#### Status Enums
- **VoucherPackStatus**: PENDING, PUBLISHED, CLOSED, EXPIRED
- **VoucherStatus**: PENDING, REDEEMED, USED, EXPIRED
- **Features**: Status transition validation, business rule enforcement

### 4. Domain Events

#### Base Classes
- **DomainEvent**: Abstract base for all domain events
- **Features**: Unique IDs, timestamps, event types, utility methods

#### Specific Events
- **VoucherPackCreatedEvent**: Raised when new voucher packs are created
- **VoucherRedeemedEvent**: Raised when customers redeem vouchers
- **Usage**: Audit trails, external system notifications, analytics

## Application Layer

### 1. Service Interfaces

#### VoucherPackService
**Responsibilities**:
- Voucher pack creation and configuration
- Status management and transitions
- Stock management and validation
- Business rule enforcement
- Expiration management

**Key Methods**:
- `createVoucherPack()`: Creates new voucher packs
- `updatePendingVoucherPack()`: Modifies packs before publication
- `closeVoucherPack()`: Manually closes packs
- `markExpiredVoucherPacks()`: Automatic expiration processing

#### VoucherService
**Responsibilities**:
- Voucher redemption from voucher packs
- Voucher application to transactions
- Voucher status management and validation
- Automatic expiration processing
- Customer voucher retrieval and filtering

**Key Methods**:
- `redeemVoucherFromPack()`: Creates vouchers from packs
- `applyVoucher()`: Applies vouchers to transactions
- `expireExpiredVouchers()`: Automatic expiration processing

#### ExpirationManagementService
**Responsibilities**:
- Automatic expiration of expired voucher packs
- Automatic expiration of expired vouchers
- Comprehensive expiration checks across the system
- Coordination of expiration operations
- Performance monitoring and reporting

**Key Methods**:
- `markExpiredVoucherPacks()`: Expires eligible packs
- `markExpiredVouchers()`: Expires eligible vouchers
- `performComprehensiveExpirationCheck()`: Coordinated expiration with metrics

### 2. Data Transfer Objects (DTOs)

#### Request DTOs
- **VoucherPackCreateRequest**: Voucher pack creation data
- **VoucherPackUpdateRequest**: Voucher pack modification data

#### Response DTOs
- **VoucherPackInternalResponse**: Internal voucher pack data
- **VoucherPackExternalResponse**: External-facing voucher pack data
- **VoucherExternalResponse**: External-facing voucher data

**Features**:
- Input validation and sanitization
- Entity conversion methods
- Business rule enforcement
- API contract definition

### 3. Repository Interfaces

#### VoucherPackRepository
**Responsibilities**:
- CRUD operations for voucher pack entities
- Status-based queries and filtering
- Stock management operations
- Expiration eligibility queries
- Bulk status updates

**Key Methods**:
- `existsById()`: Lightweight existence checks
- `getAllVoucherPacksWithStatus()`: Status-based filtering
- `markExpiredVoucherPacks()`: Bulk expiration operations
- `getVoucherPacksEligibleForExpiration()`: Expiration queries

#### VoucherRepository
**Responsibilities**:
- CRUD operations for voucher entities
- Status-based queries and filtering
- Customer-specific voucher retrieval
- Expiration eligibility queries
- Bulk status updates and operations

**Key Methods**:
- `getVoucherByCode()`: Voucher lookup by code
- `getAllVouchersWithStatus()`: Customer voucher filtering
- `markExpiredVouchers()`: Bulk expiration operations
- `updateVoucherStatusBatch()`: Efficient bulk updates

## Business Rules and Validation

### 1. Voucher Pack Rules
- **Creation**: Must have valid description, quantity, and points
- **Modification**: Only PENDING packs can be modified
- **Publication**: Packs must be valid before publication
- **Redemption**: Only PUBLISHED packs with stock can be redeemed
- **Expiration**: Automatic expiration based on packValidTo dates

### 2. Voucher Rules
- **Redemption**: Must come from valid, published packs
- **Usage**: Only REDEEMED vouchers within validity period
- **Ownership**: Vouchers can only be used by their rightful owner
- **Expiration**: Automatic expiration based on validTo dates
- **Uniqueness**: Each voucher code must be unique

### 3. Status Transition Rules
- **VoucherPackStatus**: Enforces logical progression through states
- **VoucherStatus**: Prevents invalid status changes
- **System Operations**: Allows EXPIRED status from any current status

## Expiration Management

### 1. Automatic Expiration
- **Voucher Packs**: PUBLISHED or PENDING packs past packValidTo
- **Vouchers**: REDEEMED vouchers past validTo
- **Processing Order**: Packs first, then vouchers
- **Error Handling**: Continues processing even if individual items fail

### 2. Scheduled Operations
- **Frequency**: Typically daily (configurable)
- **Performance**: Includes timing metrics for monitoring
- **Coordination**: Ensures proper order of operations
- **Reporting**: Returns detailed operation summaries

### 3. Manual Operations
- **Administrative**: Manual expiration of specific items
- **Bulk Operations**: Efficient processing of multiple items
- **Validation**: Business rule enforcement during operations

## Usage Examples

### 1. Creating a Voucher Pack
```java
VoucherPackService service = // ... get service instance

VoucherPack pack = VoucherPack.builder()
    .description("Summer Sale - 20% Off")
    .discountAmount(new DiscountPercentage(20.0))
    .voucherValidRange(new DateRange(30, ChronoUnit.DAYS))
    .requiredPoints(1000L)
    .quantity(100)
    .packValidFrom(LocalDate.now())
    .packValidTo(LocalDate.now().plusMonths(3))
    .build();

VoucherPack created = service.createVoucherPack(pack, "admin@hotel.com");
```

### 2. Redeeming Vouchers
```java
VoucherService voucherService = // ... get service instance

Voucher voucher = voucherService.redeemVoucherFromPack(
    packId, 
    "customer-uuid", 
    2
);
```

### 3. Applying Vouchers
```java
Voucher applied = voucherService.applyVoucher(
    "VOUCHER-ABC123", 
    "customer-uuid"
);
```

### 4. Expiration Management
```java
ExpirationManagementService expirationService = // ... get service instance

ExpirationSummary summary = expirationService.performComprehensiveExpirationCheck();
System.out.println("Expired " + summary.expiredVoucherPacks() + " packs and " + 
                  summary.expiredVouchers() + " vouchers in " + 
                  summary.totalProcessingTimeMs() + "ms");
```

## Testing

### 1. Unit Tests
- **Service Layer**: Business logic validation
- **Entity Validation**: Business rule enforcement
- **Value Object**: Immutability and validation
- **Status Transitions**: State machine validation

### 2. Integration Tests
- **Repository Operations**: Data persistence validation
- **Service Coordination**: End-to-end workflow testing
- **Business Rules**: Cross-entity validation

### 3. Test Dependencies
```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
```

## Configuration

### 1. Maven Dependencies
- **Lombok**: Reduces boilerplate code
- **Spring Framework**: Dependency injection and configuration
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework

### 2. Build Configuration
- **Java Version**: 17+
- **Encoding**: UTF-8
- **Compilation**: Maven-based build system

## Performance Considerations

### 1. Database Operations
- **Bulk Updates**: Efficient expiration processing
- **Indexing**: Status and date-based queries
- **Transactions**: Proper transaction management
- **Connection Pooling**: Database connection optimization

### 2. Memory Management
- **Immutable Objects**: Value objects are immutable
- **Lazy Loading**: Entity relationships loaded on demand
- **Batch Processing**: Efficient bulk operations

### 3. Caching Strategy
- **Entity Caching**: Frequently accessed entities
- **Query Results**: Status-based query caching
- **Expiration**: Time-based cache invalidation

## Security Considerations

### 1. Input Validation
- **Parameter Validation**: All inputs are validated
- **Business Rule Enforcement**: Domain-level security
- **SQL Injection Prevention**: Parameterized queries

### 2. Access Control
- **Customer Isolation**: Vouchers are customer-specific
- **Administrative Access**: Service-level access control
- **Audit Logging**: All operations are logged

## Monitoring and Observability

### 1. Performance Metrics
- **Expiration Timing**: Processing time monitoring
- **Operation Counts**: Success/failure tracking
- **Resource Usage**: Memory and database monitoring

### 2. Business Metrics
- **Voucher Redemption Rates**: Customer engagement
- **Expiration Patterns**: Usage optimization
- **Error Rates**: System health monitoring

## Future Enhancements

### 1. Planned Features
- **Advanced Discount Types**: Tiered discounts, conditional offers
- **Customer Segmentation**: Targeted promotions
- **Analytics Integration**: Business intelligence
- **Multi-tenancy**: Hotel chain support

### 2. Technical Improvements
- **Event Sourcing**: Complete audit trail
- **CQRS**: Read/write optimization
- **Microservices**: Service decomposition
- **Cloud Native**: Containerization and scaling

## Contributing

### 1. Development Guidelines
- **DDD Principles**: Maintain domain focus
- **Clean Code**: Readable and maintainable code
- **Test Coverage**: Comprehensive testing
- **Documentation**: Clear and complete documentation

### 2. Code Review Process
- **Business Logic**: Domain expert review
- **Technical Quality**: Senior developer review
- **Testing**: Test coverage validation
- **Documentation**: Documentation completeness

## Support and Maintenance

### 1. Issue Reporting
- **Bug Reports**: Detailed reproduction steps
- **Feature Requests**: Business justification
- **Performance Issues**: Metrics and context

### 2. Version Management
- **Semantic Versioning**: MAJOR.MINOR.PATCH
- **Backward Compatibility**: API stability
- **Migration Guides**: Upgrade instructions

---

**Version**: 1.0  
**Last Updated**: 2024  
**Author**: Hotel Management System Team  
**License**: Proprietary
