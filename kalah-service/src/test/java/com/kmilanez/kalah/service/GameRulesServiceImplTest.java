package com.kmilanez.kalah.service;

import com.kmilanez.kalah.domain.entity.game.Game;
import com.kmilanez.kalah.domain.exception.*;
import com.kmilanez.kalah.repository.GameRepository;
import com.kmilanez.kalah.service.impl.GameRulesServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GameRulesServiceImplTest {

    private static final String DUMMY_FIRST_PLAYER = "first-player";
    private static final String DUMMY_FIRST_PLAYER_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnZ29kb3kiLCJleHAiOjE1NjI1Mjg3NzZ9.l8jcs-KEl9r27FuKHhfbnzHkYe2udleF_BrHIs-ffM9IFZUAxbiFyZudKccHsltCgqKUMWB7iahd9ikXojIrVA";
    private static final String DUMMY_SECOND_PLAYER = "second-player";
    private static final String DUMMY_SECOND_PLAYER_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrbWlsYW5leiIsImV4cCI6MTU2MjUyODczNn0.bm7nHSGSEqszbce4gMICK9RzeqvLnu9bd70DRVSh76iUdYRm-joRzOWrsFwh8TGpSi8pAtM5w3hQq4pdQb187Q";

    @Mock
    private UserService userService;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameRulesServiceImpl service;


    @Test
    public void shouldThrowGameHasNotStartedWhenGameIsWaitingForPlayers() {
        // given
        Game game = new Game();
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        // when
        when(userService.getUsername(DUMMY_FIRST_PLAYER_TOKEN)).thenReturn(DUMMY_SECOND_PLAYER);
        when(gameRepository.findGameById(game.getId())).thenReturn(game);
        // then
        assertThrows(GameHasNotStartedException.class,
                () -> service.playTurn(DUMMY_FIRST_PLAYER_TOKEN, game.getId(), 4));
    }

    @Test
    public void shouldThrowCannotPlayTurnWhenPlayerTriesToPlayOpponentTurn() {
        // given
        Game game = new Game();
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        // when
        when(userService.getUsername(DUMMY_SECOND_PLAYER_TOKEN)).thenReturn(DUMMY_SECOND_PLAYER);
        when(gameRepository.findGameById(game.getId())).thenReturn(game);
        // then
        assertThrows(CannotPlayTurnException.class,
                () -> service.playTurn(DUMMY_SECOND_PLAYER_TOKEN, game.getId(), 11));
    }

    @Test
    public void shouldThrowCannotPickStonesFromKalahWhenPitIdReferencesKalaId() {
        // given
        Game game = new Game();
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        game.moveStones(4);
        game.setPlayerForTurn(DUMMY_SECOND_PLAYER);
        // when
        when(userService.getUsername(DUMMY_SECOND_PLAYER_TOKEN)).thenReturn(DUMMY_SECOND_PLAYER);
        when(gameRepository.findGameById(game.getId())).thenReturn(game);
        // then
        assertThrows(CannotPickStonesFromKalah.class,
                () -> service.playTurn(DUMMY_SECOND_PLAYER_TOKEN, game.getId(), 14));
    }

    @Test
    public void shouldThrowPitIsEmptyWhenPitIdReferencesEmptyPit() {
        // given
        Game game = new Game();
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        game.moveStones(4);
        game.setPlayerForTurn(DUMMY_SECOND_PLAYER);
        game.moveStones(11);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        // when
        when(userService.getUsername(DUMMY_FIRST_PLAYER_TOKEN)).thenReturn(DUMMY_FIRST_PLAYER);
        when(gameRepository.findGameById(game.getId())).thenReturn(game);
        // then
        assertThrows(PitIsEmptyException.class,
                () -> service.playTurn(DUMMY_FIRST_PLAYER_TOKEN, game.getId(), 4));
    }

    @Test
    public void shouldThrowPlayerDoesntOwnPitWhenPitIdReferencesEmptyPit() {
        // given
        Game game = new Game();
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        // when
        when(userService.getUsername(DUMMY_FIRST_PLAYER_TOKEN)).thenReturn(DUMMY_FIRST_PLAYER);
        when(gameRepository.findGameById(game.getId())).thenReturn(game);
        // then
        assertThrows(PlayerDoesntOwnPitException.class,
                () -> service.playTurn(DUMMY_FIRST_PLAYER_TOKEN, game.getId(), 11));
    }


    @Test
    public void shouldMoveStonesAndChangeTurnOnFirstPlayerTurn() {
        // given
        Game game = new Game();
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        // when
        when(userService.getUsername(DUMMY_FIRST_PLAYER_TOKEN)).thenReturn(DUMMY_FIRST_PLAYER);
        when(gameRepository.findGameById(game.getId())).thenReturn(game);
        Game updatedGame = service.playTurn(DUMMY_FIRST_PLAYER_TOKEN, game.getId(), 4);
        // then
        assertThat(updatedGame.getTurnPlayer()).isEqualTo(game.getPlayer(DUMMY_SECOND_PLAYER));
        assertThat(updatedGame.getPitById(4).getStones()).isEqualTo(0);
        assertThat(updatedGame.getPitById(5).getStones()).isEqualTo(7);
        assertThat(updatedGame.getPitById(6).getStones()).isEqualTo(7);
        assertThat(updatedGame.getPitById(7).getStones()).isEqualTo(1);
        assertThat(updatedGame.getPitById(8).getStones()).isEqualTo(7);
        assertThat(updatedGame.getPitById(9).getStones()).isEqualTo(7);
        assertThat(updatedGame.getPitById(10).getStones()).isEqualTo(7);
    }

    @Test
    public void shouldMoveStonesAndChangeTurnOnSecondPlayerTurn() {
        // given
        Game game = new Game();
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        game.moveStones(4);
        game.setPlayerForTurn(DUMMY_SECOND_PLAYER);
        // when
        when(userService.getUsername(DUMMY_SECOND_PLAYER_TOKEN)).thenReturn(DUMMY_SECOND_PLAYER);
        when(gameRepository.findGameById(game.getId())).thenReturn(game);
        Game updatedGame = service.playTurn(DUMMY_SECOND_PLAYER_TOKEN, game.getId(), 11);
        // then
        assertThat(updatedGame.getTurnPlayer()).isEqualTo(game.getPlayer(DUMMY_FIRST_PLAYER));
        assertThat(updatedGame.getPitById(4).getStones()).isEqualTo(0);
        assertThat(updatedGame.getPitById(5).getStones()).isEqualTo(7);
        assertThat(updatedGame.getPitById(6).getStones()).isEqualTo(7);
        assertThat(updatedGame.getPitById(7).getStones()).isEqualTo(1);
        assertThat(updatedGame.getPitById(8).getStones()).isEqualTo(7);
        assertThat(updatedGame.getPitById(9).getStones()).isEqualTo(7);
        assertThat(updatedGame.getPitById(10).getStones()).isEqualTo(7);
    }

}
