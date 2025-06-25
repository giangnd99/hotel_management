package com.poly.authentication.service.domain.constant;

import org.springframework.beans.factory.annotation.Value;

public class Google {

    @Value("${google.client.id}")
    public static String CLIENT_ID;

    @Value("${google.client.secret}")
    public static String CLIENT_SECRET;
    public static final String REDIRECT_URI = "http://localhost:5173/login-success";
    public static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
    public static final String GRANT_TYPE = "authorization_code";

    private Google() {
    }
}
