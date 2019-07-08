package com.kmilanez.kalah.service;

import com.kmilanez.kalah.domain.exception.InvalidUserException;
import com.kmilanez.kalah.domain.integration.service.login.IntegrationUser;
import com.kmilanez.kalah.domain.integration.service.login.LoginServiceClient;
import com.kmilanez.kalah.domain.integration.service.login.ResponseForLoginService;
import com.kmilanez.kalah.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.kmilanez.kalah.mock.UserMockedData.DUMMY_FIRST_PLAYER;
import static com.kmilanez.kalah.mock.UserMockedData.DUMMY_FIRST_PLAYER_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceImplTest {

    @Mock
    private LoginServiceClient loginService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldThrowInvalidUserWhenUserDoesnExists() {
        // when
        when(loginService.getUserFromToken(DUMMY_FIRST_PLAYER_TOKEN)).thenReturn(null);
        // then
        assertThrows(InvalidUserException.class,
                () -> userService.getUsername(DUMMY_FIRST_PLAYER_TOKEN));
    }

    @Test
    public void shouldReturnUserNameWhenUserExists() {
        // given
        IntegrationUser user = new IntegrationUser(DUMMY_FIRST_PLAYER);
        ResponseForLoginService loginServiceRespone = new ResponseForLoginService(user);
        // when
        when(loginService.getUserFromToken(DUMMY_FIRST_PLAYER_TOKEN)).thenReturn(loginServiceRespone);
        // then
        assertThat(userService.getUsername(DUMMY_FIRST_PLAYER_TOKEN)).isEqualTo(DUMMY_FIRST_PLAYER);
    }

}
