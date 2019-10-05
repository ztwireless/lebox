package com.mgc.letobox.happy.circle.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mgc.letobox.happy.R;

/**
 * 底部FootView布局
 */
public class FootViewHolder extends  RecyclerView.ViewHolder{
    public TextView foot_view_item_tv;
    public View view;
    public FootViewHolder(View view) {
        super(view);
        foot_view_item_tv=(TextView)view.findViewById(R.id.foot_view_item_tv);
        this.view =view;
    }
}