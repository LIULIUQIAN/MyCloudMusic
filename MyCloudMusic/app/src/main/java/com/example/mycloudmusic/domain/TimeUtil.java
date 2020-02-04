package com.example.mycloudmusic.domain;

public class TimeUtil {
    /**
     * 将秒格式化为分:秒，例如：15:11
     */
    public static String formatMinuteSecond(int data) {
        if (data == 0) {
            return "00:00";
        }
        data /= 1000;
        int minute = data / 60;
        int second = data - minute * 60;
        return String.format("%02d:%02d", minute, second);
    }
}
