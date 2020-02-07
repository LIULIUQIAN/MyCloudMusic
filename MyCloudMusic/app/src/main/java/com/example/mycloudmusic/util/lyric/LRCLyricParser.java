package com.example.mycloudmusic.util.lyric;

import com.example.mycloudmusic.domain.TimeUtil;
import com.example.mycloudmusic.domain.lyric.Line;
import com.example.mycloudmusic.domain.lyric.Lyric;

import java.util.ArrayList;
import java.util.List;

/**
 * LRC歌词解析器
 */
class LRCLyricParser {
    /**
     * 解析歌词
     *
     * @param data
     * @return
     */
    public static Lyric parse(String data) {
        //创建歌词解析后的对象
        Lyric result = new Lyric();

        //初始化一个列表
        List<Line> datum = new ArrayList<>();

        //使用\n拆分歌词
        String[] strings = data.split("\n");

        //循环每一行歌词
        for (String lineString : strings
        ) {
            //解析每一行歌词
            Line line = parseLine(lineString);
            if (line != null) {
                //过滤了元数据歌词
                datum.add(line);
            }
        }

        //将歌词行列表设置到歌词对象
        result.setDatum(datum);

        //返回解析后的歌词
        return result;
    }

    /**
     * 解析一行歌词
     * 例如：[00:00.300]爱的代价 - 李宗盛
     *
     * @param data
     * @return
     */
    private static Line parseLine(String data) {
        //过滤元数据
        if (data.startsWith("[0")) {
            //创建歌词行
            Line line = new Line();

            //去除前面的[
            data = data.substring(1);

            //使用]拆分
            String[] commands = data.split("]", -1);

            //开始时间
            line.setStartTime(TimeUtil.parseToInt(commands[0]));

            //歌词
            line.setData(commands[1]);

            //返回解析后的歌词行
            return line;
        }

        return null;
    }
}
