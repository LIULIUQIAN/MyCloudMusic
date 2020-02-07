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

    /**
     * 将分秒毫秒数据转为毫秒
     *
     * @param data 格式为：00:06.429
     * @return
     */
    public static long parseToInt(String data) {
        //将:替换成.
        data = data.replace(":", ".");

        //使用.拆分
        String[] strings = data.split("\\.");

        //分别取出分秒毫秒
        int m = Integer.parseInt(strings[0]);
        int s = Integer.parseInt(strings[1]);
        int ms = Integer.parseInt(strings[2]);

        //转为毫秒
        return (m * 60 + s) * 1000 + ms;
    }
}
