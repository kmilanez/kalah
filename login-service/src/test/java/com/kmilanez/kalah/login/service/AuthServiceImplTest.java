package com.kmilanez.kalah.login.service;

import com.kmilanez.kalah.login.domain.entity.User;
import com.kmilanez.kalah.login.domain.exception.InvalidPasswordException;
import com.kmilanez.kalah.login.domain.exception.UserNotFoundException;
import com.kmilanez.kalah.login.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceImplTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void shouldNotAuthenticateUserIfUsernameIsMissing() {
        // given
        User user = new User("", "testPassword");
        // then
        assertThrows(IllegalArgumentException.class, () -> authService.auth(user));
    }

    @Test
    public void shouldNotAuthenticateUserIfPasswordIsMissing() {
        // given
        User user = new User("first-player", "");
        // then
        assertThrows(IllegalArgumentException.class, () -> authService.auth(user));
    }

    @Test
    public void shouldNotAuthenticateIfUserDoesNotExist() {
        // given
        User user = new User("first-player", "testPassword");
        // when
        when(userService.loadUserByUsername(user.getUsername())).thenReturn(null);
        // then
        assertThrows(UserNotFoundException.class, () -> authService.auth(user));
    }

    @Test
    public void shouldNotAuthenticateIfPasswordDoesntMatch() {
        // given
        User user = new User("first-player", "testPassword");
        String hashedPassword = "$2a$10$OKktz9BXtWaNYitLsWhHtOwsn9d3MITGiJ/MayXB4noGrob3vcN/G";
        User dbUser = new User(user.getUsername(), hashedPassword);
        // when
        when(userService.loadUserByUsername(user.getUsername())).thenReturn(dbUser);
        when(passwordEncoder.matches(user.getPassword(), dbUser.getPassword())).thenReturn(false);
        // then
        assertThrows(InvalidPasswordException.class, () -> authService.auth(user));
    }

    @Test
    public void shouldAuthenticateIfUserIsValid() {
        // given
        User user = new User("first-player", "testPassword");
        String hashedPassword = "$2a$10$YPmTx/VZBuhqtlFYRtEtU.fn0HO9jW0G8ipuV6m2Nod/4BOTHL/ha";
        User dbUser = new User(user.getUsername(), hashedPassword);
        String userToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnZ29kb3kiLCJleHAiOjE1NjI1Mjg3NzZ9.l8jcs-KEl9r27FuKHhfbnzHkYe2udleF_BrHIs-ffM9IFZUAxbiFyZudKccHsltCgqKUMWB7iahd9ikXojIrVA";
        // when
        when(userService.loadUserByUsername(user.getUsername())).thenReturn(dbUser);
        when(passwordEncoder.matches(user.getPassword(), dbUser.getPassword())).thenReturn(true);
        when(tokenService.createToken(user.getUsername())).thenReturn(userToken);
        String token = authService.auth(user);
        // then
        assertThat(token).isNotEmpty();
        assertThat(token).isEqualTo(userToken);
    }
}
