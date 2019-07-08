package com.kmilanez.kalah.service;

import com.kmilanez.kalah.domain.entity.game.Game;

public interface GameRulesService {
    Game playTurn(String token, String gameId, Integer pitId);
}
