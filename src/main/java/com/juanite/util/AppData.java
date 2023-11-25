package com.juanite.util;

public class AppData {

    private static boolean isRunning = false;

    public static boolean isIsRunning() {
        return isRunning;
    }

    public static void setIsRunning(boolean isRunning) {
        AppData.isRunning = isRunning;
    }
}
