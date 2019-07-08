package com.kmilanez.kalah.domain.exception;

public class GameHasNotStartedException extends RuntimeException {
    public GameHasNotStartedException() {
        super("Game still doesn't have all required users to start");
    }
}
