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
- **Docker & Docker Compose**
- **PostgreSQL 15+** (or use Docker)

### **Option 1: Docker Compose (Recommended)**

1. **Clone and Navigate**
   ```bash
   cd promotion-management
   ```

2. **Start Services**
   ```bash
   docker compose up -d
   ```
   This will start:
   - PostgreSQL database
   - Redis cache (optional)
   - Your application

3. **Verify Services**
   ```bash
   docker compose ps
   ```

4. **Check Application Health**
   ```bash
   curl http://localhost:8080/promotion-management/actuator/health
   ```

### **Option 2: Local Development**

1. **Setup Database**
   ```bash
   # Start PostgreSQL
   docker run -d --name postgres \
     -e POSTGRES_DB=promotiondb \
     -e POSTGRES_USER=promotion \
     -e POSTGRES_PASSWORD=promotion \
     -p 5432:5432 \
     postgres:15-alpine
   ```

2. **Build Project**
   ```bash
   mvn clean compile
   ```

3. **Run Application**
   ```bash
   mvn spring-boot:run -pl promotion-container
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
├── 🐳 Dockerfile                       # Multi-stage Docker build
├── 🐳 docker-compose.yml               # Service orchestration
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
| `DATABASE_URL` | `jdbc:postgresql://localhost:5432/promotiondb` | Database connection |
| `DATABASE_USERNAME` | `promotion` | Database username |
| `DATABASE_PASSWORD` | `promotion` | Database password |
| `REDIS_HOST` | `localhost` | Redis host (optional) |
| `REDIS_PORT` | `6379` | Redis port |

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

## 🐳 Docker

### **Building Image**
```bash
docker build -t promotion-management:latest .
```

### **Running with Docker Compose**
```bash
# Start all services
docker compose up -d

# View logs
docker compose logs -f app

# Stop services
docker compose down

# Rebuild and restart
docker compose up -d --build
```

### **Service Ports**
- **Application**: `8080`
- **PostgreSQL**: `5432`
- **Redis**: `6379`

## 🧪 Testing

### **Test Structure**
```
src/test/java/
├── unit/           # Unit tests
├── integration/    # Integration tests
└── e2e/           # End-to-end tests
```

### **Running Tests**
```bash
# All tests
mvn test

# Specific module
mvn test -pl promotion-domain-model

# Integration tests only
mvn verify -DskipUnitTests
```

## 🔍 Troubleshooting

### **Common Issues**

1. **Database Connection Failed**
   ```bash
   # Check PostgreSQL status
   docker compose ps postgres
   
   # Check logs
   docker compose logs postgres
   ```

2. **Port Already in Use**
   ```bash
   # Find process using port 8080
   lsof -i :8080
   
   # Kill process
   kill -9 <PID>
   ```

3. **Build Failures**
   ```bash
   # Clean and rebuild
   mvn clean compile
   
   # Check Java version
   java -version
   ```

### **Logs**
```bash
# Application logs
docker compose logs -f app

# Database logs
docker compose logs -f postgres

# All services
docker compose logs -f
```

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
