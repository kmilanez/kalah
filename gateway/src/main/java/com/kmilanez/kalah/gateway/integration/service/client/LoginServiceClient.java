package com.kmilanez.kalah.gateway.integration.service.client;

import com.kmilanez.kalah.gateway.integration.service.model.ResponseForLoginService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "login-service")
public interface LoginServiceClient {
    @RequestMapping(value = "auth/me", method = RequestMethod.GET)
    ResponseForLoginService getUserFromToken(@RequestHeader("Authorization") String token);
}
