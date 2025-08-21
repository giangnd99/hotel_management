# Makefile for Staff Management Module Docker Operations

.PHONY: help build up down logs clean restart rebuild status health

# Default target
help:
	@echo "Staff Management Module Docker Commands:"
	@echo ""
	@echo "Development:"
	@echo "  make build     - Build Docker image"
	@echo "  make up        - Start development environment"
	@echo "  make down      - Stop all services"
	@echo "  make logs      - View application logs"
	@echo "  make restart   - Restart all services"
	@echo "  make rebuild   - Rebuild and start services"
	@echo ""
	@echo "Production:"
	@echo "  make up-prod   - Start production environment"
	@echo "  make down-prod - Stop production services"
	@echo "  make logs-prod - View production logs"
	@echo ""
	@echo "Utilities:"
	@echo "  make status    - Show service status"
	@echo "  make health    - Check service health"
	@echo "  make clean     - Remove containers, images, and volumes"
	@echo "  make shell     - Open shell in application container"
@echo "  make db-shell  - Open shell in database container"
@echo "  make db-reset  - Reset database schema via Liquibase"

# Development environment
build:
	docker-compose build

up:
	docker-compose up -d

down:
	docker-compose down

logs:
	docker-compose logs -f staff-app

restart:
	docker-compose restart

rebuild:
	docker-compose down
	docker-compose up -d --build

# Production environment
up-prod:
	docker-compose -f docker-compose.prod.yml up -d

down-prod:
	docker-compose -f docker-compose.prod.yml down

logs-prod:
	docker-compose -f docker-compose.prod.yml logs -f staff-app

# Production with Redis
up-prod-cache:
	docker-compose -f docker-compose.prod.yml --profile cache up -d

# Utilities
status:
	docker-compose ps

health:
	@echo "Checking service health..."
	@docker-compose ps --format "table {{.Name}}\t{{.Status}}\t{{.Ports}}"
	@echo ""
	@echo "Health check endpoints:"
	@echo "Application: http://localhost:8085/actuator/health"
	@echo "Database: docker-compose exec postgres pg_isready -U postgres"

clean:
	@echo "Stopping all services..."
	docker-compose down
	@echo "Removing containers, images, and volumes..."
	docker-compose down -v
	docker rmi $$(docker images -q staff-management) 2>/dev/null || true
	@echo "Cleanup completed!"

shell:
	docker-compose exec staff-app sh

db-shell:
	docker-compose exec postgres psql -U postgres -d hotel_management

# Database operations
db-backup:
	@echo "Creating database backup..."
	docker-compose exec postgres pg_dump -U postgres hotel_management > backup_$$(date +%Y%m%d_%H%M%S).sql
	@echo "Backup completed!"

db-restore:
	@echo "Restoring database from backup..."
	@read -p "Enter backup filename: " backup_file; \
	docker-compose exec -T postgres psql -U postgres -d hotel_management < $$backup_file
	@echo "Restore completed!"

db-reset:
	@echo "Resetting database schema via Liquibase..."
	@echo "This will drop and recreate all tables using Liquibase changelogs"
	@read -p "Are you sure? Type 'yes' to continue: " confirm; \
	if [ "$$confirm" = "yes" ]; then \
		docker-compose exec staff-app sh -c "cd /app && java -jar app.jar --spring.liquibase.drop-first=true"; \
		echo "Database reset completed!"; \
	else \
		echo "Database reset cancelled."; \
	fi

# Monitoring
monitor:
	@echo "Service Status:"
	@docker-compose ps
	@echo ""
	@echo "Resource Usage:"
	@docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}\t{{.BlockIO}}"

# Development tools
pgadmin:
	docker-compose --profile tools up -d pgadmin
	@echo "pgAdmin started at http://localhost:5050"
	@echo "Email: admin@staff.com"
	@echo "Password: admin123"

# Quick test
test:
	@echo "Running quick health check..."
	@curl -s http://localhost:8085/actuator/health | jq . || echo "Application not responding or jq not installed"

# Environment setup
env-setup:
	@echo "Setting up environment variables..."
	@if [ ! -f .env ]; then \
		echo "Creating .env file with default values..."; \
		echo "# Application" > .env; \
		echo "STAFF_MANAGEMENT_PORT=8085" >> .env; \
		echo "BUILD_VERSION=latest" >> .env; \
		echo "" >> .env; \
		echo "# Database" >> .env; \
		echo "POSTGRES_DB=hotel_management" >> .env; \
		echo "POSTGRES_USER=postgres" >> .env; \
		echo "POSTGRES_PASSWORD=postgres" >> .env; \
		echo "POSTGRES_PORT=5432" >> .env; \
		echo "" >> .env; \
		echo "# JVM" >> .env; \
		echo "JVM_XMS=1g" >> .env; \
		echo "JVM_XMX=2g" >> .env; \
		echo "" >> .env; \
		echo "# Logging" >> .env; \
		echo "LOG_LEVEL=INFO" >> .env; \
		echo "SWAGGER_ENABLED=false" >> .env; \
		echo "" >> .env; \
		echo "# Redis (optional)" >> .env; \
		echo "REDIS_PASSWORD=redis123" >> .env; \
		echo "REDIS_PORT=6379" >> .env; \
		echo ".env file created successfully!"; \
	else \
		echo ".env file already exists"; \
	fi

# Full development setup
dev-setup: env-setup build up
	@echo "Development environment setup completed!"
	@echo "Application: http://localhost:8085"
	@echo "Swagger UI: http://localhost:8085/swagger-ui.html"
	@echo "Database: localhost:5432"
	@echo ""
	@echo "Useful commands:"
	@echo "  make logs      - View logs"
	@echo "  make status    - Check status"
	@echo "  make health    - Health check"
	@echo "  make down      - Stop services"
