package com.example.mycloudmusic.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.lyric.Line;
import com.example.mycloudmusic.util.DensityUtil;
import com.example.mycloudmusic.util.TextUtil;

public class LyricLineView extends View {

    /**
     * 默认歌词字大小
     * 单位：dp
     */
    private static final float DEFAULT_LYRIC_TEXT_SIZE = 16;

    /**
     * 默认歌词颜色
     */
    private static final int DEFAULT_LYRIC_TEXT_COLOR = Color.WHITE;

    /**
     * 默认歌词高亮颜色
     */
    private static final int DEFAULT_LYRIC_SELECTED_TEXT_COLOR = Color.parseColor("#dd0000");

    /**
     * 默认歌词显示的内容
     */
    private static final String HINT_LYRIC_EMPTY = "我的云音乐,听你想听!";

    /**
     * 歌词位置 水平居左，垂直居中
     */
    private static final int GRAVITY_LEFT = 0x01;

    /**
     * 歌词位置 垂直水平居中
     */
    private static final int GRAVITY_CENTER = 0x10;

    /**
     * 当前歌词行
     */
    private Line data;

    /**
     * 是否是精确到字歌词
     */
    private boolean accurate;

    /**
     * 是否选中
     */
    private boolean lineSelected;

    /**
     * 默认歌词画笔
     */
    private Paint backgroundTextPaint;

    /**
     * 高亮画笔
     */
    private Paint foregroundTextPaint;

    /**
     * 测试画笔
     */
    private Paint testPaint;

    /**
     * 文字大小
     */
    private int lyricTextSize;

    /**
     * 歌词颜色
     */
    private int lyricTextColor;

    /**
     * 高亮歌词颜色
     */
    private int lyricSelectedTextColor;

    /**
     * 当前播放时间点，在改行的第几个字
     */
    private int lyricCurrentWordIndex;

    /**
     * 当前行歌词已经唱过的宽度，也就是歌词高亮的宽度
     */
    private float lineLyricPlayedWidth;

    /**
     * 当前字，已经播放的时间
     */
    private float wordPlayedTime;

    /**
     * 歌词位置
     */
    private int lyricGravity;

    public LyricLineView(Context context) {
        super(context);
        init(null);
    }

    public LyricLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LyricLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LyricLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {

        lyricTextSize = DensityUtil.dipTopx(getContext(), DEFAULT_LYRIC_TEXT_SIZE);
        lyricTextColor = DEFAULT_LYRIC_TEXT_COLOR;
        lyricSelectedTextColor = DEFAULT_LYRIC_SELECTED_TEXT_COLOR;

        //解析自定义属性
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.LyricLineView);
            lyricTextSize = (int) typedArray.getDimension(R.styleable.LyricLineView_text_size, lyricTextSize);
            lyricTextColor = typedArray.getColor(R.styleable.LyricLineView_text_color, lyricTextColor);
            lyricSelectedTextColor = typedArray.getColor(R.styleable.LyricLineView_selected_text_color, lyricSelectedTextColor);
            lyricGravity = typedArray.getInt(R.styleable.LyricLineView_gravity, GRAVITY_CENTER);

            typedArray.recycle();



        }
        //初始化画笔
        backgroundTextPaint = new Paint();
        backgroundTextPaint.setDither(true);
        backgroundTextPaint.setAntiAlias(true);
        backgroundTextPaint.setTextSize(lyricTextSize);
        backgroundTextPaint.setColor(lyricTextColor);

        foregroundTextPaint = new Paint();
        foregroundTextPaint.setDither(true);
        foregroundTextPaint.setAntiAlias(true);
        foregroundTextPaint.setTextSize(lyricTextSize);
        foregroundTextPaint.setColor(lyricSelectedTextColor);

        testPaint = new Paint();
        testPaint.setDither(true);
        testPaint.setAntiAlias(true);
        testPaint.setColor(Color.BLUE);
        testPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        if (isEmptyLyric()) {
            drawDefaultText(canvas);
        } else {
            drawLyricText(canvas);
        }

        canvas.restore();

    }

    /*
     * 绘制歌词
     * */
    private void drawLyricText(Canvas canvas) {

        float textWidth = TextUtil.getTextWidth(backgroundTextPaint, data.getData());
        float textHeight = TextUtil.getTextHeight(backgroundTextPaint);
        float centerX = getCenterX(textWidth);

        Paint.FontMetrics fontMetrics = backgroundTextPaint.getFontMetrics();

        float centerY = (getMeasuredHeight() - textHeight) / 2 + Math.abs(fontMetrics.top);

        canvas.drawText(data.getData(), centerX, centerY, backgroundTextPaint);

        if (lineSelected) {

            if (accurate) {

                if (lyricCurrentWordIndex == -1) {
                    lineLyricPlayedWidth = textWidth;
                } else {

                    String[] lyricWords = data.getWords();
                    Integer[] wordDurations = data.getWordDurations();

                    String beforeText = data.getData().substring(0, lyricCurrentWordIndex);
                    float beforeTextWidth = TextUtil.getTextWidth(foregroundTextPaint, beforeText);
                    String currentWord = lyricWords[lyricCurrentWordIndex];
                    float currentWordTextWidth = TextUtil.getTextWidth(foregroundTextPaint, currentWord);

                    float currentWordPlayedWidth = currentWordTextWidth / wordDurations[lyricCurrentWordIndex] * wordPlayedTime;

                    lineLyricPlayedWidth = beforeTextWidth + currentWordPlayedWidth;

                }

                canvas.clipRect(centerX, 0, centerX + lineLyricPlayedWidth, getMeasuredHeight());

            }
            canvas.drawText(data.getData(), centerX, centerY, foregroundTextPaint);
        }

    }

    /**
     * 绘制默认提示文本
     */
    private void drawDefaultText(Canvas canvas) {

        float textWidth = TextUtil.getTextWidth(foregroundTextPaint, HINT_LYRIC_EMPTY);
        float textHeight = TextUtil.getTextHeight(foregroundTextPaint);
        float centerX = getCenterX(textWidth);

        Paint.FontMetrics fontMetrics = foregroundTextPaint.getFontMetrics();

        float centerY = (getHeight() - textHeight) / 2 + Math.abs(fontMetrics.top);

        canvas.drawText(HINT_LYRIC_EMPTY, centerX, centerY, foregroundTextPaint);
    }

    /**
     * 歌词是否为空
     */
    private boolean isEmptyLyric() {
        return data == null;
    }

    /**
     * 获取歌词在水平方向上的中心点
     */
    private float getCenterX(float textWidth) {

        switch (lyricGravity) {
            case GRAVITY_LEFT:
                return 0;
            default:
                return (getMeasuredWidth() - textWidth) / 2;
        }
    }

    /**
     * 设置歌词行
     */
    public void setData(Line data) {
        this.data = data;
        invalidate();
    }

    public Line getData() {
        return data;
    }

    public void setAccurate(boolean accurate) {
        this.accurate = accurate;
    }

    public void setLineSelected(boolean lineSelected) {
        this.lineSelected = lineSelected;
    }

    public void setLyricCurrentWordIndex(int lyricCurrentWordIndex) {
        this.lyricCurrentWordIndex = lyricCurrentWordIndex;
    }

    public void setWordPlayedTime(float wordPlayedTime) {
        this.wordPlayedTime = wordPlayedTime;
    }

    /**
     * 歌词进度
     */
    public void onProgress() {
        if (!isEmptyLyric()) {
            invalidate();
        }
    }


}
