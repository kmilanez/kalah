package com.kmilanez.kalah.login.service;

public interface TokenService {

    String createToken(final String username);
    String getUsername(final String token);

}
