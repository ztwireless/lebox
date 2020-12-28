package com.mgc.letobox.happy.task;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.leto.reward.LetoRewardEvents;
import com.leto.reward.LetoRewardManager;
import com.leto.reward.listener.ILetoAnswerCallBack;
import com.leto.reward.listener.ILetoIdiomCallBack;
import com.leto.reward.listener.ILetoOpenRedPacketCallBack;
import com.leto.reward.listener.ILetoScratchCardCallBack;
import com.leto.reward.listener.ILetoTurntableCallBack;
import com.leto.reward.model.IdiomResultGame;
import com.leto.reward.model.QaGameRewardBean;
import com.leto.reward.model.TurnTableRewardBean;
import com.mgc.leto.game.base.LetoEvents;
import com.mgc.leto.game.base.bean.LoginResultBean;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.listener.ILetoLoginResultCallback;
import com.mgc.leto.game.base.listener.ILetoPlayedDurationListener;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.mgc.bean.CoinDialogScene;
import com.mgc.leto.game.base.mgc.dialog.IMGCCoinDialogListener;
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.letobox.happy.NewerTaskManager;
import com.mgc.letobox.happy.event.DailyTaskRefreshEvent;
import com.mgc.letobox.happy.listener.IRewardedVideoListener;
import com.mgc.letobox.happy.me.bean.TaskResultBean;
import com.mgc.letobox.happy.me.bean.UserTaskStatusResultBean;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LetoBoxEvents;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Create by zhaozhihui on 2020/12/24
 **/
public class LeBoxTaskManager {
    private final static String TAG = LeBoxTaskManager.class.getSimpleName();


    ILetoPlayedDurationListener playedDurationListener;

    ILetoAnswerCallBack _anserCallBack;
    ILetoIdiomCallBack _idiomCallBack;
    ILetoOpenRedPacketCallBack _openRedPacketCallBack;
    ILetoScratchCardCallBack _scratchCardCallBack;
    ILetoTurntableCallBack _turntableCallBack;

    IRewardedVideoListener _rewardedVideoCallback;

    WeakReference<Context> _weakReferenceContext;

    public LeBoxTaskManager(Context context) {
        _weakReferenceContext = new WeakReference<>(context);

        init();
    }

    private void init() {
        //获取任务列表
        NewerTaskManager.getNewPlayerTaskList(_weakReferenceContext.get(), null);
        NewerTaskManager.getDailyTaskList(_weakReferenceContext.get(), null);

        initRewardListener();
    }

    public void destroy() {
        removeRewardListener();

        if (mTaskHandler != null) {
            mTaskHandler.removeCallbacksAndMessages(null);
        }
    }

    private void removeRewardListener() {
        LetoRewardEvents.setAnserCallBack(null);
        LetoRewardEvents.setScratchCardCallBack(null);
        LetoRewardEvents.setTurntableCallBack(null);
        LetoRewardEvents.setIdiomCallBack(null);

        LetoEvents.setLetoLoginResultCallback(null);

        LetoEvents.removeLetoPlayedDurationListener(playedDurationListener);
    }

