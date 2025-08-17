# Promotion Management - Data Access Module

## Overview

The Data Access Module is responsible for persisting and retrieving domain entities from the database. It implements the repository interfaces defined in the domain layer and provides a clean separation between business logic and data persistence concerns.

## Architecture

This module follows the **Hexagonal Architecture** pattern and implements the **Repository Pattern**:

```
┌─────────────────────────────────────────────────────────────┐
│                    Domain Layer                             │
│  ┌─────────────────┐    ┌─────────────────┐               │
│  │ VoucherPack     │    │ Voucher         │               │
│  │ Repository      │    │ Repository      │               │
│  │ (Interface)     │    │ (Interface)     │               │
│  └─────────────────┘    └─────────────────┘               │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ Implements
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  Data Access Layer                          │
│  ┌─────────────────┐    ┌─────────────────┐               │
│  │ VoucherPack     │    │ Voucher         │               │
│  │ Repository      │    │ Repository      │               │
│  │ Adapter         │    │ Adapter         │               │
│  └─────────────────┘    └─────────────────┘               │
│                              │                             │
│                              │ Uses                        │
│                              ▼                             │
│  ┌─────────────────┐    ┌─────────────────┐               │
│  │ VoucherPack     │    │ Voucher         │               │
│  │ JPA Repository  │    │ JPA Repository  │               │
│  └─────────────────┘    └─────────────────┘               │
│                              │                             │
│                              │ Maps to                     │
│                              ▼                             │
│  ┌─────────────────┐    ┌─────────────────┐               │
│  │ VoucherPack     │    │ Voucher         │               │
│  │ JPA Entity      │    │ JPA Entity      │               │
│  └─────────────────┘    └─────────────────┘               │
└─────────────────────────────────────────────────────────────┘
```

## Module Structure

```
src/main/java/com/poly/promotion/data/access/
├── adapter/                    # Repository adapters implementing domain interfaces
│   ├── VoucherPackRepositoryAdapter.java
│   └── VoucherRepositoryAdapter.java
├── jpaentity/                 # JPA entity classes
│   ├── VoucherPackJpaEntity.java
│   └── VoucherJpaEntity.java
├── jparepository/             # Spring Data JPA repository interfaces
│   ├── VoucherPackJpaRepository.java
│   └── VoucherJpaRepository.java
└── transformer/               # MapStruct transformers for entity conversion
    ├── VoucherPackTransformer.java
    └── VoucherTransformer.java
```

## Key Components

### 1. JPA Entities (`jpaentity/`)

- **VoucherPackJpaEntity**: Maps to the `voucher_packs` table
- **VoucherJpaEntity**: Maps to the `vouchers` table

**Features:**
- Proper JPA annotations and constraints
- Audit fields (created_at, updated_at, created_by, updated_by)
- Optimistic locking with version field
- Indexes for performance optimization
- Check constraints for data integrity

### 2. JPA Repositories (`jparepository/`)

- **VoucherPackJpaRepository**: Extends `JpaRepository<VoucherPackJpaEntity, Long>`
- **VoucherJpaRepository**: Extends `JpaRepository<VoucherJpaEntity, String>`

**Features:**
- Custom query methods for business operations
- Bulk update operations for expiration management
- Performance-optimized queries with proper indexing
- Support for pagination and sorting

### 3. Transformers (`transformer/`)

- **VoucherPackTransformer**: Converts between domain and JPA entities
- **VoucherTransformer**: Converts between domain and JPA entities

**Features:**
- MapStruct-based automatic implementation generation
- Custom mappers for complex value objects
- Bidirectional transformation support
- Null-safe mapping with proper validation

### 4. Repository Adapters (`adapter/`)

- **VoucherPackRepositoryAdapter**: Implements `VoucherPackRepository`
- **VoucherRepositoryAdapter**: Implements `VoucherRepository`

**Features:**
- Implements domain repository interfaces
- Coordinates between JPA repositories and transformers
- Handles transaction management
- Provides business logic validation
- Ensures data consistency and integrity

## Database Schema

### Voucher Packs Table (`voucher_packs`)

