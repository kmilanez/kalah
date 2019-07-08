package com.kmilanez.kalah.repository;

import com.kmilanez.kalah.domain.entity.game.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, String> {
    Game findGameById(String gameId);
}
