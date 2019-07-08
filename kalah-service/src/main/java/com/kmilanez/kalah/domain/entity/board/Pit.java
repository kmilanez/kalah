package com.kmilanez.kalah.domain.entity.board;

import com.kmilanez.kalah.domain.entity.player.Player;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Pit {
    private int id;
    private int stones;
    private Player ownedBy;
    private boolean isKalah;

    public void addStone() {
        this.stones += 1;
    }

    public void addStones(int amount) {
        this.stones += this.stones + amount;
    }

    public int pickStones() {
        int pitStones = stones;
        this.stones = 0;
        return pitStones;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return stones == 0;
    }

    public boolean isOwnedBy(Player player) {
        return ownedBy.equals(player);
    }
}
