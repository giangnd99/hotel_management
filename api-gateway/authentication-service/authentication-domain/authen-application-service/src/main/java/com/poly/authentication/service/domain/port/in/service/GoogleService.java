package com.poly.authentication.service.domain.port.in.service;

import com.poly.authentication.service.domain.dto.reponse.user.UserGGResponse;

public interface GoogleService {

    UserGGResponse getUserResponse(String accessToken) throws Exception;
}
