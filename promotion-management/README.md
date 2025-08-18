# Hotel Promotion Management System

## Overview

The Hotel Promotion Management System is a comprehensive solution for managing hotel promotions, voucher packs, and customer voucher operations. Built using Spring Boot with Clean Architecture and Domain-Driven Design principles, the system provides a robust foundation for hotel marketing and customer loyalty programs.

## Architecture

### Module Structure

```
promotion-management/
├── promotion-domain-model/          # Domain model and business logic
│   ├── promotion-domain-core/      # Core domain entities and value objects
│   └── promotion-domain-application/ # Application services and APIs
├── promotion-application/           # REST controllers and web layer
├── promotion-container/             # Configuration and main application
└── promotion-data-access/          # Data persistence layer
```

### Architecture Principles

- **Clean Architecture**: Separation of concerns with clear boundaries
- **Hexagonal Architecture**: Ports and adapters for external integrations
- **Domain-Driven Design**: Business logic centered around domain concepts
- **SOLID Principles**: Single responsibility, open/closed, Liskov substitution, interface segregation, dependency inversion

## Spring Boot Implementation

### Controllers

#### 1. VoucherPackController

**Purpose**: Manages voucher pack operations including CRUD operations and customer access.

**Endpoints**:
- `GET /api/v1/voucher-packs` - Retrieve available voucher packs for customers
- `POST /api/v1/voucher-packs` - Create new voucher packs (administrative)
- `PUT /api/v1/voucher-packs/{id}` - Update existing voucher packs (administrative)
- `DELETE /api/v1/voucher-packs/{id}` - Delete voucher packs (administrative)

**Features**:
- Comprehensive OpenAPI/Swagger documentation
- Input validation and error handling
- Business rule enforcement
- Audit logging and monitoring
- Role-based access control

**Usage Example**:
```bash
# Get available voucher packs
curl -X GET "http://localhost:8080/promotion-management/api/v1/voucher-packs"

# Create new voucher pack
curl -X POST "http://localhost:8080/promotion-management/api/v1/voucher-packs" \
  -H "Content-Type: application/json" \
  -d '{
    "description": "20% off room bookings",
    "discountAmount": "20.0",
    "validRange": "30 DAYS",
    "requiredPoints": 1000,
    "quantity": 100,
    "validFrom": "2025-01-01",
    "validTo": "2025-12-31"
  }'
```

#### 2. VoucherController

**Purpose**: Manages individual voucher operations including redemption and customer access.

**Endpoints**:
- `GET /api/v1/vouchers/customer/{customerId}` - Retrieve customer's vouchers
- `POST /api/v1/vouchers/redeem` - Redeem voucher from voucher pack

**Features**:
- Customer-specific voucher access
- Loyalty point validation during redemption
- Business rule enforcement
- Security and privacy protection
- Comprehensive error handling

**Usage Example**:
```bash
# Get customer vouchers
curl -X GET "http://localhost:8080/promotion-management/api/v1/vouchers/customer/CUST001"

# Redeem voucher
curl -X POST "http://localhost:8080/promotion-management/api/v1/vouchers/redeem" \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST001",
    "voucherPackId": 123
  }'
```

### Configuration

#### 1. PromotionManagementConfig

**Purpose**: Main configuration class for component scanning and Spring features.

**Features**:
- Component scanning across all modules
- Entity scanning for JPA operations
- Repository scanning for data access
- Scheduling and transaction management

#### 2. OpenApiConfig

**Purpose**: OpenAPI/Swagger documentation configuration.

**Features**:
- Comprehensive API documentation
- Multiple server environments
- Contact and license information
- Detailed endpoint descriptions

#### 3. Main Application Class

**Purpose**: Spring Boot application entry point.

**Features**:
- Component scanning configuration
- Entity and repository scanning
- Scheduling and transaction management
- Environment-specific configurations

### Configuration Files

#### application.yml

**Purpose**: Comprehensive configuration for all system aspects.

**Configuration Sections**:

1. **Server Configuration**
   - Port and context path
   - Compression settings
   - Servlet configuration

2. **Database Configuration**
   - PostgreSQL connection settings
   - Hikari connection pool
   - JPA and Hibernate settings

3. **Spring Configuration**
   - Application metadata
   - Jackson serialization
   - Validation settings
   - Actuator endpoints

4. **Business Logic Configuration**
   - Voucher pack limits
   - Voucher constraints
   - Expiration management
   - Rate limiting

5. **Security Configuration**
   - CORS settings
   - Rate limiting
   - Authentication requirements

6. **Monitoring Configuration**
   - Metrics export
   - Prometheus integration
   - Health checks

7. **Environment Profiles**
   - Development (dev)
   - Production (prod)
   - Testing (test)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher
- Redis (optional, for caching)

### Environment Variables