| Column           | Type           | Constraints                    | Description                    |
|------------------|----------------|--------------------------------|--------------------------------|
| id               | BIGSERIAL      | PRIMARY KEY                    | Auto-generated identifier      |
| description      | VARCHAR(500)   | NOT NULL                      | Human-readable description     |
| discount_amount  | DECIMAL(10,2)  | NOT NULL, > 0                 | Discount value                 |
| valid_range      | VARCHAR(50)    | NOT NULL                      | Validity period (e.g., "30 DAYS") |
| required_points  | BIGINT         | NOT NULL, > 0                 | Loyalty points required        |
| quantity         | INTEGER        | NOT NULL, >= 0                | Available quantity             |
| valid_from       | DATE           | NULL                          | Start date for availability    |
| valid_to         | DATE           | NULL                          | End date for expiration        |
| status           | VARCHAR(20)    | NOT NULL, enum values         | Current lifecycle status       |
| created_at       | TIMESTAMP      | NOT NULL, DEFAULT NOW         | Creation timestamp             |
| created_by       | VARCHAR(100)   | NOT NULL                      | Creator identifier             |
| updated_at       | TIMESTAMP      | NULL, DEFAULT NOW             | Last modification timestamp     |
| updated_by       | VARCHAR(100)   | NULL                          | Last modifier identifier       |
| version          | BIGINT         | NOT NULL, DEFAULT 0           | Optimistic locking version     |

### Vouchers Table (`vouchers`)

| Column           | Type           | Constraints                    | Description                    |
|------------------|----------------|--------------------------------|--------------------------------|
| id               | VARCHAR(36)    | PRIMARY KEY                    | UUID identifier                |
| voucher_code     | VARCHAR(20)    | NOT NULL, UNIQUE              | Human-readable voucher code    |
| discount_amount  | DECIMAL(10,2)  | NOT NULL, > 0                 | Discount amount                |
| voucher_pack_id  | BIGINT         | NOT NULL, FOREIGN KEY         | Reference to voucher pack      |
| customer_id      | VARCHAR(100)   | NOT NULL                      | Customer identifier            |
| redeemed_at      | TIMESTAMP      | NOT NULL, DEFAULT NOW         | Redemption timestamp           |
| valid_to         | TIMESTAMP      | NOT NULL                      | Expiration timestamp           |
| status           | VARCHAR(20)    | NOT NULL, enum values         | Current status                 |
| used_at          | TIMESTAMP      | NULL                          | First usage timestamp          |
| created_at       | TIMESTAMP      | NOT NULL, DEFAULT NOW         | Creation timestamp             |
| updated_at       | TIMESTAMP      | NULL, DEFAULT NOW             | Last modification timestamp     |
| version          | BIGINT         | NOT NULL, DEFAULT 0           | Optimistic locking version     |

## Database Migration

The module uses **Liquibase** for database schema management:

### Migration Files

1. **V001__create_voucher_packs_table.yml**: Creates the voucher_packs table
2. **V002__create_vouchers_table.yml**: Creates the vouchers table
3. **V003__create_indexes_and_constraints.yml**: Creates indexes and constraints
4. **V004__insert_initial_data.yml**: Inserts sample data

### Key Features

- **Type-safe DDL**: Uses Liquibase YAML format for readable migrations
- **Incremental changes**: Each changeSet is tracked and versioned
- **Rollback support**: Changes can be rolled back if needed
- **Environment-specific**: Supports different database environments
- **Sample data**: Includes realistic test data for development

## Performance Optimizations

### Indexes

- **Primary keys**: Auto-indexed by PostgreSQL
- **Status fields**: For filtering by lifecycle state
- **Date fields**: For range queries and expiration checks
- **Composite indexes**: For common query patterns
- **Unique constraints**: For voucher codes and business rules

### Query Optimization

- **Custom queries**: Optimized for business operations
- **Bulk operations**: Efficient batch updates for expiration management
- **Lazy loading**: For entity relationships
- **Pagination support**: For large result sets

## Transaction Management

- **Read operations**: Marked with `@Transactional(readOnly = true)`
- **Write operations**: Use default transaction propagation
- **Bulk operations**: Optimized for performance
- **Error handling**: Proper rollback on exceptions

## Error Handling

- **Repository layer**: Catches and wraps database exceptions
- **Business validation**: Ensures data integrity before persistence
- **Logging**: Comprehensive logging for debugging and monitoring
- **User-friendly messages**: Clear error messages for business users

