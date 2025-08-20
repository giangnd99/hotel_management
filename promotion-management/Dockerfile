# -----------------------------------------------------------------------------
# Multi-stage Dockerfile for Promotion Management Service
# - Stage 1: Build the Spring Boot application (promotion-container)
# - Stage 2: Run the built JAR on a slim JRE image
# -----------------------------------------------------------------------------

# ===== Stage 1: Build =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /workspace

# Copy module sources (this directory contains all child modules)
COPY . ./

# Build only the runnable container module and its dependencies
RUN mvn -q -DskipTests -pl promotion-container -am package


# ===== Stage 2: Runtime =====
FROM eclipse-temurin:17-jre-jammy AS runtime

ENV JAVA_OPTS="-Xms256m -Xmx512m" \
    SPRING_PROFILES_ACTIVE=prod \
    SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/promotiondb \
    SPRING_DATASOURCE_USERNAME=promotion \
    SPRING_DATASOURCE_PASSWORD=promotion \
    MONITORING_SERVICE_URL=http://monitoring:8081 \
    SERVER_PORT=8080

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /workspace/promotion-container/target/*.jar /app/app.jar

EXPOSE 8080

# Default entrypoint with overridable JAVA_OPTS
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


