package com.kmilanez.kalah.service.impl;

import com.kmilanez.kalah.domain.entity.board.Pit;
import com.kmilanez.kalah.domain.entity.game.Game;
import com.kmilanez.kalah.domain.entity.player.Player;
import com.kmilanez.kalah.domain.exception.*;
import com.kmilanez.kalah.repository.GameRepository;
import com.kmilanez.kalah.service.GameRulesService;
import com.kmilanez.kalah.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GameRulesServiceImpl implements GameRulesService {

    private UserService userService;
    private GameRepository gameRepository;

    public GameRulesServiceImpl(UserService userService,
                                GameRepository gameRepository) {
        this.userService = userService;
        this.gameRepository = gameRepository;
    }

    @Override
    public Game playTurn(final String token, final String gameId, final Integer pitId) {
        String playerUsername = userService.getUsername(token);
        Game game = gameRepository.findGameById(gameId);
        checkIfTurnIsPlayable(game, playerUsername);
        checkIfPitIsValid(game, pitId);
        moveStones(game, pitId);
        captureStonesIfLastPitWasEmpty(game);
        endGameOrChangeTurn(game);
        gameRepository.save(game);
        return game;
    }

    private void checkIfTurnIsPlayable(final Game game, final String playerUsername) {
        checkIfGameStarted(game);
        checkIfPlayerCanPlayTurn(game, playerUsername);
    }

    private void checkIfGameStarted(final Game game) {
        if (!game.reachedMaxUsers()) {
            throw new GameHasNotStartedException();
        }
    }

    private void checkIfPlayerCanPlayTurn(final Game game, final String playerUsername) {
        if (!game.isPlayerTurn(playerUsername) || game.isOver()) {
            throw new CannotPlayTurnException();
        }
    }

    private void checkIfPitIsValid(final Game game, final Integer pitId) {
        checkIfPitIsNotKalah(game, pitId);
        checkIfPitIsNotEmpty(game, pitId);
        checkIfPitBelongsToPlayer(game, pitId);
    }

    private void checkIfPitIsNotKalah(final Game game, final Integer pitId) {
        if (game.getPitById(pitId).isKalah()) {
            throw new CannotPickStonesFromKalah();
        }
    }

    private void checkIfPitIsNotEmpty(final Game game, final Integer pitId) {
        boolean pitIsEmpty = game.getPitById(pitId).isEmpty();
        if (pitIsEmpty) {
            throw new PitIsEmptyException();
        }
    }

    private void checkIfPitBelongsToPlayer(final Game game, final Integer pitId) {
        boolean userDoesntOwnsPit = game.getTurnPlayerPits()
                .stream()
                .noneMatch(pit -> pit.getId() == pitId);
        if (userDoesntOwnsPit) {
            throw new PlayerDoesntOwnPitException();
        }
    }

    private void moveStones(Game game, final Integer pitId) {
        game.moveStones(pitId);
    }

    private void captureStonesIfLastPitWasEmpty(final Game game) {
        Pit lastPitToReceiveStone = game.getLastPitToReceiveStone();

        boolean pitOwnedByTurnPlayer = lastPitToReceiveStone.isOwnedBy(game.getTurnPlayer());

        boolean pitWasEmpty = lastPitToReceiveStone.getStones() == 1;

        if (pitOwnedByTurnPlayer && pitWasEmpty) {
            game.captureStonesFromPit(lastPitToReceiveStone, game.getTurnPlayerKalah());
            game.captureStonesFromPit(game.getOponnentPlayerKalah(), game.getTurnPlayerKalah());
        }
    }

    private void endGameOrChangeTurn(Game game) {
        if (game.arePlayerPitsEmpty(game.getTurnPlayerPits()) ||
                game.arePlayerPitsEmpty(game.getTurnPlayerPits())) {
            endGame(game);
        } else {
            decideNextTurn(game);
        }
    }

    private void endGame(Game game) {
        sumUpRemainingStonesInBoard(game);
        wrapUpMatch(game);
    }

    private void sumUpRemainingStonesInBoard(Game game) {
        if (game.arePlayerPitsEmpty(game.getTurnPlayerPits())) {
            game.addStoneFromPitsToKalah(game.getTurnPlayerPits(),
                    game.getTurnPlayerKalah());
        } else {
            game.addStoneFromPitsToKalah(game.getOponnentPlayerPits(),
                    game.getOponnentPlayerKalah());
        }
    }

    private void wrapUpMatch(Game game) {

        int stoneDifference = game.getTurnPlayerKalah().getStones() - game.getOponnentPlayerKalah().getStones();

        // Turn player wins!
        if (stoneDifference > 0) {
           game.markPlayerOneAsWinner();
        }

        // Oponent player wins!
        if (stoneDifference < 0) {
            game.markPlayerTwoAsWinner();
        }

        // Draw!
        if (stoneDifference == 0) {
            game.markDraw();
        }

        game.endGame();
    }

    private void decideNextTurn(Game game) {
        Player player = game.getTurnPlayer();

        Pit lastPitToReceiveStone = game.getLastPitToReceiveStone();

        if (!isPlayerKalah(lastPitToReceiveStone, player)) {
            game.setPlayerForTurn(game.getOpponentPlayer());
        } else {
            game.setPlayerForTurn(player);
        }
    }
    
    private boolean isPlayerKalah(Pit pit, Player player) {
        return pit.isKalah() && !pit.getOwnedBy().equals(player);
    }

}
