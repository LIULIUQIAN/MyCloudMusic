package com.example.mycloudmusic.util;

import com.example.mycloudmusic.domain.MatchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtil {


    private static final String REG_MENTION = "(@[\\u4e00-\\u9fa5a-zA-Z0-9_-]{1,30})";
    private static final String REG_HASH_TAG = "(#.*?#)";

    public static List<MatchResult> find(String red, String data) {
        List<MatchResult> results = new ArrayList<>();

        Pattern pattern = Pattern.compile(red);

        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            results.add(new MatchResult(matcher.start(), matcher.end(), matcher.group(0).trim()));
        }

        return results;
    }

    /**
     * 查找mentions
     */
    public static List<MatchResult> findMentions(String data) {
        return find(REG_MENTION, data);
    }

    /**
     * 查找话题
     */
    public static List<MatchResult> findHashTags(String data) {
        return find(REG_HASH_TAG, data);
    }
}