## Testing

### Test Dependencies

- **H2 Database**: In-memory database for unit tests
- **TestContainers**: PostgreSQL container for integration tests
- **Spring Boot Test**: Framework for testing Spring components

### Test Strategy

- **Unit tests**: Test individual components in isolation
- **Integration tests**: Test database interactions
- **Repository tests**: Verify CRUD operations
- **Transformer tests**: Validate entity conversions

## Configuration

### Dependencies

```xml
<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Liquibase -->
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>

<!-- MapStruct -->
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>${mapstruct.version}</version>
</dependency>
```

### MapStruct Configuration

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

## Usage Examples

### Creating a Voucher Pack

```java
@Service
public class VoucherPackService {
    
    @Autowired
    private VoucherPackRepository voucherPackRepository;
    
    public VoucherPack createVoucherPack(VoucherPackCreateRequest request) {
        VoucherPack voucherPack = request.toEntity();
        return voucherPackRepository.createVoucherPack(voucherPack);
    }
}
```

### Finding Available Voucher Packs

```java
@Service
public class VoucherPackService {
    
    public List<VoucherPack> getAvailableVoucherPacks() {
        return voucherPackRepository.findAvailableVoucherPacks();
    }
}
```

### Redeeming a Voucher

```java
@Service
public class VoucherService {
    
    @Autowired
    private VoucherRepository voucherRepository;
    
    public Voucher redeemVoucher(String voucherCode, String customerId) {
        Voucher voucher = voucherRepository.getVoucherByCode(voucherCode);
        // Business logic for redemption
        voucher = voucherRepository.updateVoucher(voucher);
        return voucher;
    }
}
```

## Best Practices

### 1. Entity Design

- Use appropriate JPA annotations
- Implement proper validation constraints
- Include audit fields for tracking
- Use optimistic locking for concurrency

### 2. Repository Design

- Keep repositories focused and cohesive
- Use custom queries for complex operations
- Implement bulk operations for performance
- Handle transactions appropriately

### 3. Transformation

- Use MapStruct for automatic mapping
- Implement custom mappers for complex logic
- Ensure null-safe transformations
- Validate mapping results

### 4. Performance

- Create appropriate indexes
- Use bulk operations when possible
- Implement pagination for large datasets
- Monitor query performance

## Monitoring and Maintenance

### Database Monitoring

- **Query performance**: Monitor slow queries
- **Index usage**: Ensure indexes are being used
- **Table statistics**: Regular maintenance and updates
- **Connection pooling**: Monitor connection usage

### Application Monitoring

- **Repository metrics**: Track operation performance
- **Error rates**: Monitor exception frequencies
- **Transaction metrics**: Track transaction success/failure
- **Response times**: Monitor operation latency

## Future Enhancements

### Planned Features

- **Caching layer**: Redis integration for frequently accessed data
- **Audit logging**: Comprehensive audit trail for all changes
- **Data archiving**: Automatic archiving of old data
- **Performance tuning**: Query optimization and index tuning

### Scalability Considerations

- **Read replicas**: For read-heavy operations
- **Sharding**: For large datasets
- **Caching strategies**: Multi-level caching
- **Async operations**: For non-critical operations

## Troubleshooting

### Common Issues

1. **Migration failures**: Check Liquibase logs and database constraints
2. **Performance issues**: Review query execution plans and indexes
3. **Transaction failures**: Check transaction boundaries and rollback scenarios
4. **Mapping errors**: Verify MapStruct configuration and entity relationships

### Debugging Tips

- Enable SQL logging in development
- Use database query analyzers
- Monitor application logs for errors
- Check database connection pool status

## Contributing

When contributing to this module:

1. **Follow the architecture**: Maintain hexagonal architecture principles
2. **Add tests**: Include unit and integration tests for new features
3. **Update documentation**: Keep this README and code comments current
4. **Follow conventions**: Use established naming and coding conventions
5. **Performance impact**: Consider the performance impact of changes

## License

This module is part of the Promotion Management system and follows the same licensing terms.

---

**Note**: This module is designed for production use in hotel management environments. Ensure proper security measures and testing before deployment in production environments.
