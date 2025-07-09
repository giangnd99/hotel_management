package com.poly.authentication.service.application.rest.controller.user;

import com.poly.authentication.service.domain.dto.ApiResponse;
import com.poly.authentication.service.domain.dto.reponse.UserResponse;
import com.poly.authentication.service.domain.dto.request.UserCreationRequest;
import com.poly.authentication.service.domain.dto.request.UserUpdatedRequest;
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
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdatedRequest request) {
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
//
//    @PostMapping("/upload-avatar/{userId}")
//    ApiResponse<?> uploadAvatar(@PathVariable Integer userId, @RequestParam MultipartFile avatar) {
//        userService.uploadAvatar(userId, avatar);
//        return ApiResponse.<String>builder()
//                .result("Upload avatar successfully")
//                .build();
//    }
}
