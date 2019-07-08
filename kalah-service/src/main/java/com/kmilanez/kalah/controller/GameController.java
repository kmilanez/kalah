package com.kmilanez.kalah.controller;

import com.kmilanez.kalah.domain.entity.ServiceResponse;
import com.kmilanez.kalah.domain.entity.game.Game;
import com.kmilanez.kalah.service.GameRulesService;
import com.kmilanez.kalah.service.GameService;
import com.kmilanez.kalah.utils.ServiceUrlUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
public class GameController {

    private GameService gameService;

    private GameRulesService gameRulesService;

    public GameController(final GameService gameService,
                          final GameRulesService gameRulesService) {
        this.gameService = gameService;
        this.gameRulesService = gameRulesService;
    }

    @PostMapping
    public ServiceResponse createGame(@RequestHeader("Authorization")final String token) {
        String serviceUrl = ServiceUrlUtils.createUrl(this.getClass());
        Game newGame = gameService.createGame(token, serviceUrl);
        return ServiceResponse.replyGameIds(newGame);
    }

    @PostMapping("/{gameId}")
    public ServiceResponse joinGame(@RequestHeader("Authorization")final String token,
                                    @PathVariable("gameId") String gameId) {
        Game joinedGame = gameService.joinGame(token, gameId);
        return ServiceResponse.replyGameIds(joinedGame);
    }

    @PutMapping("/{gameId}/{pitId}")
    public ServiceResponse playTurn(@RequestHeader("Authorization") final String token,
                                    @PathVariable("gameId") String gameId,
                                    @PathVariable("pitId") Integer pitId) {
        Game updatedGame = gameRulesService.playTurn(token, gameId, pitId);
        return ServiceResponse.replyTurnStatus(updatedGame);
    }
}
