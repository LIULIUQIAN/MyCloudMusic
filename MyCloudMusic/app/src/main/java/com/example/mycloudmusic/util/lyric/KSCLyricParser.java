package com.example.mycloudmusic.util.lyric;


import com.example.mycloudmusic.domain.TimeUtil;
import com.example.mycloudmusic.domain.lyric.Line;
import com.example.mycloudmusic.domain.lyric.Lyric;
import com.example.mycloudmusic.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * KSC歌词解析器
 */
public class KSCLyricParser {
    /**
     * 解析歌词
     *
     * @param data
     * @return
     */
    public static Lyric parse(String data) {
        //创建歌词对象
        Lyric result = new Lyric();

        //设置是精确到字歌词
        result.setAccurate(true);

        //创建一个列表
        List<Line> datum = new ArrayList<>();

        //使用\n拆分歌词
        String[] strings = data.split("\n");

        //循环每一行歌词
        for (String lineString : strings
        ) {
            Line line = parseLine(lineString);
            if (line != null) {
                datum.add(line);
            }
        }

        //将歌词列表设置到歌词对象
        result.setDatum(datum);

        //返回歌词对象
        return result;
    }

    /**
     * 解析每一行歌词
     * 例如：karaoke.add('00:27.487', '00:32.068', '一时失志不免怨叹', '347,373,1077,320,344,386,638,1096');
     */
    private static Line parseLine(String data) {
        if (data.startsWith("karaoke.add")) {
            //过滤了前面的元数据

            //创建结果对象
            Line result = new Line();

            //移除字符串前面的karaoke.add('
            //移除字符串后面的');
            //data=00:27.487', '00:32.068', '一时失志不免怨叹', '347,373,1077,320,344,386,638,1096
            data = data.substring(13, data.length() - 3);

            //使用', '拆分字符串
            String[] commands = data.split("', '", -1);

            //开始时间
            result.setStartTime(TimeUtil.parseToInt(commands[0]));

            //结束时间
            result.setEndTime(TimeUtil.parseToInt(commands[1]));

            //歌词
            result.setData(commands[2]);

            //将歌词拆分为每一个字
            result.setWords(StringUtil.words(result.getData()));

            //每一个字的时间列表
            List<Object> wordDurations = new ArrayList<>();

            String lyricTimeString = commands[3];

            //使用,拆分
            String[] lyricTimeWords = lyricTimeString.split(",");

            //将字符串时间转为int
            for (String string : lyricTimeWords
            ) {
                //转为int时方便后面的计算
                wordDurations.add(Integer.valueOf(string));
            }

            //将每一个字的时间也转为数组
            //是因为数组相对于列表来说更快
            result.setWordDurations(wordDurations.toArray(new Integer[wordDurations.size()]));

            //返回歌词行
            return result;
        }

        return null;
    }
}
