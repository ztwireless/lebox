package com.mgc.letobox.happy.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mgc.letobox.happy.R;

public class FloatBubbleView extends FrameLayout {

    private static final String TAG = FloatBubbleView.class.getSimpleName();
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
        ViewConfiguration vc = ViewConfiguration.get(context);
        touchSlop = vc.getScaledTouchSlop();
    }

    private void startAnim() {
        goDown();
    }

    private void goDown() {
        ValueAnimator animator = ValueAnimator.ofFloat(-40, 0).setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float lastValue;
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                if (valueAnimator.getAnimatedFraction() == 0) {
                    lastValue = -40;
                }
                if (!isDragging) {
                    float dy = value - lastValue;
                    setTranslationY(getTranslationY() + dy);
                }
                lastValue = value;
                if (valueAnimator.getAnimatedFraction() == 1) {
                    goUp();
                }
            }
        });
        animator.start();
    }

    private void goUp() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, -40).setDuration(2000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float lastValue;
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                if (valueAnimator.getAnimatedFraction() == 0) {
                    lastValue = 0;
                }
                if (!isDragging) {
                    float dy = value - lastValue;
                    setTranslationY(getTranslationY() + dy);
                }
                lastValue = value;
                if (valueAnimator.getAnimatedFraction() == 1) {
                    goDown();
                }
            }
        });
        animator.start();
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

    private PointF initialPoint = new PointF();
    private float touchSlop;
    private boolean isDragging = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                initialPoint.set(event.getX(), event.getY());
                isDragging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - initialPoint.x;
                float dy = event.getY() - initialPoint.y;
                if (Math.abs(dx) > touchSlop || Math.abs(dy) > touchSlop || Math.sqrt(dx * dx + dy * dy) > touchSlop) {
                    isDragging = true;
                    onDragging(event.getX(), event.getY(), dx, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isDragging) {
                    performClick();
                } else {
                    isDragging = false;
                    startAnim();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private void onDragging(float x, float y, float dx, float dy) {
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }

}
