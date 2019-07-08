package com.kmilanez.kalah.domain.exception;

public class CannotPickStonesFromKalah extends RuntimeException {
    public CannotPickStonesFromKalah() {
        super("Cannot pick stones from Kalah pit");
    }
}
