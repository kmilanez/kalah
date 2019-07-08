package com.kmilanez.kalah.login.service;

import com.kmilanez.kalah.login.service.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenServiceImplTest {

    private TokenServiceImpl tokenService;

    @BeforeEach
    public void init() {
        this.tokenService = new TokenServiceImpl();
    }

    @Test
    public void shouldCreateTokenWhenUsernameIsProvided() {
        // when
        String username = "first-player";
        // when
        String token = tokenService.createToken(username);
        // then
        assertThat(token).isNotEmpty();
    }

    @Test
    public void shouldReturnUsernameWhenTokenIsValid() {
        // when
        String username = "first-player";
        // when
        String token = tokenService.createToken(username);
        String decodedUsername = tokenService.getUsername(token);
        // then
        assertThat(username).isNotEmpty();
        assertThat(decodedUsername).isEqualTo(username);
    }

}
