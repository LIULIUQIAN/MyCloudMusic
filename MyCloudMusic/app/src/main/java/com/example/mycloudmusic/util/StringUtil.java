package com.example.mycloudmusic.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.MatchResult;
import com.example.mycloudmusic.listener.OnTagClickListener;
import com.example.mycloudmusic.span.MClickableSpan;

import java.util.ArrayList;
import java.util.List;

import static com.example.mycloudmusic.util.Constant.HAST_TAG;
import static com.example.mycloudmusic.util.Constant.MENTION;
import static com.example.mycloudmusic.util.Constant.REGEX_EMAIL;
import static com.example.mycloudmusic.util.Constant.REGEX_PHONE;

public class StringUtil {


    /**
     * 是否是手机号
     *
     * @param value
     * @return
     */
    public static boolean isPhone(String value) {
        return value.matches(REGEX_PHONE);
    }

    /**
     * 是否是邮箱
     *
     * @param value
     * @return
     */
    public static boolean isEmail(String value) {
        return value.matches(REGEX_EMAIL);
    }

    /**
     * 是否是密码格式
     *
     * @param value
     * @return
     */
    public static boolean isPassword(String value) {
        return value.trim().length() >= 6 && value.trim().length() <= 15;
    }

    /**
     * 是否符合昵称格式
     */
    public static boolean isNickname(String value) {
        return value.length() >= 2 && value.length() <= 10;
    }

    /**
     * 将一行字符串
     * 拆分为单个字
     * <p>
     * 只实现了中文
     * 英文是有问题的
     * 大家感兴趣可以在继续实现
     *
     * @param data
     * @return
     */
    public static String[] words(String data) {
        //创建一个列表
        List<String> results = new ArrayList<>();

        //转为char数组
        char[] chars = data.toCharArray();

        //循环每一个字符
        for (char c : chars
        ) {
            //转为字符串
            //冰添加到列表
            results.add(String.valueOf(c));
        }

        //转为数组
        return results.toArray(new String[results.size()]);
    }

    /**
     * 文本进行高亮
     */
    public static SpannableString processHighlight(Context context, String data) {

        List<MatchResult> mentionsAndHashTags = RegUtil.findHashTags(data);
        mentionsAndHashTags.addAll(RegUtil.findMentions(data));

        SpannableString result = new SpannableString(data);

        for (MatchResult matchResult : mentionsAndHashTags) {
            ForegroundColorSpan span = new ForegroundColorSpan(context.getResources().getColor(R.color.text_highlight));
            result.setSpan(span, matchResult.getStart(), matchResult.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return result;
    }

    /**
     * 处理文本添加点击事件
     */
    public static SpannableString processContent(Context context, String data,
                                                 OnTagClickListener onHashTagsListener,
                                                 OnTagClickListener onMentionsListener) {

        SpannableString result = new SpannableString(data);

        List<MatchResult> tags = RegUtil.findHashTags(data);
        for (MatchResult matchResult : tags) {

            MClickableSpan span = new MClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    onHashTagsListener.onTagClick(matchResult.getContent(), matchResult);
                }
            };
            result.setSpan(span, matchResult.getStart(), matchResult.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tags = RegUtil.findMentions(data);

        for (MatchResult matchResult : tags) {
            MClickableSpan span = new MClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    onMentionsListener.onTagClick(matchResult.getContent(), matchResult);
                }
            };

            result.setSpan(span, matchResult.getStart(), matchResult.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return result;

    }

    /**
     * 移除字符串中首的@
     * 移除首尾的#
     */
    public static String removePlaceholderString(String data) {

        if (data.startsWith(MENTION)) {
            return data.substring(1);
        } else if (data.startsWith(HAST_TAG)) {
            return data.substring(1, data.length() - 1);
        }

        return data;
    }
}
