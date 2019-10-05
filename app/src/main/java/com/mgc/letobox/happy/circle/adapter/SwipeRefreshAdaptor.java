package com.mgc.letobox.happy.circle.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by zzh on 2018/3/27.
 */

public    class SwipeRefreshAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    //上拉加载更多
    public static final int  PULLUP_LOAD_MORE=0;
    //正在加载中
    public static final int  LOADING_MORE=1;

    public static final int  NO_MORE_DATA =2;
    //上拉加载更多状态-默认为0
    public int load_more_status=0;

    public static final int TYPE_ITEM =0;  //普通Item View
    public static final int TYPE_FOOTER = 20;  //顶部FootView

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
