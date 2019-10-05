package com.mgc.letobox.happy.find.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class ScrollBottomView extends ScrollView {

    private Context mContext;
    private int downX;
    private int downY;
    private int mTouchSlop;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public ScrollBottomView(Context context) {
        super(context);
        mContext = context;
    }

    public ScrollBottomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        View view = (View) getChildAt(getChildCount() - 1);

        int d = view.getBottom();

        d -= (getHeight() + getScrollY());

//        Log.e("---------->","d"+d);
        if (d == 0) {

            if (onScrollBottomListener != null) {
                onScrollBottomListener.onScrollBottom( type);

            }

        }
        if(onScrollChangeListener!=null) {
            onScrollChangeListener.onScrollChanged(l,t,oldl, oldt);
        }
    }
    public interface OnScrollBottomListener {
        void onScrollBottom(int type);
    }
    public OnScrollBottomListener onScrollBottomListener = null;

    public void setOnScrollBottomListener(OnScrollBottomListener onScrollBottomListener) {
        this.onScrollBottomListener = onScrollBottomListener;
    }

    public interface  OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);

    }
    public OnScrollChangedListener onScrollChangeListener = null;

    public void onScrollChangedListener(OnScrollChangedListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

}