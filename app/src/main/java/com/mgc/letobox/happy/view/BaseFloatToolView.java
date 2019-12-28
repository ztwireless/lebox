package com.mgc.letobox.happy.view;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.OverScroller;

import com.mgc.letobox.happy.R;

public class BaseFloatToolView extends FrameLayout {
    private static final String TAG = BaseFloatToolView.class.getSimpleName();

    public BaseFloatToolView(@NonNull Context context) {
        super(context);
        View v = inflate(context, getLayoutId(), this);
        onInit(context, v);
    }

    protected int getLayoutId() {
        return 0;
    }

    private RectF edgeRatio = new RectF();
    public void setEdgeRatio(RectF edgeRatio) {
        edgeRatio.set(edgeRatio);
    }
    public void setEdgeRatio(float left, float top, float right, float bottom) {
        edgeRatio.set(left, top, right, bottom);
    }

    protected void onInit(Context context, View v) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        touchSlop = vc.getScaledTouchSlop();
        scroller = new OverScroller(context);
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
                if (Math.abs(dx) > touchSlop || Math.abs(dy) > touchSlop || Math.sqrt(dx * dx + dy + dy) > touchSlop) {
                    isDragging = true;
                    onDragging(event.getX(), event.getY(), dx, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isDragging) {
                    performClick();
                } else {
                    isDragging = false;
                    settleToEdge();
                }
                break;
        }
        return true;
    }

    private OverScroller scroller;
    private void settleToEdge() {
        float finalLeft;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        if (getX() < getResources().getDisplayMetrics().widthPixels / 2f) {
            finalLeft = screenWidth * edgeRatio.left;
        } else {
            finalLeft = screenWidth - screenWidth * edgeRatio.right - getWidth();
        }
        scroller.startScroll((int) getX(), (int) getY(), (int) (finalLeft - getX()), 0, 800);
        invalidate();
    }

    private void onDragging(float x, float y, float dx, float dy) {
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            setX(scroller.getCurrX());
            setY(scroller.getCurrY());
            invalidate();
        }
    }
}
