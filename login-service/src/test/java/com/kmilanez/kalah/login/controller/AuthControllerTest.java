package com.kmilanez.kalah.login.controller;

import com.kmilanez.kalah.login.domain.ServiceResponse;
import com.kmilanez.kalah.login.domain.entity.User;
import com.kmilanez.kalah.login.service.AuthService;
import com.kmilanez.kalah.login.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void shouldAuthenticateIfUserIsValid() {
        // given
        User user = new User("first-player", "testPassword");
        String userToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnZ29kb3kiLCJleHAiOjE1NjI1Mjg3NzZ9.l8jcs-KEl9r27FuKHhfbnzHkYe2udleF_BrHIs-ffM9IFZUAxbiFyZudKccHsltCgqKUMWB7iahd9ikXojIrVA";
        // when
        when(authService.auth(user)).thenReturn(userToken);
        ServiceResponse response = authController.auth(user);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNotEmpty();
        assertThat(response.getToken()).isEqualTo(userToken);
    }

    @Test
    public void shouldReturnUserWhenTokenIsValid() {
        // given
        String userToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnZ29kb3kiLCJleHAiOjE1NjI1Mjg3NzZ9.l8jcs-KEl9r27FuKHhfbnzHkYe2udleF_BrHIs-ffM9IFZUAxbiFyZudKccHsltCgqKUMWB7iahd9ikXojIrVA";
        User user = new User("first-player", "testPassword");
        // when
        when(tokenService.getUsername(userToken)).thenReturn(user.getUsername());
        ServiceResponse response = authController.getUserFromToken(userToken);
        // then
        assertThat(response).isNotNull();
        assertThat(response.getUser()).isNotNull();
        assertThat(response.getUser().getUsername()).isNotEmpty();
        assertThat(response.getUser().getUsername()).isEqualTo(user.getUsername());
    }


}
