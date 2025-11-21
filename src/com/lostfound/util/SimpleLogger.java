package com.lostfound.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleLogger {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("[" + timestamp + "] INFO: " + message);
    }

    public static void error(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.err.println("[" + timestamp + "] ERROR: " + message);
    }

    public static void debug(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("[" + timestamp + "] DEBUG: " + message);
    }
}