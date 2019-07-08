package com.kmilanez.kalah.service.impl;

import com.kmilanez.kalah.domain.entity.game.Game;
import com.kmilanez.kalah.domain.exception.CannotJoinGameException;
import com.kmilanez.kalah.repository.GameRepository;
import com.kmilanez.kalah.service.GameService;
import com.kmilanez.kalah.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {


    private UserService userService;
    private GameRepository gameRepository;


    public GameServiceImpl(UserService userService,
                           GameRepository gameRepository) {
        this.userService = userService;
        this.gameRepository = gameRepository;
    }

    @Override
    public Game createGame(String token, String serviceUrl) {
        String username = userService.getUsername(token);
        Game newGame = new Game();
        newGame.createUrl(serviceUrl);
        newGame.addPlayer(username);
        newGame.setPlayerForTurn(username);
        gameRepository.save(newGame);
        return newGame;
    }

    @Override
    public Game joinGame(String token, String gameId) {
        String playerUsername = userService.getUsername(token);
        Game game = gameRepository.findGameById(gameId);
        checkIfUserCanJoinGame(game, playerUsername);
        game.addPlayer(playerUsername);
        gameRepository.save(game);
        return game;
    }

    private void checkIfUserCanJoinGame(Game game, String playerUsername) {
        if (game.findPlayer(playerUsername) || game.reachedMaxUsers()) {
            throw new CannotJoinGameException();
        }
    }
}
