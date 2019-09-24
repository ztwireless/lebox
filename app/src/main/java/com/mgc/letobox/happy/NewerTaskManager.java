package com.mgc.letobox.happy;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ledong.lib.leto.mgc.NewerTaskBean;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.me.bean.TaskResultBean;
import com.mgc.letobox.happy.me.bean.UserTaskStatusRequestBean;
import com.mgc.letobox.happy.me.bean.UserTaskStatusResultBean;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LeBoxUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-11
 **/
public class NewerTaskManager {

    private final static String TAG = "NewerTaskManager";

    public static List<TaskResultBean> mTaskBeanList = new ArrayList<>();

    public static void getTaskList(final Context context) {
        LeBoxUtil.getNewPlayerTasklist(context, new HttpCallbackDecode<List<TaskResultBean>>(context, null) {
            @Override
            public void onDataSuccess(final List<TaskResultBean> data) {
                if (null != data) {
                    try {
                        Gson gson = new Gson();

                        final List<TaskResultBean> taskList = new Gson().fromJson(gson.toJson(data), new TypeToken<List<TaskResultBean>>() {
                        }.getType());
                        if (taskList != null) {

                            if (mTaskBeanList != null) {
                                mTaskBeanList.clear();
                            }

                            mTaskBeanList.addAll(taskList);
                        }

//                        addNewTask(mTaskBeanList);

                        getUserTaskStatus(context);

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

    public static void getUserTaskStatus(Context context) {


        LeBoxUtil.getUserNewPlayerTasklist(context, new HttpCallbackDecode<List<UserTaskStatusResultBean>>(context, null) {
            @Override
            public void onDataSuccess(final List<UserTaskStatusResultBean> data) {
                if (null != data) {
                    try {
                        Gson gson = new Gson();

                        Log.d(TAG, "getUserTaskStatus" + gson.toJson(data));

                        final List<UserTaskStatusResultBean> taskList = new Gson().fromJson(gson.toJson(data), new TypeToken<List<UserTaskStatusResultBean>>() {
                        }.getType());

                        for (UserTaskStatusResultBean taskStatusRequestBean : taskList) {

                            for (TaskResultBean taskResultBean : mTaskBeanList) {

                                if (taskResultBean.getChannel_task_id() == taskStatusRequestBean.getChannel_task_id()) {
                                    taskResultBean.setProcess(taskStatusRequestBean.getTask_progress());
                                    break;
                                }

                            }
                        }

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


    public static ArrayList getCompleteNewerTaskId(Context context, String gameId, long time) {
        ArrayList<TaskResultBean> taskId = new ArrayList();
        try {

            for (TaskResultBean newerTaskBean : mTaskBeanList) {

                if (newerTaskBean.getProcess() < newerTaskBean.getFinish_level()) {
                    if (newerTaskBean.getFinish_type() == 2) {  //累计时长的任务
                        long process = newerTaskBean.getProcess() + time;
                        newerTaskBean.setProcess(process);
                        if (process >= newerTaskBean.getFinish_level()) {
                            newerTaskBean.setStatus(1);

                            taskId.add(newerTaskBean);
                        }

                        reportTaskProgress(context, newerTaskBean.getChannel_task_id(), time);

                    } else if (newerTaskBean.getFinish_type() == 4) {     //累计单个游戏 时长的任务
                        if (5 * 60 * 1000 <= time) {
                            long process = newerTaskBean.getProcess() + 1;
                            newerTaskBean.setProcess(process);
                            if (process >= newerTaskBean.getFinish_level()) {
                                taskId.add(newerTaskBean);
                                newerTaskBean.setStatus(1);
                            }
                            reportTaskProgress(context, newerTaskBean.getChannel_task_id(), 1);
                        }
                    }
                }
            }
            return taskId;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return taskId;
    }


    public static void reportTaskProgress(Context context, int taskId, long progress) {
        LeBoxUtil.reportUserNewPlayerTasklist(context, taskId, progress, new HttpCallbackDecode<List<UserTaskStatusResultBean>>(context, null) {
            @Override
            public void onDataSuccess(final List<UserTaskStatusResultBean> data) {
                if (null != data) {

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

    public static void addDailyTasks(List<TaskResultBean> taskList) {

        TaskResultBean taskResultBean1 = new TaskResultBean();
        taskResultBean1.setAward_coins(666666);
        taskResultBean1.setTask_title("玩游戏计时奖励");
        taskResultBean1.setTask_desc("玩的越多，累计获得金币越多");
        taskResultBean1.setChannel_task_id(1);
        taskResultBean1.setClassify(2);

        TaskResultBean taskResultBean2 = new TaskResultBean();
        taskResultBean2.setAward_coins(666666);
        taskResultBean2.setTask_title("玩“挑战”游戏计时奖励");
        taskResultBean2.setTask_desc("玩的越多，累计获得金币越多");
        taskResultBean2.setChannel_task_id(2);
        taskResultBean2.setClassify(2);

        taskList.clear();
        taskList.add(taskResultBean1);
        taskList.add(taskResultBean2);
    }
}
