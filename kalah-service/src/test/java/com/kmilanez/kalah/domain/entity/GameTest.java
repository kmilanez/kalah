package com.kmilanez.kalah.domain.entity;

import com.kmilanez.kalah.domain.entity.board.Board;
import com.kmilanez.kalah.domain.entity.game.Game;
import com.kmilanez.kalah.domain.entity.player.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {


    private static final String DUMMY_FIRST_PLAYER = "first-player";
    private static final String DUMMY_SECOND_PLAYER = "second-player";
    private static final String DEFAULT_SERVICE_URL = "http://127.0.0.1:8080";

    @Test
    public void shouldHaveAGameIdOfSixDigitsWhenGamesIsCreated() {
        // given
        Game game = new Game();
        // then
        assertThat(game.getId()).isNotBlank();
        assertThat(game.getId().length()).isEqualTo(6);
    }

    @Test
    public void shouldCreateJoinableUrlWhenRequestTo() {
        // given
        Game game = new Game();
        // when
        game.createUrl(DEFAULT_SERVICE_URL);
        // then
        assertThat(game.getId()).isNotBlank();
        assertThat(game.getUrl()).isNotBlank();
        assertThat(game.getUrl()).isEqualTo(
                String.format("%s/%s", DEFAULT_SERVICE_URL, game.getId())
        );
    }

    @Test
    public void shouldFindUsersIfTheyAreInTheGame() {
        // given
        Game game = new Game();
        // when
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        // then
        assertThat(game.getPlayers()).isNotEmpty();
        assertThat(game.getPlayers().size()).isEqualTo(2);
        assertThat(game.getPlayers().get(0).getUsername()).isEqualTo(DUMMY_FIRST_PLAYER);
        assertThat(game.getPlayers().get(1).getUsername()).isEqualTo(DUMMY_SECOND_PLAYER);
        assertThat(game.findPlayer(DUMMY_FIRST_PLAYER)).isTrue();
        assertThat(game.findPlayer(DUMMY_SECOND_PLAYER)).isTrue();
    }

    @Test
    public void shouldMarkGameAsFullIfThereAreAlreadyTwoPlayers() {
        // given
        Game game = new Game();
        // when
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        // then
        assertThat(game.reachedMaxUsers()).isTrue();
    }


    @Test
    public void shouldMoveStonesOnFirstPlayerTurn() {
        // given
        Game game = new Game();
        // when
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        game.moveStones(4);
        // then
        assertThat(game.getPitById(4).getStones()).isEqualTo(0);
        assertThat(game.getPitById(5).getStones()).isEqualTo(7);
        assertThat(game.getPitById(6).getStones()).isEqualTo(7);
        assertThat(game.getPitById(7).getStones()).isEqualTo(1);
        assertThat(game.getPitById(8).getStones()).isEqualTo(7);
        assertThat(game.getPitById(9).getStones()).isEqualTo(7);
        assertThat(game.getPitById(10).getStones()).isEqualTo(7);
    }


    @Test
    public void shouldMoveStonesOnSecondFirstPlayerTurn() {
        // given
        Game game = new Game();
        // when
        game.addPlayer(DUMMY_FIRST_PLAYER);
        game.addPlayer(DUMMY_SECOND_PLAYER);
        game.setPlayerForTurn(DUMMY_FIRST_PLAYER);
        game.moveStones(4);
        game.setPlayerForTurn(DUMMY_SECOND_PLAYER);
        game.moveStones(11);
        // then
        assertThat(game.getPitById(4).getStones()).isEqualTo(0);
        assertThat(game.getPitById(5).getStones()).isEqualTo(7);
        assertThat(game.getPitById(6).getStones()).isEqualTo(7);
        assertThat(game.getPitById(7).getStones()).isEqualTo(1);
        assertThat(game.getPitById(8).getStones()).isEqualTo(7);
        assertThat(game.getPitById(9).getStones()).isEqualTo(7);
        assertThat(game.getPitById(10).getStones()).isEqualTo(7);
        assertThat(game.getPitById(11).getStones()).isEqualTo(0);
        assertThat(game.getPitById(12).getStones()).isEqualTo(7);
        assertThat(game.getPitById(13).getStones()).isEqualTo(7);
        assertThat(game.getPitById(14).getStones()).isEqualTo(1);
        assertThat(game.getPitById(1).getStones()).isEqualTo(7);
        assertThat(game.getPitById(2).getStones()).isEqualTo(7);
        assertThat(game.getPitById(3).getStones()).isEqualTo(7);
    }

}
