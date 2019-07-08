package com.kmilanez.kalah.domain.entity.board;

import com.kmilanez.kalah.domain.entity.game.Game;
import com.kmilanez.kalah.domain.entity.player.Player;
import lombok.*;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Board {

    private static final int PITS_PER_PLAYER = 7;
    private static final int BOARD_PITS = 14;

    private List<Pit> pits;

    public Board() {
        this.pits = new ArrayList<>();
    }

    public void addPlayerToBoard(Player player) {
        Pit lastAssignedPit = pits.stream().reduce((first, second) -> second).orElse(null);

        int lastAssignedPitId = (!ObjectUtils.isEmpty(lastAssignedPit) ? lastAssignedPit.getId() : 0) + 1;

        List<Pit> playerPits = IntStream.range(lastAssignedPitId, lastAssignedPitId + PITS_PER_PLAYER)
                .mapToObj(id -> initPit(id, player))
                .collect(Collectors.toList());

        this.pits.addAll(playerPits);
    }

    private Pit initPit(int id, Player player) {
        if (id % PITS_PER_PLAYER == 0) {
            return new Pit(id, 0, player, true);
        } else {
            return new Pit(id, 6, player, false);
        }
    }

    public List<Pit> getPlayerPits(Player player) {
        return pits.stream()
            .filter(pit -> pit.getOwnedBy().equals(player))
            .collect(Collectors.toList());
    }

    public Pit getPlayerKalah(Player player) {
        return pits.stream()
                .filter(pit -> pit.getOwnedBy().equals(player) && pit.isKalah())
                .findAny()
                .get();
    }

    public Pit getPitById(Integer pitId) {
        return pits.stream()
                .filter(pit -> pit.getId() == pitId)
                .findAny()
                .get();
    }

    public Pit moveStonesFromPit(Integer pitId, Player player) {
        int pitsInBoard = pits.size();
        int stonesInPit = getPitById(pitId).pickStones();

        Pit lastPitTouched = null;

        for (int stone = 1; stone <= stonesInPit; stone++) {
            int pitIdToAddStone = (stone + pitId);
            if (pitIdToAddStone > pitsInBoard) {
                pitIdToAddStone = pitIdToAddStone % pitsInBoard;
            }
            Pit pitToMoveStone = getPitById(pitIdToAddStone);
            if (isOpponentKalah(pitToMoveStone, player)) {
                pitIdToAddStone += 1;
                pitToMoveStone = getPitById(pitIdToAddStone);
            }
            pitToMoveStone.addStone();
            lastPitTouched = pitToMoveStone;
        }

        return lastPitTouched;
    }

    private boolean isOpponentKalah(final Pit pit, final Player player) {
        return pit.isKalah() && !pit.getOwnedBy().equals(player);
    }

    public void captureStones(final Pit stoneSourcePit, final Pit stoneDestinationPit) {
        if (!stoneSourcePit.isEmpty()) {
            int stonesToCapture = stoneSourcePit.pickStones();
            stoneDestinationPit.addStones(stonesToCapture);
        }
    }

    public boolean arePlayerPitsEmpty(List<Pit> playerPits) {
        return playerPits.stream().filter(Pit::isEmpty).count() >= 6;
    }

    public void addStoneFromPitsToKalah(List<Pit> pits, Pit kalah) {
        int stonesToAdd = pits.stream()
                .filter(pit -> !pit.isKalah())
                .mapToInt(Pit::pickStones)
                .sum();
        kalah.addStones(stonesToAdd);
    }

}
