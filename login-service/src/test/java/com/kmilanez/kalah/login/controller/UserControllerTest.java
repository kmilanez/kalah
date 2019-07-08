package com.kmilanez.kalah.login.controller;

import com.kmilanez.kalah.login.domain.entity.User;
import com.kmilanez.kalah.login.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void shouldCreateUserWhenNewUserHasRequiredFields() {
        // given
        String hashedPassword = "$2a$10$mW0Y1bE8wGZjkzHV8Vq1T.En.NVyxBWN61xgX1KV5OE4TTiCKB93.";
        User user = new User("first-player", hashedPassword);
        // when
        when(userService.add(any())).thenReturn(user);
        User savedUser = userController.addUser(user);
        // then
        assertThat(savedUser.getUsername()).isNotNull();
        assertThat(savedUser.getPassword()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(savedUser.getPassword()).isEqualTo(hashedPassword);
    }

    @Test
    public void shouldReturnUserWhenUserExists() {
        // given
        String username = "first-player";
        String password = "$2a$10$mW0Y1bE8wGZjkzHV8Vq1T.En.NVyxBWN61xgX1KV5OE4TTiCKB93.";
        User user = new User(username, password);
        // when
        when(userService.loadUserByUsername(username)).thenReturn(user);
        User dbUser = userService.loadUserByUsername(username);
        // then
        assertThat(dbUser.getUsername()).isNotNull();
        assertThat(dbUser.getPassword()).isNotNull();
        assertThat(dbUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(dbUser.getPassword()).isEqualTo(user.getPassword());
    }
}
