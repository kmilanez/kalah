package com.kmilanez.kalah.login.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kmilanez.kalah.login.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.kmilanez.kalah.login.domain.constant.TokenParameters.EXPIRTATION;
import static com.kmilanez.kalah.login.domain.constant.TokenParameters.SECRET;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String createToken(final String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRTATION))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    @Override
    public String getUsername(final String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }
}