    public void initRewardListener() {

        //玩游戏时长回调
        playedDurationListener = new ILetoPlayedDurationListener() {
            @Override
            public void getPlayedDurations(String gameId, long duration) {

                Log.i(TAG, "gameId: " + gameId + "-------------duration: " + duration);
                //如果是语聊，则更新到本地
                if (!TextUtils.isEmpty(gameId) && LetoRewardManager.isChatGame(gameId)) {
                    LetoTrace.d(TAG, "reportChatGameProgress: " + duration);
                    LetoRewardManager.updateChatGameProgress(_weakReferenceContext.get(), duration);
                }

                reportTaskProgress(duration);
            }
        };
        LetoEvents.addLetoPlayedDurationListener(playedDurationListener);

        _rewardedVideoCallback = new IRewardedVideoListener() {
            @Override
            public void showVideo() {
                reportDailyTaskProgressByTaskType(LeBoxConstant.LETO_TASK_TYP_VIEW_VIDEO, 1);
            }
        };

        _anserCallBack = new ILetoAnswerCallBack() {
            @Override
            public void onAnswer(Context ctx, QaGameRewardBean request) {
                LetoTrace.d("dati callback");
                if (request.getIs_correct() == 1) {
                    reportDailyTaskProgressByTaskType(LeBoxConstant.LETO_TASK_TYP_REWARD_ANSWER, 1);
                }
            }
        };
        _idiomCallBack = new ILetoIdiomCallBack() {
            @Override
            public void onAnswer(Context ctx, IdiomResultGame request) {
                LetoTrace.d("idiom callback");
                if (request.getIs_correct() == 1) {
                    reportDailyTaskProgressByTaskType(LeBoxConstant.LETO_TASK_TYP_REWARD_IDIOM, 1);
                }
            }
        };
        _scratchCardCallBack = new ILetoScratchCardCallBack() {
            @Override
            public void onScratch(Context ctx, TurnTableRewardBean request) {
                LetoTrace.d("scratch callback");

                reportDailyTaskProgressByTaskType(LeBoxConstant.LETO_TASK_TYP_REWARD_SCRATCH_CARD, 1);

            }
        };
        _turntableCallBack = new ILetoTurntableCallBack() {
            @Override
            public void onTurntable(Context ctx, TurnTableRewardBean request) {
                LetoTrace.d("turntable callback");
                reportDailyTaskProgressByTaskType(LeBoxConstant.LETO_TASK_TYP_REWARD_TURNTABLE, 1);
            }
        };
        LetoRewardEvents.setAnserCallBack(_anserCallBack);
        LetoRewardEvents.setScratchCardCallBack(_scratchCardCallBack);
        LetoRewardEvents.setTurntableCallBack(_turntableCallBack);
        LetoRewardEvents.setIdiomCallBack(_idiomCallBack);
        LetoBoxEvents.setRewardedVideoListener(_rewardedVideoCallback);

        LetoEvents.setLetoLoginResultCallback(new ILetoLoginResultCallback() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onLoginSuccess(LoginResultBean data) {
                LetoTrace.d(TAG, "setLetoLoginResultCallback onLoginSuccess...");
                try {
                    if (data != null && !LoginManager.isTempAccount(data.getMobile())) {
                        LetoTrace.d(TAG, "finish bind phone task...");
                        NewerTaskManager.getUserNewPlayerTaskStatus(_weakReferenceContext.get(), new HttpCallbackDecode<List<UserTaskStatusResultBean>>(_weakReferenceContext.get(), null, new TypeToken<List<UserTaskStatusResultBean>>() {
                        }.getType()) {
                            @Override
                            public void onDataSuccess(final List<UserTaskStatusResultBean> data) {
                                reportDailyTaskProgressByTaskType(LeBoxConstant.LETO_TASK_TYP_BIND_PHONE, 1);
                            }

                            @Override
                            public void onFailure(String code, String msg) {
                                super.onFailure(code, msg);
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();

                            }
                        });
                    }
                } catch (Throwable e) {

                }
            }
        });
    }

    public void reportTaskProgress(long progress) {
        boolean hasNewTask = false;
        boolean hasDailyTask = false;
        List<TaskResultBean> taskResultBeans = NewerTaskManager.getCompleteNewerTaskId(_weakReferenceContext.get(), "", progress);
        if (taskResultBeans != null && taskResultBeans.size() > 0) {
            hasNewTask = true;
        }

        List<TaskResultBean> dailyTaskResultBeans = NewerTaskManager.getCompleteDailyTaskIdOnPlayGame(_weakReferenceContext.get(), "", progress);

        if (dailyTaskResultBeans != null && dailyTaskResultBeans.size() > 0) {
            taskResultBeans.addAll(dailyTaskResultBeans);
            hasDailyTask = true;
        }
        if (taskResultBeans != null && taskResultBeans.size() > 0) {
            showTaskDialog(taskResultBeans, 0, 0);
        }

        EventBus.getDefault().post(new DailyTaskRefreshEvent());
    }


    public void reportDailyTaskProgressByTaskType(int taskType, long progress) {

        List<TaskResultBean> taskResultBeans = NewerTaskManager.getCompleteDailyTaskId(_weakReferenceContext.get(), taskType, "", progress);
        if (taskResultBeans != null && taskResultBeans.size() > 0) {
            showTaskDialog(taskResultBeans, 0, 0);
        }

        EventBus.getDefault().post(new DailyTaskRefreshEvent());
    }

    private void showTaskDialog(final List<TaskResultBean> taskBeans, final int pos, final int action) {
        if (_weakReferenceContext.get()==null) {
            return;
        }

        MGCDialogUtil.showMGCCoinDialogWithOrderId(_weakReferenceContext.get(), null, taskBeans.get(pos).getAward_coins(), 1, CoinDialogScene.ROOKIE_TASK,
                taskBeans.get(pos).getChannel_task_id(), new IMGCCoinDialogListener() {
                    @Override
                    public void onExit(boolean video, int coinGot) {
                        if (taskBeans.size() > pos + 1) {
                            Message msg = new Message();
                            msg.obj = taskBeans;
                            msg.arg1 = pos + 1;
                            msg.arg2 = action;
                            mTaskHandler.sendMessage(msg);
                        }
                    }
                });
    }

    Handler mTaskHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            List<TaskResultBean> taskBeans = (List<TaskResultBean>) msg.obj;
            int pos = msg.arg1;
            int action = msg.arg2;

            showTaskDialog(taskBeans, pos, action);

        }

    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LeBoxConstant.REQUEST_CODE_TASK_INVITE_CODE) {
            LetoTrace.d("invite code call back: resultCode =" + resultCode);
            if (resultCode == 1) {
                reportDailyTaskProgressByTaskType(LeBoxConstant.LETO_TASK_TYP_BIND_INVITE, 1);
            }

        } else if (requestCode == LeBoxConstant.REQUEST_CODE_TASK_PHONE_LOGIN) {
            LetoTrace.d("phone login call back: resultCode =" + resultCode);
            if (resultCode == 1) {
                reportDailyTaskProgressByTaskType(LeBoxConstant.LETO_TASK_TYP_BIND_PHONE, 1);
            }
        }
    }
}
