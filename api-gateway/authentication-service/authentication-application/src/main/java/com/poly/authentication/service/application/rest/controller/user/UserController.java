package com.poly.authentication.service.application.rest.controller.user;

import com.poly.authentication.service.domain.dto.ApiResponse;
import com.poly.authentication.service.domain.dto.reponse.user.UserResponse;
import com.poly.authentication.service.domain.dto.request.user.UserCreationRequest;
import com.poly.authentication.service.domain.dto.request.user.UserUpdatedRequest;
import com.poly.authentication.service.domain.port.in.service.ForgotPasswordService;
import com.poly.authentication.service.domain.port.in.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ForgotPasswordService forgotPasswordService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder().
                result(userService.createUser(request)).
                build();
    }

    @GetMapping
    Page<UserResponse> getAllUsers(@RequestParam int page) {
        return userService.getUsers(page);
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder().
                result(userService.getMyInfo()).
                build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder().
                result(userService.getUserById(UUID.fromString(userId))).
                build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @Valid @RequestBody UserUpdatedRequest request) {
        return ApiResponse.<UserResponse>builder().
                result(userService.updateUser(UUID.fromString(userId), request)).
                build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(UUID.fromString(userId));
        return ApiResponse.<String>builder().
                result("User deleted").
                build();
    }

    @PostMapping("/change/password/{email}")
    Boolean changePassword(@PathVariable String email, @RequestBody String password) {
        try {
            userService.updatePassword(email, password);
            log.info("Password updated successfully for user: {}", email);
            return true;
        } catch (Exception e) {
            log.error("Password update failed for user: {}", email, e);
            return false;
        }
    }

    @PostMapping("/forgot/password/{email}")
    String forgotPassword(@PathVariable String email) {
        return forgotPasswordService.createForgotPasswordToken(email);
    }

    @PostMapping("/forgot/password/valid/{token}")
    Boolean validToken(@PathVariable String token) {
        return forgotPasswordService.validateToken(token);
    }
}
