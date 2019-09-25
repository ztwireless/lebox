package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.ColorUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.MResource;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.recycleview.RecycleViewDivider;
import com.mgc.letobox.happy.NewerTaskManager;
import com.mgc.letobox.happy.me.adapter.TaskAdapter;
import com.mgc.letobox.happy.me.bean.MeModuleBean;
import com.mgc.letobox.happy.me.bean.TaskResultBean;
import com.mgc.letobox.happy.me.bean.UserTaskStatusResultBean;
import com.mgc.letobox.happy.util.LeBoxUtil;

import java.util.List;


public class NewerTaskHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    RecyclerView _recyclerView;
    TaskAdapter _taskAdapter;

    Context _context;

    ViewGroup _adContainer;


    public static NewerTaskHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_newer_task"), parent, false);
        return new NewerTaskHolder(ctx, view);
    }

    public NewerTaskHolder(Context context, View view) {
        super(view);

        _context = context;
        _splitSpace = itemView.findViewById(MResource.getIdByName(context, "R.id.split_space"));

        this._recyclerView = view.findViewById(MResource.getIdByName(context, "R.id.recyclerView"));


        _taskAdapter = new TaskAdapter(_context, NewerTaskManager.mTaskBeanList);

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
        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        initData();

        _taskAdapter.notifyDataSetChanged();
    }

    private void initData(){
        if(NewerTaskManager.mTaskBeanList.isEmpty()) {
            getTaskList();
        } else {
            getUserTaskStatus();
        }
    }

    public void getTaskList(){
        NewerTaskManager.getTaskList(_context, new HttpCallbackDecode< List<TaskResultBean>>(_context, null) {
            @Override
            public void onDataSuccess(List<TaskResultBean> data) {
                _taskAdapter.notifyDataSetChanged();
                getUserTaskStatus();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.s(_context, msg);
            }
        });
    }

    public void getUserTaskStatus() {
        LeBoxUtil.getUserNewPlayerTasklist(_context, new HttpCallbackDecode< List<UserTaskStatusResultBean>>(_context, null, new TypeToken<List<UserTaskStatusResultBean>>(){}.getType()) {
            @Override
            public void onDataSuccess(final List<UserTaskStatusResultBean> data) {
                if (null != data) {
                    _taskAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}