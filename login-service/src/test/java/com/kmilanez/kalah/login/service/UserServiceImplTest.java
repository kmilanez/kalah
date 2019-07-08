package com.kmilanez.kalah.login.service;

import com.kmilanez.kalah.login.domain.entity.User;
import com.kmilanez.kalah.login.domain.exception.UserNotFoundException;
import com.kmilanez.kalah.login.repository.UserRepository;
import com.kmilanez.kalah.login.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldThrowInvalidValueWhenUsernameIsMissing() {
        // given
        User newUser = new User("", "testPassword");
        // then
        assertThrows(IllegalArgumentException.class, () -> userService.add(newUser));
    }

    @Test
    public void shouldThrowInvalidValueWhenPasswordIsMissing() {
        // given
        User newUser = new User("first-player", "");
        // then
        assertThrows(IllegalArgumentException.class, () -> userService.add(newUser));
    }

    @Test
    public void shouldCreateUserWhenNewUserHasRequiredFields() {
        // given
        User newUser = new User("first-player", "testPassword");
        String hashedPassword = "$2a$10$mW0Y1bE8wGZjkzHV8Vq1T.En.NVyxBWN61xgX1KV5OE4TTiCKB93.";
        // when
        when(passwordEncoder.encode(newUser.getPassword())).thenReturn(hashedPassword);
        User savedUser = userService.add(newUser);
        // then
        assertThat(savedUser.getUsername()).isNotNull();
        assertThat(savedUser.getPassword()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(hashedPassword);
    }

    @Test
    public void shouldThrowUserNotFoundWhenUserDoesntExist() {
        // given
        String username = "first-player";
        // when
        when(repository.findByUsername(username)).thenReturn(null);
        // then
        assertThrows(UserNotFoundException.class,
                () -> userService.loadUserByUsername(username));
    }

    @Test
    public void shouldReturnUserWhenUserExists() {
        // given
        String username = "first-player";
        String password = "$2a$10$mW0Y1bE8wGZjkzHV8Vq1T.En.NVyxBWN61xgX1KV5OE4TTiCKB93.";
        User user = new User(username, password);
        // when
        when(repository.findByUsername(username)).thenReturn(user);
        User dbUser = userService.loadUserByUsername(username);
        // then
        assertThat(dbUser.getUsername()).isNotNull();
        assertThat(dbUser.getPassword()).isNotNull();
        assertThat(dbUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(dbUser.getPassword()).isEqualTo(user.getPassword());
    }
}
