# 🎫 Hotel Promotion Management System

A comprehensive voucher and promotion management service built with **Spring Boot**, **Clean Architecture**, **Domain-Driven Design (DDD)**, and **Hexagonal Architecture**. This service manages voucher packs, individual vouchers, and customer redemption using loyalty points.

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                    Promotion Management                        │
├─────────────────────────────────────────────────────────────────┤
│  🚀 Application Layer (REST Controllers, DTOs, AOP)           │
│  🔧 Domain Layer (Entities, Services, Business Logic)         │
│  💾 Data Access Layer (JPA, Liquibase, PostgreSQL)           │
│  🐳 Container Layer (Spring Boot, Configuration)              │
└─────────────────────────────────────────────────────────────────┘
```

### **Core Principles**
- **Clean Architecture**: Separation of concerns with clear dependency flow
- **Domain-Driven Design**: Business logic centered around domain entities
- **Hexagonal Architecture**: Adapters for external systems and databases
- **SOLID Principles**: Single responsibility, open/closed, dependency inversion

## 🚀 Quick Start

### **Prerequisites**
- **Java 17** or higher
- **Maven 3.9+**
- **PostgreSQL 15+** (via centralized infrastructure)

### **Setup with Centralized Infrastructure**

1. **Start Centralized Services**
   ```bash
   cd infrastructure/docker
   docker compose up -d
   ```
   This will start:
   - PostgreSQL database (pgvector) on port 5433
   - Redis cache on port 6379
   - Kafka cluster and related services
   - Other infrastructure services

2. **Verify Services**
   ```bash
   docker compose ps
   ```

3. **Check Database Connection**
   ```bash
   # Test PostgreSQL connection
   psql -h localhost -p 5433 -U postgres -d postgres
   ```

### **Local Development**

1. **Build Project**
   ```bash
   mvn clean compile
   ```

2. **Run Application**
   ```bash
   mvn spring-boot:run -pl promotion-container
   ```

3. **Check Application Health**
   ```bash
   curl http://localhost:8080/promotion-management/actuator/health
   ```

## 📁 Project Structure

```
promotion-management/
├── 📦 promotion-domain-model/          # Domain entities & business logic
│   ├── 🏗️ promotion-domain-core/      # Core domain entities
│   └── 🔧 promotion-domain-application/ # Domain services & use cases
├── 🚀 promotion-application/           # REST controllers & DTOs
├── 💾 promotion-data-access/           # Database & external integrations
├── 🐳 promotion-container/             # Spring Boot configuration
└── 📚 README.md                        # This file
```

## 🎯 Core Features

### **Voucher Pack Management**
- Create, read, update, delete voucher packs
- Configure loyalty point requirements
- Set validity periods and quantities
- Manage pack availability

### **Voucher Management**
- Customer voucher redemption
- Loyalty point deduction
- Voucher status tracking
- Expiration management

### **Business Operations**
- Batch processing for expired vouchers
- Monitoring and health checks
- Comprehensive logging and auditing
- Rate limiting and security

## 🔧 Configuration

### **Environment Variables**

| Variable | Default | Description |
|----------|---------|-------------|
| `PROMOTION_MANAGEMENT_PORT` | `8080` | Application port |
| `DATABASE_URL` | `jdbc:postgresql://localhost:5433/postgres?currentSchema=promotion_management` | Centralized database connection |
| `DATABASE_USERNAME` | `postgres` | Centralized database username |
| `DATABASE_PASSWORD` | `admin` | Centralized database password |
| `REDIS_HOST` | `localhost` | Redis host (centralized infrastructure) |
| `REDIS_PORT` | `6379` | Redis port (centralized infrastructure) |

### **Application Properties**

Key configurations in `application.yml`:
- **Database**: PostgreSQL with Hikari connection pool
- **Liquibase**: Database migration management
- **Actuator**: Health checks and metrics
- **Caching**: Redis (optional) or in-memory
- **Security**: Rate limiting and CORS

## 🌐 API Endpoints

### **Base URL**: `http://localhost:8080/promotion-management`

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Root with HATEOAS links |
| `/api` | GET | API discovery endpoint |
| `/api/v1/voucher-packs` | GET | List available voucher packs |
| `/api/v1/vouchers` | GET | Get customer vouchers |
| `/api/v1/vouchers/redeem` | POST | Redeem voucher from pack |
| `/actuator/health` | GET | Service health status |
| `/swagger-ui.html` | GET | API documentation |

### **API Documentation**
- **Swagger UI**: `/swagger-ui.html`
- **OpenAPI Spec**: `/api-docs`
- **HATEOAS**: All responses include navigation links

## 🗄️ Database Schema

### **Core Tables**
- `voucher_packs`: Voucher pack definitions
- `vouchers`: Individual vouchers
- `batch_job_execution`: Spring Batch job tracking
- `monitoring_data`: Local monitoring persistence

### **Database Migration**
- **Liquibase**: Type-safe DDL scripts
- **Location**: `promotion-data-access/src/main/resources/db/migration/`
- **Auto-execution**: On application startup

## 🔄 Batch Processing

### **Scheduled Jobs**
- **Voucher Expiration**: Daily at 12 AM
- **Monitoring Sync**: Every 6 hours
- **Data Cleanup**: Weekly cleanup of old monitoring data

### **Job Configuration**
```yaml
batch:
  job:
    enabled: true
    name: promotion-management-batch
  jdbc:
    initialize-schema: always
```

## 📊 Monitoring & Health

### **Spring Actuator Endpoints**
- `/actuator/health` - Service health
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics
- `/actuator/info` - Application information

### **Custom Health Checks**
- Database connectivity
- Redis connectivity (if enabled)
- External monitoring service status

## 🚀 Development

### **Building the Project**
```bash
# Clean build
mvn clean compile

# Run tests
mvn test

# Package
mvn package -DskipTests

# Run specific module
mvn spring-boot:run -pl promotion-container
```

### **Module Dependencies**
```
promotion-container
    ↓
promotion-application
    ↓
promotion-domain-model
    ↓
promotion-data-access
```

### **Code Quality**
- **Lombok**: Reduces boilerplate code
- **MapStruct**: Type-safe object mapping
- **JUnit 5**: Unit testing framework
- **Mockito**: Mocking framework

## 📚 Additional Resources

### **Architecture Patterns**
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)

### **Spring Boot**
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Batch](https://docs.spring.io/spring-batch/docs/current/reference/html/)

### **Tools & Libraries**
- [Liquibase](https://docs.liquibase.com/)
- [MapStruct](https://mapstruct.org/documentation/stable/reference/html/)
- [Lombok](https://projectlombok.org/features/all)

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**
3. **Make your changes**
4. **Add tests**
5. **Submit a pull request**

### **Development Guidelines**
- Follow Clean Architecture principles
- Write comprehensive tests
- Use meaningful commit messages
- Update documentation as needed

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For questions or issues:
1. Check the troubleshooting section
2. Review the logs
3. Create an issue with detailed information
4. Contact the development team

---

**Happy Coding! 🎉**

*Built with ❤️ using Spring Boot, Clean Architecture, and Domain-Driven Design*
