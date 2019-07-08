package com.kmilanez.kalah.utils;

import java.security.SecureRandom;

public final class GameIdUtils {
    public static String createGameId() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }
}
