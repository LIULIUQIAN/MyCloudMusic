package com.example.mycloudmusic.util.lyric;

import com.example.mycloudmusic.domain.lyric.Lyric;

import static com.example.mycloudmusic.util.Constant.KSC;

/**
 * 歌词解析器
 */
public class LyricParser {
    /**
     * 解析歌词
     *
     * @param type    歌词类型
     * @param content 歌词内容
     * @return 解析后的歌词对象
     */
    public static Lyric parse(int type, String content) {
        switch (type) {
            case KSC:
                return KSCLyricParser.parse(content);
            default:
                //默认解析LRC歌词
                return LRCLyricParser.parse(content);
        }
    }
}
