package com.kmilanez.kalah.domain.exception;

public class CannotPlayTurnException extends RuntimeException  {
    public CannotPlayTurnException() {
        super("Turn is with the opponent player or game is over");
    }
}
