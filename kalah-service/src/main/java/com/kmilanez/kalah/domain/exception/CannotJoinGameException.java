package com.kmilanez.kalah.domain.exception;

public class CannotJoinGameException extends RuntimeException {
    public CannotJoinGameException() {
        super("User cannot join game because either is already " +
                "in the game or game is already full");
    }
}
