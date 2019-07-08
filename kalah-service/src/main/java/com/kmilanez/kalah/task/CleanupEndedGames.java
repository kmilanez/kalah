package com.kmilanez.kalah.task;

import com.kmilanez.kalah.domain.entity.game.Game;
import com.kmilanez.kalah.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CleanupEndedGames {

    private GameRepository gameRepository;

    public CleanupEndedGames(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Scheduled(cron = "0 */5 * * *")
    public void cleanEndedGames() {
        log.info("Starting cleanup of ended games");
        List<Game> endedGames = gameRepository.findAll().stream()
                .filter(Game::isOver)
                .collect(Collectors.toList());
        endedGames.forEach(game -> {
            log.info("Removing game {}", game.getId());
            gameRepository.delete(game);
        });
        log.info("Cleanup completed!");
    }

}
