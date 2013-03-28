package com.dci.intellij.dbn.common;

public class TimeUtil {
    public static int ONE_SECOND = 1000;
    public static int ONE_MINUTE = 1000*60;
    public static int FIVE_MINUTES = ONE_MINUTE * 1; // *5

    public static int getMinutes(int seconds) {
        return seconds / 60;
    }

    public static int getSeconds(int minutes) {
        return minutes * 60;
    }

}
