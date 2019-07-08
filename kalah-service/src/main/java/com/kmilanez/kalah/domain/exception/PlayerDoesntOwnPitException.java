package com.kmilanez.kalah.domain.exception;

public class PlayerDoesntOwnPitException extends RuntimeException {
    public PlayerDoesntOwnPitException() {
        super("Pit doesn't belong to the player");
    }
}
