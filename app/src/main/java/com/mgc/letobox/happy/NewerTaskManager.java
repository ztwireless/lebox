package com.mgc.letobox.happy;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.mgc.RewardVideoManager;
import com.mgc.leto.game.base.mgc.bean.BenefitSettings_video_task;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.letobox.happy.me.bean.TaskResultBean;
import com.mgc.letobox.happy.me.bean.UserTaskStatusResultBean;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LeBoxUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-11
 **/
public class NewerTaskManager {

    private final static String TAG = "NewerTaskManager";

    public static List<TaskResultBean> mNewPlayerTaskBeanList = new ArrayList<>();
    public static List<TaskResultBean> mDailyTaskBeanList = new ArrayList<>();

    public static void clearTask() {
        LetoTrace.d(TAG, "clear task list");

        if (mNewPlayerTaskBeanList != null && mNewPlayerTaskBeanList.size() > 0) {
            for (TaskResultBean taskResultBean : mNewPlayerTaskBeanList) {
                taskResultBean.setStatus(0);
                taskResultBean.setProcess(0);
            }
        }

        if (mDailyTaskBeanList != null && mDailyTaskBeanList.size() > 0) {
            for (TaskResultBean taskResultBean : mDailyTaskBeanList) {
                taskResultBean.setStatus(0);
                taskResultBean.setProcess(0);
            }
        }
    }

    public static void getNewPlayerTaskList(final Context context, final HttpCallbackDecode callback) {

        getNewPlayerTaskList(context, true, callback);
    }

