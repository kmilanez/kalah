package com.kmilanez.kalah.domain.exception;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {
        super("Invalid user");
    }
}
