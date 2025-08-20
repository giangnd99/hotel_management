# Docker Setup for Staff Management Module

This document provides comprehensive instructions for running the Staff Management module using Docker and Docker Compose.

## 🐳 **Docker Files Overview**

| File | Purpose | Description |
|------|---------|-------------|
| `Dockerfile` | Application container | Multi-stage build for the Spring Boot application |
| `docker-compose.yml` | Development setup | Basic setup with PostgreSQL and pgAdmin |
| `docker-compose.prod.yml` | Production setup | Production-ready with security and performance optimizations |
| `.dockerignore` | Build optimization | Excludes unnecessary files from Docker build context |

**Note**: Database schema management is handled entirely by Liquibase changelogs - no additional SQL initialization scripts are needed.

## 🚀 **Quick Start**

### **Development Environment**

1. **Build and start all services:**
   ```bash
   docker-compose up -d
   ```

2. **View logs:**
   ```bash
   docker-compose logs -f staff-app
   ```

3. **Stop services:**
   ```bash
   docker-compose down
   ```

### **Production Environment**

1. **Set environment variables:**
   ```bash
   export POSTGRES_PASSWORD=secure_password
   export STAFF_MANAGEMENT_PORT=8085
   export LOG_LEVEL=INFO
   export SWAGGER_ENABLED=false
   ```

2. **Start production services:**
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

3. **Start with Redis caching:**
   ```bash
   docker-compose -f docker-compose.prod.yml --profile cache up -d
   ```

## 🔧 **Configuration**

### **Environment Variables**

#### **Application Configuration**
| Variable | Default | Description |
|----------|---------|-------------|
| `STAFF_MANAGEMENT_PORT` | `8085` | Application port |
| `BUILD_VERSION` | `latest` | Docker image version |

#### **Database Configuration**
| Variable | Default | Description |
|----------|---------|-------------|
| `POSTGRES_DB` | `hotel_management` | Database name |
| `POSTGRES_USER` | `postgres` | Database username |
| `POSTGRES_PASSWORD` | `postgres` | Database password |
| `POSTGRES_PORT` | `5432` | Database port |

#### **JVM Configuration**
| Variable | Default | Description |
|----------|---------|-------------|
| `JVM_XMS` | `1g` | Initial heap size |
| `JVM_XMX` | `2g` | Maximum heap size |

#### **Logging Configuration**
| Variable | Default | Description |
|----------|---------|-------------|
| `LOG_LEVEL` | `INFO` | Application log level |
| `SWAGGER_ENABLED` | `false` | Enable Swagger UI in production |

#### **Redis Configuration (Optional)**
| Variable | Default | Description |
|----------|---------|-------------|
| `REDIS_PASSWORD` | `redis123` | Redis authentication password |
| `REDIS_PORT` | `6379` | Redis port |

### **Example Environment File (.env)**

Create a `.env` file in the project root:

```bash
# Application
STAFF_MANAGEMENT_PORT=8085
BUILD_VERSION=1.0.0

# Database
POSTGRES_DB=staff_management
POSTGRES_USER=staff_user
POSTGRES_PASSWORD=secure_password123
POSTGRES_PORT=5432

# JVM
JVM_XMS=1g
JVM_XMX=2g

# Logging
LOG_LEVEL=INFO
SWAGGER_ENABLED=false

# Redis (optional)
REDIS_PASSWORD=redis_secure_pass
REDIS_PORT=6379
```

## 🏗️ **Building and Running**

### **Build Docker Image**

```bash
# Build the application image
docker build -t staff-management:latest .

# Build with custom tag
docker build -t staff-management:v1.0.0 .
```

### **Run Individual Containers**

```bash
# Run PostgreSQL only
docker run -d \
  --name staff-postgres \
  -e POSTGRES_DB=hotel_management \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15-alpine

# Run application with custom database
docker run -d \
  --name staff-app \
  -e STAFF_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/hotel_management \
  -e STAFF_DATASOURCE_USERNAME=postgres \
  -e STAFF_DATASOURCE_PASSWORD=postgres \
  -p 8085:8085 \
  staff-management:latest
```

### **Docker Compose Commands**

```bash
# Start services
docker-compose up -d

# Start with specific profile
docker-compose --profile tools up -d

# View running services
docker-compose ps

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f staff-app

# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v

# Rebuild and start
docker-compose up -d --build

# Scale application (if needed)
docker-compose up -d --scale staff-app=2
```

## 📊 **Service Endpoints**

