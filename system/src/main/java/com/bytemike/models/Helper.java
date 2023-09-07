package com.bytemike.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {
    public static String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }
}
