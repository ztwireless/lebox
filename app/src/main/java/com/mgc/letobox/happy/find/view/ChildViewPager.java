package com.mgc.letobox.happy.find.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ChildViewPager extends ViewPager {

    private int current;

    //是否可以进行滑动
    private boolean isSlide = false;

    /**
     * 保存position与对于的View
     */
    private HashMap<Integer, Integer> maps = new LinkedHashMap<Integer, Integer>();

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildViewPager(Context context) {
        super(context);
    }

    public int getCurrent() {
        return current;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        // 下面遍历所有child的高度
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            // 采用最大的view的高度
            maps.put(i, h);

        }
        if (getChildCount() > 0) {
            height = getChildAt(current).getMeasuredHeight();

        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void resetHeight(int current) {
//        this.current = current;
//        if (maps.size() > current) {
//
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
//            if (layoutParams == null) {
//                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, maps.get(current));
//            } else {
//                layoutParams.height = maps.get(current);
//            }
//            setLayoutParams(layoutParams);
//        }

        this.current = current;
        if (maps.size()>0 && maps.containsKey(current)) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, maps.get(current));
            } else {
                layoutParams.height = maps.get(current);
            }
            setLayoutParams(layoutParams);

        }
    }

    public void setSlide(boolean slide) {
        isSlide = slide;
    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return isSlide;
//    }


}