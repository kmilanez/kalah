package com.kmilanez.kalah.service;

import com.kmilanez.kalah.domain.entity.game.Game;
import com.kmilanez.kalah.domain.entity.game.GameState;
import com.kmilanez.kalah.domain.exception.CannotJoinGameException;
import com.kmilanez.kalah.mock.UserMockedData;
import com.kmilanez.kalah.repository.GameRepository;
import com.kmilanez.kalah.service.impl.GameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.kmilanez.kalah.mock.UserMockedData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GameServiceImplTest {

    private static final String SERVICE_URL = "http://localhost:8080";

    @Mock
    private UserService userService;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    public void shouldCreateNewGameWhenRequested() {
        // when
        when(userService.getUsername(DUMMY_FIRST_PLAYER_TOKEN)).thenReturn(DUMMY_FIRST_PLAYER);
        Game newGame = gameService.createGame(DUMMY_FIRST_PLAYER_TOKEN, SERVICE_URL);
        // then
        assertThat(newGame.getUrl()).contains(SERVICE_URL);
        assertThat(newGame.getId()).isNotBlank();
        assertThat(newGame.getTurnPlayer()).isEqualTo(newGame.getPlayer(DUMMY_FIRST_PLAYER));
        assertThat(newGame.getState()).isEqualTo(GameState.WAITING);
    }

    @Test
    public void shouldThrowCannotJoinGameWhenUserAlreadyInTheGameOrItsFull() {
        // given
        Game game =  new Game();
        game.createUrl(SERVICE_URL);
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        // when
        when(userService.getUsername(DUMMY_SECOND_PLAYER_TOKEN)).thenReturn(DUMMY_SECOND_PLAYER);
        when(gameRepository.findGameById(any())).thenReturn(game);
        // then
        assertThrows(CannotJoinGameException.class, () ->
                gameService.joinGame(DUMMY_SECOND_PLAYER_TOKEN, SERVICE_URL));
    }


    @Test
    public void shouldAllowUserToJoinWhenUserIsNotInTheGameOrItsFull() {
        // given
        Game game =  new Game();
        game.createUrl(SERVICE_URL);
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        // when
        when(userService.getUsername(DUMMY_SECOND_PLAYER_TOKEN)).thenReturn(DUMMY_SECOND_PLAYER);
        when(gameRepository.findGameById(any())).thenReturn(game);
        Game joinedGame = gameService.joinGame(DUMMY_SECOND_PLAYER_TOKEN, SERVICE_URL);
        // then
        assertThat(joinedGame.getUrl()).contains(SERVICE_URL);
        assertThat(joinedGame.getId()).isNotBlank();
        assertThat(joinedGame.getPlayer(DUMMY_SECOND_PLAYER)).isNotNull();
        assertThat(joinedGame.getState()).isEqualTo(GameState.STARTED);
        assertThat(joinedGame.reachedMaxUsers()).isTrue();
    }

}
