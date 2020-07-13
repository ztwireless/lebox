package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.DensityUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.leto.game.base.view.recycleview.RecycleViewDivider;
import com.mgc.letobox.happy.NewerTaskManager;
import com.mgc.letobox.happy.me.adapter.TaskAdapter;
import com.mgc.letobox.happy.me.bean.MeModuleBean;
import com.mgc.letobox.happy.me.bean.TaskResultBean;

import java.util.ArrayList;
import java.util.List;


public class DailyTaskHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    RecyclerView _recyclerView;
    TaskAdapter _taskAdapter;

    Context _context;
    List<TaskResultBean> _taskList;

    ViewGroup _adContainer;


    public static DailyTaskHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_daily_task"), parent, false);
        return new DailyTaskHolder(ctx, view);
    }

    public DailyTaskHolder(Context context, View view) {
        super(view);

        _context = context;
        this._recyclerView = view.findViewById(MResource.getIdByName(context, "R.id.recyclerView"));
        _splitSpace = itemView.findViewById(MResource.getIdByName(context, "R.id.split_space"));

        _taskList = new ArrayList<>();

        NewerTaskManager.addDailyTasks(_taskList);

        _taskAdapter = new TaskAdapter(_context, _taskList);

        // setup views
        _recyclerView.setLayoutManager(new LinearLayoutManager(context));
        _recyclerView.setAdapter(_taskAdapter);

        _recyclerView.addItemDecoration(new RecycleViewDivider(
                context, LinearLayoutManager.HORIZONTAL, DensityUtil.dip2px(context,1), ColorUtil.parseColor("#f3f3f3")));

        _recyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public void onBind(final MeModuleBean signin, final int position) {
        // name & desc
        final Context ctx = itemView.getContext();

        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);


        _taskAdapter.notifyDataSetChanged();
    }
}