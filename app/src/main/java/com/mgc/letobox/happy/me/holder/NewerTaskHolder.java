package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.gson.Gson;
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
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LeBoxUtil;

import java.util.ArrayList;
import java.util.List;


public class NewerTaskHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    RecyclerView _recyclerView;
    TaskAdapter _taskAdapter;

    Context _context;
    List<TaskResultBean> _taskList;

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


        _taskList = new ArrayList<>();
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


        initData();

        _taskAdapter.notifyDataSetChanged();

    }



    private void initData(){

        if(NewerTaskManager.mTaskBeanList!=null){
            for (TaskResultBean taskResultBean: NewerTaskManager.mTaskBeanList){
                taskResultBean.setClassify(LeBoxConstant.LETO_TASK_NEWER);
            }

            if(_taskList!=null && _taskList.size()>0){
                _taskList.clear();
            }

            _taskList.addAll(NewerTaskManager.mTaskBeanList);
            _taskAdapter.notifyDataSetChanged();

        }else{
            getTaskList();

        }
    }

    public void getTaskList(){
        LeBoxUtil.getNewPlayerTasklist(_context, new HttpCallbackDecode< List<TaskResultBean>>(_context, null) {
            @Override
            public void onDataSuccess(final List<TaskResultBean> data) {
                if (null != data) {
                    try {
                        Gson gson = new Gson();

                        final List<TaskResultBean> taskList =  new Gson().fromJson(gson.toJson(data), new TypeToken<List<TaskResultBean>>() {
                        }.getType());

                        if(taskList!=null) {

                            for (TaskResultBean taskResultBean: taskList){
                                taskResultBean.setClassify(LeBoxConstant.LETO_TASK_NEWER);
                            }

                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    if (_taskList != null) {
                                        _taskList.clear();
                                        _taskList.addAll(taskList);
                                    }

                                    NewerTaskManager.addNewTask(_taskList);

                                    _taskAdapter.notifyDataSetChanged();

                                    getUserTaskStatus(_context);
                                }
                            });
                        }

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                try {
                    ToastUtil.s(_context, msg);
                }catch (Exception e){

                }

            }

            @Override
            public void onFinish() {

            }
        });

    }


    public void getUserTaskStatus(Context context ){

        LeBoxUtil.getUserNewPlayerTasklist(context, new HttpCallbackDecode< List<UserTaskStatusResultBean>>(context, null) {
            @Override
            public void onDataSuccess(final List<UserTaskStatusResultBean> data) {
                if (null != data) {
                    try {
                        Gson gson = new Gson();

                        Log.d("NewerTaskView", "getUserTaskStatus" + gson.toJson(data));

                        final List<UserTaskStatusResultBean> taskList =  new Gson().fromJson(gson.toJson(data), new TypeToken<List<UserTaskStatusResultBean>>() {
                        }.getType());

                        for (UserTaskStatusResultBean taskStatusRequestBean: taskList ){

                            for (TaskResultBean taskResultBean: _taskList){

                                if(taskResultBean.getChannel_task_id() == taskStatusRequestBean.getChannel_task_id()){
                                    taskResultBean.setProcess( taskStatusRequestBean.getTask_progress() );
                                    break;
                                }

                            }
                        }
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {

                                _taskAdapter.notifyDataSetChanged();

                                getUserTaskStatus(_context);
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }


}