package com.poly.authentication.service.domain.implement;


import com.poly.authentication.service.domain.constant.Google;
import com.poly.authentication.service.domain.dto.reponse.OutboundUserResponse;
import com.poly.authentication.service.domain.dto.reponse.UserGGResponse;
import com.poly.authentication.service.domain.dto.request.ExchangeTokenRequest;
import com.poly.authentication.service.domain.port.in.service.AuthenticationService;
import com.poly.authentication.service.domain.port.in.service.GoogleService;
import com.poly.authentication.service.domain.port.out.httpclient.OutboundIdentityClient;
import com.poly.authentication.service.domain.port.out.httpclient.OutboundUserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleServiceImpl implements GoogleService {

    private final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final AuthenticationService authenticationService;

    public String getAccessToken(String code) {
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .clientId(Google.CLIENT_ID)
                .clientSecret(Google.CLIENT_SECRET)
                .redirectUri(Google.REDIRECT_URI)
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
