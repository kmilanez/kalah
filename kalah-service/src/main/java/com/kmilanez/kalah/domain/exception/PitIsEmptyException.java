package com.kmilanez.kalah.domain.exception;

public class PitIsEmptyException extends RuntimeException {
    public PitIsEmptyException() {
        super("Pit is empty");
    }
}
