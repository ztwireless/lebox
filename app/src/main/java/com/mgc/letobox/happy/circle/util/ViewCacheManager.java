package com.mgc.letobox.happy.circle.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by DELL on 2018/6/28.
 */

public class ViewCacheManager<ReturnView extends ViewGroup> {

    private onCacheListener mOnCacheListener;

    public onCacheListener getOnCacheListener() {
        return mOnCacheListener;
    }

    public ViewCacheManager setOnCacheListener(onCacheListener mOnCacheListener) {
        this.mOnCacheListener = mOnCacheListener;
        return this;
    }

    /**
     * 设置父布局，进行管理，并刷新数据
     *
     * @param mViewGroup
     * @param mFixedValue 期望view数量
     */
    public void onRefresh(ReturnView mViewGroup, int mFixedValue) {
        int mChangeValue;
        if (mFixedValue > mViewGroup.getChildCount()) {
            mChangeValue = mFixedValue - mViewGroup.getChildCount();
            for (int mI = 0; mI < mChangeValue; mI++) {
                mViewGroup.addView(mOnCacheListener.onAddView(mViewGroup.getChildCount()));
            }
        } else {
            mChangeValue = mViewGroup.getChildCount() - mFixedValue;
            for (int mValue = mChangeValue; mValue > 0; mValue--) {
                mViewGroup.removeViewAt(mValue);
                mOnCacheListener.onDelete(mViewGroup.getChildCount());
            }
        }
        //refresh
        int count = mViewGroup.getChildCount();
        for (int mI = 0; mI < count; mI++) {
            mOnCacheListener.onBindView(mI, mViewGroup.getChildAt(mI));
        }
    }


    public interface onCacheListener<ReturnView> {
        /**
         * 添加子view
         * add childview
         *
         * @param position view position
         * @return childview
         */
        public View onAddView(int position);

        /**
         * 删除View
         * remove childview
         *
         * @param position view position
         */
        public void onDelete(int position);

        /**
         * 刷新后对view做操作
         *
         * @param position
         * @param mView
         */
        public void onBindView(int position, ReturnView mView);
    }

}
