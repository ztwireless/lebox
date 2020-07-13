package com.mgc.letobox.happy.me.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.leto.game.base.event.DataRefreshEvent;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.DensityUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.leto.game.base.view.recycleview.RecycleViewDivider;
import com.mgc.letobox.happy.NewerTaskManager;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.me.adapter.TaskAdapter;
import com.mgc.letobox.happy.me.bean.TaskResultBean;
import com.mgc.letobox.happy.me.bean.UserTaskStatusResultBean;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LeBoxUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
public class NewerTaskView extends LinearLayout {

    RecyclerView _recyclerView;

    TaskAdapter _taskAdapter;

    Context _context;

    List<TaskResultBean> _taskList;

    public NewerTaskView(Context context) {
        super(context);
        _context = context;
        initUI(context);
    }
    public NewerTaskView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        initUI(context);
    }

    public void initUI(Context context) {
        inflate(context, R.layout.leto_mgc_me_newer_task, this);
        _recyclerView = findViewById(R.id.recyclerView);


        _taskList = new ArrayList<>();
        _taskAdapter = new TaskAdapter(context, _taskList);

        // setup views
        _recyclerView.setLayoutManager(new LinearLayoutManager(context));
        _recyclerView.setAdapter(_taskAdapter);

        _recyclerView.addItemDecoration(new RecycleViewDivider(
                context, LinearLayoutManager.HORIZONTAL, DensityUtil.dip2px(context,1), ColorUtil.parseColor("#f3f3f3")));

        _recyclerView.setNestedScrollingEnabled(false);

        initData();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //重新显示到窗口，更新ui
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDataRefresh(DataRefreshEvent event) {

        Log.i("NewerTaskView", "onDataRefresh");

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                _taskList.clear();
                _taskList.addAll(NewerTaskManager.mTaskBeanList);
                _taskAdapter.notifyDataSetChanged();
            }
        });


    }


    private void initData(){

        if(NewerTaskManager.mTaskBeanList!=null){
            for (TaskResultBean taskResultBean: NewerTaskManager.mTaskBeanList){
                taskResultBean.setClassify(LeBoxConstant.LETO_TASK_NEWER);
            }
            _taskList.addAll(NewerTaskManager.mTaskBeanList);
            _taskAdapter.notifyDataSetChanged();

        }else{

            getTaskList();

        }


    }

    public void getTaskList(){
        LeBoxUtil.getNewPlayerTasklist(_context, new HttpCallbackDecode< List<TaskResultBean>>(getContext(), null) {
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
                                        _taskList.addAll(taskList);
                                    }

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
