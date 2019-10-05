package com.mgc.letobox.happy.view;

import android.content.Context;
import android.view.View;

import com.mgc.letobox.happy.R;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

/**
 * Created by DELL on 2018/8/4.
 */

public class ColorBar implements ScrollBar {

    protected Gravity gravity;
    protected View view;
    protected int height;
    protected int width;

    public ColorBar(Context context, int width , int height) {
        this(context,width ,height, Gravity.BOTTOM);
    }

    public ColorBar(Context context, int width, int height, Gravity gravity) {
        view = new View(context);
        this.height = height;
        this.width = width;
        view.setBackgroundResource(R.mipmap.circle_line);
        this.gravity = gravity;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getHeight(int tabHeight) {
        if (height == 0) {
            return tabHeight;
        }
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getWidth(int tabWidth) {
        if (width == 0) {
            return tabWidth;
        }
        return width;
    }

    @Override
    public View getSlideView() {
        return view;
    }

    @Override
    public Gravity getGravity() {
        return gravity;
    }

    public void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

}