    public static void getNewPlayerTaskList(final Context context, boolean getUserStatus, final HttpCallbackDecode callback) {
        LetoTrace.d(TAG, "getNewPlayerTaskList...");
        LeBoxUtil.getNewPlayerTasklist(context, new HttpCallbackDecode<List<TaskResultBean>>(context, null, new TypeToken<List<TaskResultBean>>() {
        }.getType()) {
            @Override
            public void onDataSuccess(final List<TaskResultBean> data) {
                if (null != data) {
                    try {
                        for (TaskResultBean taskResultBean : data) {
                            taskResultBean.setClassify(LeBoxConstant.LETO_TASK_NEWER);
                            if (taskResultBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_VIEW_VIDEO_NEW) {
                                //无需上报看视频任务
                                BenefitSettings_video_task.VideoReward liveDayVideoReward = RewardVideoManager.getLiveDayVideoReward(context);
                                int videoNumber = liveDayVideoReward.getVideo_num_max() - liveDayVideoReward.getVideo_num_min();

                                long todayViewVideoNumber = RewardVideoManager.getRewardedVideoNumber(context);
                                long progress = todayViewVideoNumber - liveDayVideoReward.getVideo_num_min();
                                if (liveDayVideoReward.isEnd()) {
                                    videoNumber = 30;
                                    progress = (todayViewVideoNumber - liveDayVideoReward.getVideo_num_min()) % 30;
                                }

//                                String title = String.format("看%d次视频，领0.3元", videoNumber);
//                                String message = String.format("进入任意小游戏中领取%d个游戏红包，提现0.3元", videoNumber);
                                String message = "";
                                String title = String.format("进入任意小游戏中领取%d个游戏红包，提现0.3元", videoNumber);

                                taskResultBean.setTask_title(title);
                                taskResultBean.setTask_desc(message);
                                taskResultBean.setFinish_level(videoNumber);
                                taskResultBean.setProcess(progress);
                                taskResultBean.setAward_coins(liveDayVideoReward.getReward_coins());

                            }
                        }
                        mNewPlayerTaskBeanList.clear();
                        mNewPlayerTaskBeanList.addAll(data);

                        if (getUserStatus) {
                            getUserNewPlayerTaskStatus(context, null);
                        }
                        // forward
                        if (callback != null) {
                            callback.onDataSuccess(data);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onFailure("500", e.getLocalizedMessage());
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure("500",
                                context.getString(MResource.getIdByName(context, "R.string.lebox_get_task_list_failed")));
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (callback != null) {
                    callback.onFailure(code, msg);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (callback != null) {
                    callback.onFinish();
                }
            }
        });
    }

    public static void getUserNewPlayerTaskStatus(final Context context, final HttpCallbackDecode callback) {
        LetoTrace.d(TAG, "getUserNewPlayerTaskStatus...");
        LeBoxUtil.getUserNewPlayerTasklist(context, new HttpCallbackDecode<List<UserTaskStatusResultBean>>(context, null, new TypeToken<List<UserTaskStatusResultBean>>() {
        }.getType()) {
            @Override
            public void onDataSuccess(final List<UserTaskStatusResultBean> data) {
                if (null != data) {
                    try {
                        for (UserTaskStatusResultBean taskStatusRequestBean : data) {
                            for (TaskResultBean taskResultBean : mNewPlayerTaskBeanList) {
                                if (taskResultBean.getChannel_task_id() == taskStatusRequestBean.getChannel_task_id()) {
                                    taskResultBean.setProcess(taskStatusRequestBean.getTask_progress());
                                    taskResultBean.setStatus(taskStatusRequestBean.getTask_status());

                                    if (taskResultBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_VIEW_VIDEO_NEW) {
                                        //无需上报看视频任务
                                        BenefitSettings_video_task.VideoReward liveDayVideoReward = RewardVideoManager.getLiveDayVideoReward(context);
                                        int videoNumber = liveDayVideoReward.getVideo_num_max() - liveDayVideoReward.getVideo_num_min();

                                        long todayViewVideoNumber = RewardVideoManager.getRewardedVideoNumber(context);
                                        long progress = todayViewVideoNumber - liveDayVideoReward.getVideo_num_min();
                                        if (liveDayVideoReward.isEnd()) {
                                            videoNumber = 30;
                                            progress = (todayViewVideoNumber - liveDayVideoReward.getVideo_num_min()) % 30;
                                        }

//                                        String title = String.format("看%d次视频，领0.3元", videoNumber);
//                                        String message = String.format("进入任意小游戏中领取%d个游戏红包，提现0.3元", videoNumber);
                                        String message = "";
                                        String title = String.format("进入任意小游戏中领取%d个游戏红包，提现0.3元", videoNumber);

                                        taskResultBean.setTask_title(title);
                                        taskResultBean.setTask_desc(message);
                                        taskResultBean.setFinish_level(videoNumber);
                                        taskResultBean.setProcess(progress);
                                        taskResultBean.setAward_coins(liveDayVideoReward.getReward_coins());

                                    }

                                    break;
                                }
                            }
                        }
                        if (callback != null) {
                            callback.onDataSuccess(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onFailure("500", e.getLocalizedMessage());
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure("500",
                                context.getString(MResource.getIdByName(context, "R.string.lebox_get_task_status_failed")));
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (callback != null) {
                    callback.onFailure(code, msg);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (callback != null) {
                    callback.onFinish();
                }
            }
        });
    }


    public static ArrayList getCompleteNewerTaskId(Context context, String gameId, long time) {
        ArrayList<TaskResultBean> taskId = new ArrayList();
        try {

            for (TaskResultBean newerTaskBean : mNewPlayerTaskBeanList) {

                if (newerTaskBean.getProcess() < newerTaskBean.getFinish_level()) {
                    if (newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_PLAY_GAME_DURATION) {  //累计时长的任务
                        long process = newerTaskBean.getProcess() + time;
                        newerTaskBean.setProcess(process);
                        if (process >= newerTaskBean.getFinish_level()) {
                            if (newerTaskBean.getStatus() == 0) {
                                newerTaskBean.setStatus(1);
                                taskId.add(newerTaskBean);
                            }
                        }

                        reportTaskProgress(context, newerTaskBean.getChannel_task_id(), time);

                    } else if (newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_PLAY_GAME_TIME) {     //累计单个游戏 时长的任务
                        if (5 * 60 * 1000 <= time) {
                            long process = newerTaskBean.getProcess() + 1;
                            newerTaskBean.setProcess(process);
                            if (process >= newerTaskBean.getFinish_level()) {
                                if (newerTaskBean.getStatus() == 0) {
                                    newerTaskBean.setStatus(1);
                                    taskId.add(newerTaskBean);
                                }
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


    public static ArrayList getCompleteDailyTaskIdOnPlayGame(Context context, String gameId, long time) {
        ArrayList<TaskResultBean> taskId = new ArrayList();
        try {

            for (TaskResultBean newerTaskBean : mDailyTaskBeanList) {

                if (newerTaskBean.getProcess() < newerTaskBean.getFinish_level()) {
                    if (newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_PLAY_GAME_DURATION) {  //累计时长的任务
                        long process = newerTaskBean.getProcess() + time;
                        newerTaskBean.setProcess(process);
                        if (process >= newerTaskBean.getFinish_level()) {
                            if (newerTaskBean.getStatus() == 0) {
                                newerTaskBean.setStatus(1);
                                taskId.add(newerTaskBean);
                            }
                        }

                        reportTaskProgress(context, newerTaskBean.getChannel_task_id(), time);

                    } else if (newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_PLAY_GAME_TIME) {     //累计单个游戏 时长的任务
                        if (5 * 60 * 1000 <= time) {
                            long process = newerTaskBean.getProcess() + 1;
                            newerTaskBean.setProcess(process);
                            if (process >= newerTaskBean.getFinish_level()) {
                                if (newerTaskBean.getStatus() == 0) {
                                    newerTaskBean.setStatus(1);
                                    taskId.add(newerTaskBean);
                                }
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

    public static ArrayList getCompleteDailyTaskId(Context context, int taskType, String gameId, long time) {
        ArrayList<TaskResultBean> taskId = new ArrayList();
        try {

            List<TaskResultBean> taskList = new ArrayList<>();
            taskList.addAll(mNewPlayerTaskBeanList);
            taskList.addAll(mDailyTaskBeanList);

            for (TaskResultBean newerTaskBean : taskList) {
                if (taskType == newerTaskBean.getFinish_type()) {
                    if (newerTaskBean.getProcess() < newerTaskBean.getFinish_level()) {
                        if (newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_PLAY_GAME_DURATION) {  //累计时长的任务
                            long process = newerTaskBean.getProcess() + time;
                            newerTaskBean.setProcess(process);
                            if (process >= newerTaskBean.getFinish_level()) {
                                if (newerTaskBean.getStatus() == 0) {
                                    newerTaskBean.setStatus(1);
                                    taskId.add(newerTaskBean);
                                }
                            }

                            reportTaskProgress(context, newerTaskBean.getChannel_task_id(), time);

                        } else if (newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_PLAY_GAME_TIME) {     //累计单个游戏 时长的任务
                            if (5 * 60 * 1000 <= time) {
                                long process = newerTaskBean.getProcess() + 1;
                                newerTaskBean.setProcess(process);
                                if (process >= newerTaskBean.getFinish_level()) {
                                    if (newerTaskBean.getStatus() == 0) {
                                        newerTaskBean.setStatus(1);
                                        taskId.add(newerTaskBean);
                                    }
                                }
                                reportTaskProgress(context, newerTaskBean.getChannel_task_id(), 1);
                            }
                        } else if (newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_SCRATCH_CARD ||       //计算次数任务
                                newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_ANSWER ||
                                newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_IDIOM ||
                                newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_TURNTABLE ||
                                newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_VIEW_VIDEO ||
                                newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_BIND_PHONE ||
                                newerTaskBean.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_BIND_INVITE
                        ) {
                            long process = newerTaskBean.getProcess() + time;
                            newerTaskBean.setProcess(process);
                            if (process >= newerTaskBean.getFinish_level()) {
                                if (newerTaskBean.getStatus() == 0) {
                                    newerTaskBean.setStatus(1);
                                    taskId.add(newerTaskBean);
                                }
                            }

                            reportTaskProgress(context, newerTaskBean.getChannel_task_id(), time);

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
        if (context == null) {
            return;
        }
        LeBoxUtil.reportUserTasklist(context, taskId, progress, new HttpCallbackDecode<List<UserTaskStatusResultBean>>(context, null) {
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

    public static void getDailyTaskList(final Context context, final HttpCallbackDecode callback) {
        getDailyTaskList(context, true, callback);
    }

    public static void getDailyTaskList(final Context context, boolean getUserStatus, final HttpCallbackDecode callback) {
        LetoTrace.d(TAG, "getDailyTaskList...");
        LeBoxUtil.getDailyTasklist(context, new HttpCallbackDecode<List<TaskResultBean>>(context, null, new TypeToken<List<TaskResultBean>>() {
        }.getType()) {
            @Override
            public void onDataSuccess(final List<TaskResultBean> data) {
                if (null != data) {
                    try {
                        for (TaskResultBean taskResultBean : data) {
                            taskResultBean.setClassify(LeBoxConstant.LETO_TASK_DAILY);
                        }
                        mDailyTaskBeanList.clear();
                        mDailyTaskBeanList.addAll(data);

                        if (getUserStatus) {
                            getUserDailyTaskStatus(context, null);
                        }

                        // forward
                        if (callback != null) {
                            callback.onDataSuccess(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onFailure("500", e.getLocalizedMessage());
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onFailure("500",
                                context.getString(MResource.getIdByName(context, "R.string.lebox_get_task_list_failed")));
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (callback != null) {
                    callback.onFailure(code, msg);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (callback != null) {
                    callback.onFinish();
                }
            }
        });
    }

    public static void getUserDailyTaskStatus(final Context context, final HttpCallbackDecode callback) {
        LetoTrace.d(TAG, "getUserDailyTaskStatus...");
        LeBoxUtil.getUserDailyTasklist(context, new HttpCallbackDecode<List<UserTaskStatusResultBean>>(context, null, new TypeToken<List<UserTaskStatusResultBean>>() {
        }.getType()) {
            @Override
            public void onDataSuccess(final List<UserTaskStatusResultBean> data) {
                if (null != data) {
                    try {
                        for (UserTaskStatusResultBean taskStatusRequestBean : data) {
                            for (TaskResultBean taskResultBean : mDailyTaskBeanList) {
                                if (taskResultBean.getChannel_task_id() == taskStatusRequestBean.getChannel_task_id()) {
                                    taskResultBean.setStatus(taskStatusRequestBean.getTask_status());
                                    taskResultBean.setProcess(taskStatusRequestBean.getTask_progress());
                                    break;
                                }
                            }
                        }
                        if (callback != null) {
                            callback.onDataSuccess(data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (callback != null) {
                            callback.onFailure("500", e.getLocalizedMessage());
                        }
                    }
                } else {
                    if (callback != null) {
                        callback.onDataSuccess(data);
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (callback != null) {
                    callback.onFailure(code, msg);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (callback != null) {
                    callback.onFinish();
                }
            }
        });
    }

}