```bash
# Database Configuration
export DATABASE_URL=jdbc:postgresql://localhost:5432/hotel_management
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=your_password

# Application Configuration
export PROMOTION_MANAGEMENT_PORT=8080
export SPRING_PROFILES_ACTIVE=dev

# Business Logic Configuration
export MAX_VOUCHER_PACK_QUANTITY=10000
export MIN_REQUIRED_POINTS=100
export MAX_REQUIRED_POINTS=100000

# Logging Configuration
export LOG_LEVEL=INFO
export LOG_FILE_PATH=logs/promotion-management.log
```

### Running the Application

1. **Clone and Build**:
   ```bash
   git clone <repository-url>
   cd hotel_management/promotion-management
   mvn clean install
   ```

2. **Start the Application**:
   ```bash
   cd promotion-container
   mvn spring-boot:run
   ```

3. **Access the Application**:
   - Application: http://localhost:8080/promotion-management
   - Swagger UI: http://localhost:8080/promotion-management/swagger-ui.html
   - API Docs: http://localhost:8080/promotion-management/api-docs

### Database Setup

1. **Create Database**:
   ```sql
   CREATE DATABASE hotel_management;
   ```

2. **Run Migrations** (if using Flyway or similar):
   ```bash
   mvn flyway:migrate
   ```

3. **Verify Connection**:
   - Check application logs for successful database connection
   - Verify JPA entity scanning

## API Documentation

### OpenAPI/Swagger

The system provides comprehensive API documentation through OpenAPI/Swagger:

- **Interactive Documentation**: Swagger UI for testing endpoints
- **API Specification**: OpenAPI JSON for integration
- **Request/Response Examples**: Detailed examples for all endpoints
- **Error Codes**: Comprehensive error response documentation

### API Categories

1. **Voucher Pack Management**
   - Create, read, update, delete operations
   - Status management and validation
   - Business rule enforcement

2. **Voucher Operations**
   - Customer voucher redemption
   - Voucher status management
   - Expiration handling

3. **Customer Operations**
   - Customer voucher portfolio
   - Redemption history
   - Loyalty point integration

4. **Administrative Functions**
   - System monitoring
   - Audit trail access
   - Configuration management

## Security Features

### Authentication & Authorization

- Customer authentication required for voucher operations
- Role-based access control for administrative functions
- Secure token-based authentication (configurable)

### Data Protection

- Customer data isolation
- Input validation and sanitization
- SQL injection prevention
- XSS protection

### Rate Limiting

- Configurable rate limits per endpoint
- Per-customer and per-IP limiting
- Burst protection and throttling

## Monitoring & Observability

### Health Checks

- Application health status
- Database connectivity
- External service dependencies
- Custom business health indicators

### Metrics

- Prometheus metrics export
- Custom business metrics
- Performance monitoring
- Resource utilization tracking

### Logging

- Structured logging with JSON format
- Log level configuration
- File rotation and retention
- Audit trail logging

## Development Guidelines

### Code Quality

- Comprehensive Java documentation (Javadoc)
- Unit test coverage
- Integration test coverage
- Code style consistency

### Architecture Compliance

- Clean Architecture principles
- Domain-Driven Design patterns
- SOLID principles adherence
- Dependency injection usage

### Testing Strategy

- Unit tests for business logic
- Integration tests for APIs
- End-to-end tests for workflows
- Performance and load testing

## Deployment

### Containerization

- Docker support for containerized deployment
- Multi-stage builds for optimization
- Environment-specific configurations
- Health check integration

### Cloud Deployment

- Kubernetes deployment manifests
- Helm charts for package management
- Cloud-native configuration
- Auto-scaling capabilities

### CI/CD Integration

- Maven build automation
- Automated testing
- Deployment pipelines
- Environment promotion

## Troubleshooting

### Common Issues

1. **Database Connection Failures**
   - Verify database credentials
   - Check network connectivity
   - Validate database schema

2. **Component Scanning Issues**
   - Verify package structure
   - Check component annotations
   - Validate dependency injection

3. **Configuration Problems**
   - Validate YAML syntax
   - Check environment variables
   - Verify profile activation

### Debug Mode

Enable debug logging for troubleshooting:

```yaml
logging:
  level:
    com.poly.promotion: DEBUG
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
```

## Contributing

### Development Setup

1. Fork the repository
2. Create a feature branch
3. Implement changes with tests
4. Submit a pull request

### Code Standards

- Follow existing code style
- Add comprehensive documentation
- Include unit tests
- Update relevant documentation

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:

- **Email**: dev@hotelmanagement.com
- **Documentation**: https://hotelmanagement.com/developers
- **Issues**: GitHub Issues repository

## Version History

- **v1.0.0** - Initial release with core functionality
- **v1.1.0** - Enhanced expiration management
- **v1.2.0** - Improved security and monitoring
- **v1.3.0** - Performance optimizations and caching

---

**Note**: This system is designed for production use in hotel management environments. Ensure proper security measures and testing before deployment in production environments.
