package com.kmilanez.kalah.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kmilanez.kalah.domain.entity.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TurnStatus state;

    public static ServiceResponse replyGameIds(Game game) {
        return new ServiceResponse(game.getId(), game.getUrl(), null);
    }

    public static ServiceResponse replyTurnStatus(Game game) {
        TurnStatus turnStatus = new TurnStatus(
                game.getPits(),
                game.getResult(),
                game.getState(),
                game.getTurnPlayer()
        );
        return new ServiceResponse(game.getId(), game.getUrl(), turnStatus);
    }
}
