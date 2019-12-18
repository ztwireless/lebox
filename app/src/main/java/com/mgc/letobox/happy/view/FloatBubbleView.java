package com.mgc.letobox.happy.view;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mgc.letobox.happy.R;

public class FloatBubbleView extends FrameLayout {

    private static int ID = 0;

    private int mBubbleId;
    private int mCoinCount;
    private TextView mCoinCountView;

    public FloatBubbleView(@NonNull Context context) {
        this(context, null);
    }

    public FloatBubbleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatBubbleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBubbleId(ID++);
        inflate(context, R.layout.layout_bubble, this);
        mCoinCountView = findViewById(R.id.itemCoinCount);
        post(new Runnable() {
            @Override
            public void run() {
                startAnim();
            }
        });
    }

    private void startAnim() {
        goDown();
    }

    private void goDown() {
        animate().translationYBy(40).setDuration(2000).setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        goUp();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                })
                .start();
    }

    private void goUp() {
        animate().translationYBy(-40).setDuration(2000).setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        goDown();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                })
                .start();
    }

    public int getCoinCount() {
        return mCoinCount;
    }

    public void setCoinCount(int coinCount) {
        this.mCoinCount = coinCount;
        mCoinCountView.setText(String.format("+%d", mCoinCount));
    }

    public int getBubbleId() {
        return mBubbleId;
    }

    private void setBubbleId(int mId) {
        this.mBubbleId = mId;
    }
}
