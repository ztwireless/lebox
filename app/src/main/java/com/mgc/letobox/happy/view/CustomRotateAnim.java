package com.mgc.letobox.happy.view;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Create by zhaozhihui on 2020/12/8
 **/
public class CustomRotateAnim extends Animation {
    /** 控件宽 */
    private int mWidth;

    /** 控件高 */
    private int mHeight;

    /** 实例 */
    private static CustomRotateAnim rotateAnim;

    /**
     * 获取动画实例
     * @return 实例
     */
    public static CustomRotateAnim getCustomRotateAnim() {
        if (null == rotateAnim) {
            rotateAnim = new CustomRotateAnim();
        }
        return rotateAnim;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        this.mWidth = width;
        this.mHeight = height;
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // 左右摇摆
        t.getMatrix().setRotate((float)(Math.sin(interpolatedTime*Math.PI*2)*20), mWidth/2, mHeight/2);
        super.applyTransformation(interpolatedTime, t);
    }
}
