package com.kmilanez.kalah.domain.entity;

import com.kmilanez.kalah.domain.entity.board.Board;
import com.kmilanez.kalah.domain.entity.player.Player;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardTest {

    private static final String DUMMY_FIRST_PLAYER = "first-player";
    private static final Integer DUMMY_FIRST_PLAYER_PIT = 4;
    private static final String DUMMY_SECOND_PLAYER = "second-player";
    private static final Integer DUMMY_SECOND_PLAYER_PIT = 11;

    @Test
    public void shouldRemoveStonesFromPitWhenPlayerPicksThem() {
        // given
        Board board = new Board();
        Player player = new Player(DUMMY_FIRST_PLAYER);
        // when
        board.addPlayerToBoard(player);
        board.getPitById(2).pickStones();
        // then
        assertThat(board.getPlayerPits(player)).isNotEmpty();
        assertThat(board.getPlayerPits(player).size()).isEqualTo(7);
        assertThat(board.getPitById(2).getStones()).isEqualTo(0);
    }

    @Test
    public void shouldFindPitByProvidedId() {
        // given
        Board board = new Board();
        Player player = new Player(DUMMY_FIRST_PLAYER);
        // when
        board.addPlayerToBoard(player);
        // then
        assertThat(board.getPlayerPits(player)).isNotEmpty();
        assertThat(board.getPlayerPits(player).size()).isEqualTo(7);
        assertThat(board.getPitById(DUMMY_FIRST_PLAYER_PIT).getStones()).isEqualTo(6);
    }

    @Test
    public void shouldAddStonesFromPitOnFirstPlayerMove() {
        // given
        Board board = new Board();
        Player firstPlayer = new Player(DUMMY_FIRST_PLAYER);
        Player secondPlayer = new Player(DUMMY_SECOND_PLAYER);
        // when
        board.addPlayerToBoard(firstPlayer);
        board.addPlayerToBoard(secondPlayer);
        board.moveStonesFromPit(4, firstPlayer);
        // then
        assertThat(board.getPitById(4).getStones()).isEqualTo(0);
        assertThat(board.getPitById(5).getStones()).isEqualTo(7);
        assertThat(board.getPitById(6).getStones()).isEqualTo(7);
        assertThat(board.getPitById(7).getStones()).isEqualTo(1);
        assertThat(board.getPitById(8).getStones()).isEqualTo(7);
        assertThat(board.getPitById(9).getStones()).isEqualTo(7);
        assertThat(board.getPitById(10).getStones()).isEqualTo(7);
    }

    @Test
    public void shouldAddStonesFromPitOnSecondPlayerMove() {
        // given
        Board board = new Board();
        Player firstPlayer = new Player(DUMMY_FIRST_PLAYER);
        Player secondPlayer = new Player(DUMMY_SECOND_PLAYER);
        // when
        board.addPlayerToBoard(firstPlayer);
        board.addPlayerToBoard(secondPlayer);
        board.moveStonesFromPit(DUMMY_FIRST_PLAYER_PIT, firstPlayer);
        board.moveStonesFromPit(DUMMY_SECOND_PLAYER_PIT, secondPlayer);
        // then
        assertThat(board.getPitById(4).getStones()).isEqualTo(0);
        assertThat(board.getPitById(5).getStones()).isEqualTo(7);
        assertThat(board.getPitById(6).getStones()).isEqualTo(7);
        assertThat(board.getPitById(7).getStones()).isEqualTo(1);
        assertThat(board.getPitById(8).getStones()).isEqualTo(7);
        assertThat(board.getPitById(9).getStones()).isEqualTo(7);
        assertThat(board.getPitById(10).getStones()).isEqualTo(7);
        assertThat(board.getPitById(11).getStones()).isEqualTo(0);
        assertThat(board.getPitById(12).getStones()).isEqualTo(7);
        assertThat(board.getPitById(13).getStones()).isEqualTo(7);
        assertThat(board.getPitById(14).getStones()).isEqualTo(1);
        assertThat(board.getPitById(1).getStones()).isEqualTo(7);
        assertThat(board.getPitById(2).getStones()).isEqualTo(7);
        assertThat(board.getPitById(3).getStones()).isEqualTo(7);
    }



}