### **Application Endpoints**
- **Main Application**: http://localhost:8085
- **API Documentation**: http://localhost:8085/swagger-ui.html
- **Health Check**: http://localhost:8085/actuator/health
- **Metrics**: http://localhost:8085/actuator/metrics
- **Prometheus**: http://localhost:8085/actuator/prometheus

### **Database Management**
- **PostgreSQL**: localhost:5432
- **pgAdmin**: http://localhost:5050 (admin@staff.com / admin123)
- **Schema Management**: Automatically handled by Liquibase changelogs

#### **Liquibase Database Operations**
```bash
# Reset database schema (drops and recreates all tables)
make db-reset

# View Liquibase changelog status
docker-compose exec staff-app sh -c "cd /app && java -jar app.jar --spring.liquibase.status"

# Force Liquibase update
docker-compose exec staff-app sh -c "cd /app && java -jar app.jar --spring.liquibase.update"
```

### **Optional Services**
- **Redis**: localhost:6379 (if using cache profile)

## 🔍 **Monitoring and Debugging**

### **Container Health Checks**

```bash
# Check container health
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

# View health check logs
docker inspect staff-management-app-prod | grep -A 10 Health
```

### **Application Logs**

```bash
# View application logs
docker-compose logs -f staff-app

# View database logs
docker-compose logs -f postgres

# View logs with timestamps
docker-compose logs -f --timestamps staff-app
```

### **Database Connection**

```bash
# Connect to PostgreSQL
docker exec -it staff-postgres psql -U postgres -d hotel_management

# View database tables
\dt

# View schemas
\dn
```

## 🛠️ **Troubleshooting**

### **Common Issues**

#### **Port Already in Use**
```bash
# Check what's using the port
sudo lsof -i :8085

# Kill the process
sudo kill -9 <PID>
```

#### **Database Connection Issues**
```bash
# Check if PostgreSQL is running
docker-compose ps postgres

# Restart PostgreSQL
docker-compose restart postgres

# Check database logs
docker-compose logs postgres
```

#### **Application Won't Start**
```bash
# Check application logs
docker-compose logs staff-app

# Check if database is ready
docker-compose exec postgres pg_isready -U postgres

# Restart application
docker-compose restart staff-app
```

### **Reset Everything**

```bash
# Stop all services
docker-compose down

# Remove all containers and volumes
docker-compose down -v

# Remove all images
docker rmi $(docker images -q staff-management)

# Start fresh
docker-compose up -d --build
```

## 🔒 **Security Considerations**

### **Production Security**

1. **Change default passwords** for all services
2. **Use secrets management** for sensitive data
3. **Enable SSL/TLS** for database connections
4. **Restrict network access** using Docker networks
5. **Regular security updates** for base images

### **Network Security**

```bash
# Create custom network with specific subnet
docker network create --subnet=172.20.0.0/16 --gateway=172.20.0.1 staff-network

# Use custom network in docker-compose
networks:
  staff-network:
    external: true
```

## 📈 **Performance Tuning**

### **JVM Optimization**

```bash
# Custom JVM options
JAVA_OPTS="-Xms2g -Xmx4g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"
```

### **Database Optimization**

```bash
# PostgreSQL performance settings
POSTGRES_INITDB_ARGS="--encoding=UTF-8 --lc-collate=C --lc-ctype=C --data-checksums"
```

### **Resource Limits**

```yaml
deploy:
  resources:
    limits:
      memory: 4G
      cpus: '4.0'
    reservations:
      memory: 2G
      cpus: '2.0'
```

## 🚀 **Deployment Examples**

### **Single Server Deployment**

```bash
# Clone repository
git clone <repository-url>
cd staff-management

# Set environment variables
export POSTGRES_PASSWORD=production_password
export STAFF_MANAGEMENT_PORT=8085

# Start production services
docker-compose -f docker-compose.prod.yml up -d
```

### **Multi-Environment Deployment**

```bash
# Development
docker-compose -f docker-compose.yml up -d

# Staging
docker-compose -f docker-compose.prod.yml -f docker-compose.staging.yml up -d

# Production
docker-compose -f docker-compose.prod.yml -f docker-compose.production.yml up -d
```

## 📚 **Additional Resources**

- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [PostgreSQL Docker Image](https://hub.docker.com/_/postgres)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)

## 🤝 **Support**

For issues or questions:
1. Check the troubleshooting section above
2. Review application logs: `docker-compose logs staff-app`
3. Check container health: `docker-compose ps`
4. Verify environment variables are set correctly
5. Ensure all required ports are available
