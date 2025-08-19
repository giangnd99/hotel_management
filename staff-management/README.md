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

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL database

### Configuration
1. Update `application.yml` with your database credentials
2. Ensure PostgreSQL is running and accessible
3. The database will be created automatically on first run

### Running the Application
```bash
# Navigate to the module directory
cd staff-management

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on port 8082.

### API Documentation
Once running, access Swagger UI at:
- http://localhost:8082/swagger-ui.html

## Database Schema

The module automatically creates the following table:

```sql
CREATE TABLE staffs (
    staff_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    department VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## Example Usage

### Create a Staff Member
```bash
curl -X POST http://localhost:8082/api/staffs \
  -H "Content-Type: application/json" \
  -d '{
    "staffId": "S001",
    "name": "John Doe",
    "email": "john.doe@hotel.com",
    "phone": "1234567890",
    "department": "Front Desk"
  }'
```

### Get All Staff
```bash
curl http://localhost:8082/api/staffs
```

### Update Staff
```bash
curl -X PUT http://localhost:8082/api/staffs/S001 \
  -H "Content-Type: application/json" \
  -d '{
    "department": "Housekeeping",
    "status": "ACTIVE"
  }'
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
- PostgreSQL Driver
- Lombok
- Swagger OpenAPI

## Notes

- This module is designed to be simple and focused on essential CRUD operations
- It follows a 3-layer architecture for simplicity and maintainability
- The module integrates with other hotel management modules through the staffId reference
- All timestamps are automatically managed by JPA
- Input validation ensures data integrity
- Comprehensive error handling provides clear feedback
