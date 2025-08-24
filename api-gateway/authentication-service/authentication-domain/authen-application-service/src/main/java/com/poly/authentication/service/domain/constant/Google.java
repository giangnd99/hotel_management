package com.poly.authentication.service.domain.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Google {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    public String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    public String CLIENT_SECRET;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    public String REDIRECT_URI;
    public static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
    public static final String GRANT_TYPE = "authorization_code";

}
