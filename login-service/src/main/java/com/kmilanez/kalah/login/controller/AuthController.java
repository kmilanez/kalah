package com.kmilanez.kalah.login.controller;

import com.kmilanez.kalah.login.domain.ServiceResponse;
import com.kmilanez.kalah.login.domain.entity.User;
import com.kmilanez.kalah.login.service.AuthService;
import com.kmilanez.kalah.login.service.TokenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private TokenService tokenService;
    private AuthService authService;

    public AuthController(final TokenService tokenService,
                          final AuthService authService) {
        this.tokenService = tokenService;
        this.authService = authService;
    }

    @PostMapping
    public ServiceResponse auth(@RequestBody final User user) {
        return ServiceResponse.replyToken(authService.auth(user));
    }

    @GetMapping("/me")
    public ServiceResponse getUserFromToken(@RequestHeader("Authorization") final String token) {
        return ServiceResponse.replyUser(new User(tokenService.getUsername(token)));
    }

}
