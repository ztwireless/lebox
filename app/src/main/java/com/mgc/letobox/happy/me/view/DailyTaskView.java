package com.mgc.letobox.happy.me.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.leto.game.base.util.ColorUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.view.recycleview.RecycleViewDivider;
import com.mgc.letobox.happy.NewerTaskManager;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.me.adapter.TaskAdapter;
import com.mgc.letobox.happy.me.bean.TaskResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
public class DailyTaskView extends LinearLayout {

    RecyclerView _recyclerView;

    TaskAdapter _taskAdapter;

    Context _context;

    List<TaskResultBean> _taskList;

    public DailyTaskView(Context context) {
        super(context);
        _context = context;
        initUI(context);
    }
    public DailyTaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        initUI(context);
    }

    public void initUI(Context context) {
        inflate(context, R.layout.leto_mgc_me_daily_task, this);
        _recyclerView = findViewById(R.id.recyclerView);

        _taskList = new ArrayList<>();

        NewerTaskManager.addDailyTasks(_taskList);

        _taskAdapter = new TaskAdapter(context, _taskList);

        // setup views
        _recyclerView.setLayoutManager(new LinearLayoutManager(context));

        _recyclerView.addItemDecoration(new RecycleViewDivider(
                context, LinearLayoutManager.HORIZONTAL, DensityUtil.dip2px(context,1), ColorUtil.parseColor("#f3f3f3")));

        _recyclerView.setAdapter(_taskAdapter);
        _recyclerView.setNestedScrollingEnabled(false);

    }
}
