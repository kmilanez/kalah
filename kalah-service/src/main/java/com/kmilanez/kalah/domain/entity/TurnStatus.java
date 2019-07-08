package com.kmilanez.kalah.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kmilanez.kalah.domain.entity.board.Pit;
import com.kmilanez.kalah.domain.entity.game.GameResult;
import com.kmilanez.kalah.domain.entity.game.GameState;
import com.kmilanez.kalah.domain.entity.player.Player;
import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TurnStatus {
    private List<Pit> board;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GameResult winner;
    private GameState state;
    private Player playingNextRound;
}
