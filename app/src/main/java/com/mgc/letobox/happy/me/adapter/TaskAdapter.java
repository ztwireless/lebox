package com.mgc.letobox.happy.me.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.mgc.letobox.happy.me.bean.SigninBean;
import com.mgc.letobox.happy.me.bean.TaskResultBean;
import com.mgc.letobox.happy.me.holder.TaskHolder;

import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

    Context _context;

    List<TaskResultBean> _list;

    public TaskAdapter(Context context, List<TaskResultBean> dataList) {
        _context = context;

        _list = dataList;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return TaskHolder.create(_context, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        holder.onBind(_list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return _list == null ? 0 : _list.size();
    }
}
