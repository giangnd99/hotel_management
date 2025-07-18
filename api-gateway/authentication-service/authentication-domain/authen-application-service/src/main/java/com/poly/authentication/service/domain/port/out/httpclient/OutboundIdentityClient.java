package com.poly.authentication.service.domain.port.out.httpclient;

import com.poly.authentication.service.domain.dto.reponse.ExchangeTokenResonse;
import com.poly.authentication.service.domain.dto.request.ExchangeTokenRequest;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "outbound-identity" ,url = "https://oauth2.googleapis.com" )
public interface OutboundIdentityClient {
    @PostMapping(value = "/token" ,produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ExchangeTokenResonse exchangeToken(@QueryMap ExchangeTokenRequest request);

}
