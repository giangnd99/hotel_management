@echo off
echo Starting Swagger Hub...
echo.
echo Make sure you have the following services running:
echo - Eureka Server (Port 8761)
echo - Inventory Service (Port 8081)
echo - Restaurant Service (Port 8082)
echo - Booking Management (Port 8083)
echo - Room Management (Port 8087)
echo - Spring AI Management (Port 8088)
echo - Staff Management (Port 8089)
echo - Service Management (Port 8090)
echo - Notification Management (Port 8091)
echo - Payment Management (Port 8092)
echo - Customer Management (Port 8093)
echo - Reporting Management (Port 8094)
echo.
echo Swagger Hub will be available at: http://localhost:8080/swagger-ui.html
echo.
pause

mvn spring-boot:run 