package com.poly.authentication.service.application.rest.controller.authen;

import com.nimbusds.jose.JOSEException;
import com.poly.authentication.service.domain.dto.ApiResponse;
import com.poly.authentication.service.domain.dto.reponse.AuthenticationResponse;
import com.poly.authentication.service.domain.dto.reponse.IntrospectResponse;
import com.poly.authentication.service.domain.dto.reponse.UserGGResponse;
import com.poly.authentication.service.domain.dto.request.AuthenticationRequest;
import com.poly.authentication.service.domain.dto.request.IntrospectRequest;
import com.poly.authentication.service.domain.dto.request.LogoutRequest;
import com.poly.authentication.service.domain.dto.request.RefreshRequest;
import com.poly.authentication.service.domain.port.in.service.AuthenticationService;
import com.poly.authentication.service.domain.port.in.service.GoogleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final GoogleService googleService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticationResponseApiResponse(@RequestBody AuthenticationRequest request) {

        var result = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder().
                result(result).
                build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {

        var result = authenticationService.introspect(request);

        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<?> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {

        authenticationService.logout(request);

        return ApiResponse.<String>builder().
                result("Account is logged out successfully ").
                build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder().
                result(authenticationService.refreshToken(request)).
                build();
    }

    @PostMapping("/callback")
    public ApiResponse<?> handleGoogleCallback(@RequestBody Map<String, String> requestData) {
        String code = requestData.get("code");

        if (code == null) {
            return ApiResponse.<String>builder()
                    .code(900)
                    .message("Lỗi: Không tìm thấy mã code!")
                    .build();
        }

        try {
            UserGGResponse user = googleService.getUserResponse(code);

            return ApiResponse.<String>builder()
                    .result(user.getToken())
                    .build();

        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .code(901)
                    .message("Lỗi: Không tìm được người dùng từ Google API!")
                    .build();
        }
    }
}
