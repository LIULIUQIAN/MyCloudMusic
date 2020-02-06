package com.example.mycloudmusic.util;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.animation.AccelerateInterpolator;

public class SwitchDrawableUtil {

    /**
     * 背景（原来的图片）索引
     */
    private static final int INDEX_BACKGROUND = 0;

    /**
     * 前景（新图片）索引
     */
    private static final int INDEX_FOREGROUND = 1;

    /**
     * 动画执行时间
     * 单位：毫秒
     */
    private static final int DURATION_ANIMATION = 300;
    private final LayerDrawable layerDrawable;
    private ValueAnimator animator;

    public SwitchDrawableUtil(Drawable backgroundDrawable, Drawable foregroundDrawable) {

        Drawable[] drawables = new Drawable[2];
        if (backgroundDrawable == null) {
            drawables[INDEX_BACKGROUND] = foregroundDrawable;
            drawables[INDEX_FOREGROUND] = foregroundDrawable;
        } else {
            drawables[INDEX_BACKGROUND] = backgroundDrawable;
            drawables[INDEX_FOREGROUND] = foregroundDrawable;
        }

        layerDrawable = new LayerDrawable(drawables);

        initAnimation();
    }

    private void initAnimation() {
        animator = ValueAnimator.ofFloat(0f, 255f);
        animator.setDuration(DURATION_ANIMATION);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float foregroundAlpha = (float) animation.getAnimatedValue();
                layerDrawable.getDrawable(INDEX_FOREGROUND).setAlpha((int) foregroundAlpha);
            }
        });
    }

    public Drawable getDrawable() {
        return layerDrawable;
    }

    public void start() {
        animator.start();
    }
}
