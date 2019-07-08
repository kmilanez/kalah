package com.kmilanez.kalah.domain.entity.game;

import com.kmilanez.kalah.domain.entity.board.Board;
import com.kmilanez.kalah.domain.entity.board.Pit;
import com.kmilanez.kalah.domain.entity.player.Player;
import com.kmilanez.kalah.utils.GameIdUtils;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Document(collection = "games")
public class Game {

    private static final int MAX_PLAYERS = 2;

    @Id
    @JsonIgnore
    private ObjectId _id;
    @Getter
    private String id;
    @Getter
    private String url;
    @JsonIgnore
    private Board board;
    @JsonIgnore
    @Getter
    @Setter
    private Pit lastPitToReceiveStone;
    @JsonIgnore
    @Getter
    private List<Player> players;
    @Getter
    private Player turnPlayer;
    @Getter
    private GameResult result;
    @Getter
    private GameState state;

    public Game() {
        this._id = ObjectId.get();
        this.id = GameIdUtils.createGameId();
        this.board = new Board();
        this.players = new ArrayList<>();
    }

    public void createUrl(String baseUrl) {
        this.url = String.format("%s/%s", baseUrl, id);
    }

    public void addPlayer(String playerUsername) {
        Player newPlayer = new Player(playerUsername);
        this.players.add(newPlayer);
        this.board.addPlayerToBoard(newPlayer);
        updateGameStatus();
    }

    private void updateGameStatus() {
        if (reachedMaxUsers()) {
            this.state = GameState.STARTED;
        } else {
            this.state = GameState.WAITING;
        }
    }

    public boolean findPlayer(String playerUsername) {
        return players.stream()
                .anyMatch(gamePlayer -> gamePlayer.getUsername().equals(playerUsername));
    }

    public Player getPlayer(String playerUsername) {
        return players.stream().filter(player -> player.getUsername().equals(playerUsername)).findFirst().get();
    }

    public boolean reachedMaxUsers() {
        return players.size() == MAX_PLAYERS;
    }

    public void setPlayerForTurn(String playerUsername) {
        this.turnPlayer = getPlayer(playerUsername);
    }

    public void setPlayerForTurn(Player player) {
        this.turnPlayer = player;
    }

    public boolean isPlayerTurn(String playerUsername) {
        return turnPlayer.getUsername().equals(playerUsername);
    }

    public Player getOpponentPlayer() {
        return players.stream().filter(player -> !player.equals(turnPlayer)).findAny().get();
    }

    public List<Pit> getPits() {
        return board.getPits();
    }

    public Pit getPitById(Integer pitId) {
        return board.getPitById(pitId);
    }

    public void moveStones(Integer pitId) {
        Pit lastTouchedPit =  board.moveStonesFromPit(pitId, turnPlayer);
        setLastPitToReceiveStone(lastTouchedPit);
    }

    public void captureStonesFromPit(final Pit sourcePit, final Pit destinationPit) {
        board.captureStones(sourcePit, destinationPit);
    }

    public List<Pit> getTurnPlayerPits() {
        return getPlayerPits(getTurnPlayer());
    }

    public Pit getTurnPlayerKalah() {
        return  getKalah(getTurnPlayer());
    }

    public List<Pit> getOponnentPlayerPits() {
        return getPlayerPits(getOpponentPlayer());
    }

    public Pit getOponnentPlayerKalah() {
        return getKalah(getOpponentPlayer());
    }

    private List<Pit> getPlayerPits(Player player) {
        return board.getPlayerPits(player);
    }

    public boolean arePlayerPitsEmpty(List<Pit> playerPits) {
        return playerPits.stream().filter(Pit::isEmpty).count() >= 6;
    }

    private Pit getKalah(Player player) {
        return board.getPlayerKalah(player);
    }

    public void addStoneFromPitsToKalah(List<Pit> pits, Pit kalah) {
        int stonesToAdd = pits.stream()
                .filter(pit -> !pit.isKalah())
                .mapToInt(Pit::pickStones)
                .sum();
        kalah.addStones(stonesToAdd);
    }

    public void markPlayerOneAsWinner() {
        this.result = GameResult.PLAYER_1_WINS;
    }

    public void markPlayerTwoAsWinner() {
        this.result = GameResult.PLAYER_2_WINS;
    }

    public void markDraw() {
        this.result = GameResult.DRAW;
    }

    public boolean isOver() {
        return this.state == GameState.ENDED;
    }

    public void endGame() {
        this.state = GameState.ENDED;
    }


}
