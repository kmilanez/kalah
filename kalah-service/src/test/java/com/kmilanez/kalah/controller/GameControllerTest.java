package com.kmilanez.kalah.controller;

import com.kmilanez.kalah.domain.entity.ServiceResponse;
import com.kmilanez.kalah.domain.entity.game.Game;
import com.kmilanez.kalah.domain.entity.game.GameState;
import com.kmilanez.kalah.mock.UserMockedData;
import com.kmilanez.kalah.service.GameRulesService;
import com.kmilanez.kalah.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.kmilanez.kalah.mock.UserMockedData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GameControllerTest {

    private static final String SERVICE_URL = "http://localhost:8081";

    @Mock
    private GameService gameService;

    @Mock
    private GameRulesService gameRulesService;

    @InjectMocks
    private GameController gameController;

    @Test
    public void shouldCreateNewGameWhenRequested() {
        // given
        Game game = new Game();
        game.createUrl(SERVICE_URL);
        game.addPlayer(DUMMY_FIRST_PLAYER);
        // when
        when(gameService.createGame(any(), any())).thenReturn(game);
        ServiceResponse response = gameController.createGame(DUMMY_FIRST_PLAYER_TOKEN);
        // then
        assertThat(response.getId()).contains(game.getId());
        assertThat(response.getUrl()).contains(SERVICE_URL);
    }

    @Test
    public void shouldAllowUserToJoinWhenUserIsNotInTheGameOrItsFull() {
        // given
        Game game = new Game();
        game.createUrl(SERVICE_URL);
        game.addPlayer(DUMMY_FIRST_PLAYER);
        // when
        when(gameService.joinGame(any(), any())).thenReturn(game);
        ServiceResponse response = gameController.joinGame(DUMMY_SECOND_PLAYER_TOKEN, game.getId());
        // then
        assertThat(response.getId()).contains(game.getId());
        assertThat(response.getUrl()).contains(SERVICE_URL);
    }

    @Test
    public void shouldMoveStonesAndChangeTurnOnFirstPlayerTurn() {
        // given
        Game game = new Game();
        game.createUrl(SERVICE_URL);
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        game.moveStones(4);
        game.setPlayerForTurn(DUMMY_SECOND_PLAYER);
        // when
        when(gameRulesService.playTurn(any(), any(), any())).thenReturn(game);
        ServiceResponse response = gameController.playTurn(DUMMY_SECOND_PLAYER_TOKEN, game.getId(), 4);
        // then
        assertThat(response.getId()).contains(game.getId());
        assertThat(response.getUrl()).contains(SERVICE_URL);
        assertThat(response.getState().getState()).isEqualTo(GameState.STARTED);
        assertThat(response.getState().getPlayingNextRound()).isEqualTo(game.getPlayer(DUMMY_SECOND_PLAYER));
        assertThat(response.getState().getBoard().get(3).getStones()).isEqualTo(0);
        assertThat(response.getState().getBoard().get(4).getStones()).isEqualTo(7);
        assertThat(response.getState().getBoard().get(5).getStones()).isEqualTo(7);
        assertThat(response.getState().getBoard().get(6).getStones()).isEqualTo(1);
        assertThat(response.getState().getBoard().get(7).getStones()).isEqualTo(7);
        assertThat(response.getState().getBoard().get(8).getStones()).isEqualTo(7);
        assertThat(response.getState().getBoard().get(9).getStones()).isEqualTo(7);
    }


}
