package com.kmilanez.kalah.service;

import com.kmilanez.kalah.domain.entity.game.Game;

public interface GameService {
    Game createGame(final String token, String serviceUrl);
    Game joinGame(final String token, final String gameId);
}
