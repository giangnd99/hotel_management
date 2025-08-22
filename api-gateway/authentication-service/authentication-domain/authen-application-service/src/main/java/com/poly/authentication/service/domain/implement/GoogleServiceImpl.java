package com.poly.authentication.service.domain.implement;


import com.poly.authentication.service.domain.constant.Google;
import com.poly.authentication.service.domain.dto.reponse.user.OutboundUserResponse;
import com.poly.authentication.service.domain.dto.reponse.user.UserGGResponse;
import com.poly.authentication.service.domain.dto.request.auth.ExchangeTokenRequest;
import com.poly.authentication.service.domain.port.in.service.AuthenticationService;
import com.poly.authentication.service.domain.port.in.service.GoogleService;
import com.poly.authentication.service.domain.port.out.httpclient.OutboundIdentityClient;
import com.poly.authentication.service.domain.port.out.httpclient.OutboundUserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {

    private final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final AuthenticationService authenticationService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    public String getAccessToken(String code) {
        log.info("Access token: {}", code);
        log.info(clientId);
        log.info(clientSecret);
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .grantType(Google.GRANT_TYPE)
                .code(code)
                .build());

        log.info("Access token: {}", response.getAccessToken());
        return response.getAccessToken();
    }

    @Override
    public UserGGResponse getUserResponse(String code) {
        var accessToken = getAccessToken(code);
        OutboundUserResponse userInfo = outboundUserClient.getUserInfo("json", accessToken);

        return authenticationService.processGoogleAccount(userInfo);
    }
}
