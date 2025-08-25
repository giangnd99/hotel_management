# Staff Management Module

A simple 3-layer architecture module for managing hotel staff with basic CRUD operations.

## Architecture

This module follows a simple 3-layer architecture:

- **Controller Layer**: REST API endpoints for staff management
- **Service Layer**: Business logic and data processing
- **Repository Layer**: Data access and persistence

## Features

### Core Functionality
- ✅ Create new staff members
- ✅ Retrieve staff information
- ✅ Update staff details
- ✅ Delete staff members
- ✅ Search staff by name or email
- ✅ Filter staff by department
- ✅ Filter staff by status
- ✅ Validation and error handling

### Staff Attributes
- **staffId**: Unique identifier (3-10 characters, uppercase letters and numbers)
- **userId**: Reference to user entity in authentication service (UUID, unique)
- **name**: Staff member's full name
- **email**: Unique email address
- **phone**: Phone number (10-15 digits, can include +, -, (), and spaces)
- **department**: Department assignment
- **status**: Current status (ACTIVE, INACTIVE, ON_LEAVE, TERMINATED)
- **createdAt**: Timestamp when record was created
- **updatedAt**: Timestamp when record was last updated

## API Endpoints

### CRUD Operations
- `GET /api/staffs` - Get all staff members
- `GET /api/staffs/{staffId}` - Get staff by ID
- `POST /api/staffs` - Create new staff member
- `PUT /api/staffs/{staffId}` - Update staff information
- `DELETE /api/staffs/{staffId}` - Delete staff member

### Search & Filter
- `GET /api/staffs/search?name={name}&email={email}` - Search staff by name or email
- `GET /api/staffs/department/{department}` - Get staff by department
- `GET /api/staffs/status/{status}` - Get staff by status

### Utility
- `GET /api/staffs/exists/{staffId}` - Check if staff ID exists
- `GET /api/staffs/email-exists?email={email}` - Check if email exists
- `GET /api/staffs/user-exists?userId={userId}` - Check if user ID exists

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL database

### Configuration

#### Environment Variables

##### Application Configuration
- **`STAFF_MANAGEMENT_PORT`**: Application port (default: 8085)

##### Database Configuration (Production)
- **`STAFF_DATASOURCE_URL`**: Database connection URL (default: `jdbc:postgresql://localhost:5432/hotel_management`)
- **`STAFF_DATASOURCE_USERNAME`**: Database username (default: `postgres`)
- **`STAFF_DATASOURCE_PASSWORD`**: Database password (default: `postgres`)

##### Database Configuration (Testing)
- **`STAFF_TEST_DATASOURCE_URL`**: Test database URL (default: `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`)
- **`STAFF_TEST_DATASOURCE_USERNAME`**: Test database username (default: `sa`)
- **`STAFF_TEST_DATASOURCE_PASSWORD`**: Test database password (default: empty)

#### Example Environment Setup
```bash
# Application port
export STAFF_MANAGEMENT_PORT=8086

# Production database
export STAFF_DATASOURCE_URL=jdbc:postgresql://localhost:5432/staff_db
export STAFF_DATASOURCE_USERNAME=staff_user
export STAFF_DATASOURCE_PASSWORD=secure_password

# Test database (optional, uses defaults)
export STAFF_TEST_DATASOURCE_URL=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
export STAFF_TEST_DATASOURCE_USERNAME=test_user
export STAFF_TEST_DATASOURCE_PASSWORD=test_pass
```

### Running the Application
```bash
# Navigate to the module directory
cd staff-management

# Build the project
mvn clean install

# Run with default configuration
mvn spring-boot:run

# Run with custom port
STAFF_MANAGEMENT_PORT=8086 mvn spring-boot:run

# Run with custom database configuration
STAFF_DATASOURCE_URL=jdbc:postgresql://localhost:5432/staff_db \
STAFF_DATASOURCE_USERNAME=staff_user \
STAFF_DATASOURCE_PASSWORD=secure_password \
mvn spring-boot:run
```

The application will start on the configured port (default: 8085) and connect to the configured database.

### API Documentation
Once running, access Swagger UI at:
- http://localhost:8085/swagger-ui.html (or your configured port)

## Database Schema

The module automatically creates and manages the database schema using Liquibase changelogs. No manual SQL scripts are needed.

```sql
CREATE TABLE staffs (
    staff_id VARCHAR(10) PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    department VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_staffs_user_id ON staffs(user_id);
CREATE INDEX idx_staffs_email ON staffs(email);
CREATE INDEX idx_staffs_department ON staffs(department);
CREATE INDEX idx_staffs_status ON staffs(status);
```

## Integration with Authentication Service

The staff management module integrates with the authentication service through the `userId` field:

- **userId**: References the `User` entity in the authentication service
- **Type**: UUID (matches the `UserId` value object in the common domain)
- **Constraint**: Unique across all staff members
- **Validation**: Ensures no duplicate user associations

This integration allows:
- Linking staff records to authenticated users
- Maintaining referential integrity between services
- Supporting user authentication and authorization workflows

## Example Usage

### Create a Staff Member
```bash
curl -X POST http://localhost:8085/api/staffs \
  -H "Content-Type: application/json" \
  -d '{
    "staffId": "S001",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "email": "john.doe@hotel.com",
    "phone": "1234567890",
    "department": "Front Desk"
  }'
```

### Get All Staff
```bash
curl http://localhost:8085/api/staffs
```

### Update Staff
```bash
curl -X PUT http://localhost:8085/api/staffs/S001 \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "550e8400-e29b-41d4-a716-446655440001",
    "department": "Housekeeping",
    "status": "ACTIVE"
  }'
```

### Check if User ID Exists
```bash
curl "http://localhost:8085/api/staffs/user-exists?userId=550e8400-e29b-41d4-a716-446655440000"
```

## Testing

Run the tests with:
```bash
mvn test
```

## Dependencies

- Spring Boot Web
- Spring Boot Data JPA
- Spring Boot Validation
- Liquibase (Database migration)
- PostgreSQL Driver
- Lombok
- Swagger OpenAPI

## Notes

- This module is designed to be simple and focused on essential CRUD operations
- It follows a 3-layer architecture for simplicity and maintainability
- The module integrates with the authentication service through the userId field
- All timestamps are automatically managed by JPA
- Input validation ensures data integrity
- Comprehensive error handling provides clear feedback
- **Database schema is managed entirely through Liquibase changelogs** - no manual SQL scripts needed
- Port configuration is flexible through environment variables to avoid conflicts
- Database configuration is flexible through environment variables for different environments
- Test configuration uses separate environment variables for isolated testing

## Liquibase Database Management

The module uses Liquibase for all database schema operations:

- **Automatic Schema Creation**: Tables, indexes, and constraints are created automatically on startup
- **Version Control**: All database changes are tracked and versioned
- **Consistent Deployments**: Ensures database schema consistency across environments
- **Rollback Support**: Can rollback database changes if needed

### Liquibase Commands (via Docker)

```bash
# Reset database schema (development only)
make db-reset

# View changelog status
docker-compose exec staff-app sh -c "cd /app && java -jar app.jar --spring.liquibase.status"

# Force update
docker-compose exec staff-app sh -c "cd /app && java -jar app.jar --spring.liquibase.update"
```
