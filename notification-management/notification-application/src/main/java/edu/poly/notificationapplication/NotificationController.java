package edu.poly.notificationapplication;


import edu.poly.notificationapplicationservice.dto.CreateNotificationRequest;
import edu.poly.notificationapplicationservice.dto.NotificationResponse;
import edu.poly.notificationapplicationservice.exception.ApplicationNotificationException;
import edu.poly.notificationapplicationservice.service.NotificationAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "Quản lý thông báo hệ thống")
public class NotificationController {

    private final NotificationAppService appService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody CreateNotificationRequest request) throws ApplicationNotificationException {
        NotificationResponse response = appService.createNotification(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/user/{userId}")
    @Operation(summary = "Lấy danh sách thông báo theo người dùng")
    public ResponseEntity<Page<NotificationResponse>> getByUser(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(appService.getUserNotifications(userId, page, size));
    }
}