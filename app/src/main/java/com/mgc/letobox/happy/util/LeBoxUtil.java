package com.mgc.letobox.happy.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.leto.game.base.bean.LetoError;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.SdkApi;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.mgc.letobox.happy.me.bean.ExchangeRequestBean;
import com.mgc.letobox.happy.me.bean.TaskListRequestBean;
import com.mgc.letobox.happy.me.bean.UserTaskStatusRequestBean;

/**
 * Create by zhaozhihui on 2019-09-11
 **/
public class LeBoxUtil {

    private static final String TAG ="LeBoxUtil";

    private static void printUrl(String path) {
        Log.e(TAG, "http_url=" + SdkApi.getRequestUrl() + path);
    }

    //盒子初始化
    public static String getStartup() {
        return  "";
//        return SdkApi.getRequestUrl() + "system/hzstartup";
    }

    //获取盒子版本的升级信息
    public static String getLatestVersion() {
        printUrl("upgrade/box_up");
        return SdkApi.getRequestUrl() + "upgrade/box_up";
    }

    /**
     * 查询新手任务列表
     */
    public static void getNewPlayerTasklist(final Context ctx, final HttpCallbackDecode callback) {
        try {
            TaskListRequestBean requestBean = new TaskListRequestBean();
            requestBean.setTask_type(1);
            requestBean.setApp_id(BaseAppUtil.getChannelID(ctx));

            String args = new Gson().toJson(requestBean);
            (new RxVolley.Builder())
                    .setTag(ctx)
                    .shouldCache(false)
                    .url(SdkApi.getTaskList() + "?data=" + args)
                    .callback(callback)
                    .doTask();
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
            }
        }
    }

    /**
     * 查询用户新手任务列表
     */
    public static void getUserNewPlayerTasklist(final Context ctx, final HttpCallbackDecode callback) {
        try {
            TaskListRequestBean requestBean = new TaskListRequestBean();
            requestBean.setTask_type(1);
            requestBean.setApp_id(BaseAppUtil.getChannelID(ctx));
            requestBean.setMobile(LoginManager.getUserId(ctx));

            String args = new Gson().toJson(requestBean);
            String url = SdkApi.getUserTaskList() + "?data=" + args;

            Log.i(TAG, "getUserNewPlayerTasklist: " + url);

            (new RxVolley.Builder())
                    .setTag(ctx)
                    .shouldCache(false)
                    .url(url)
                    .callback(callback)
                    .doTask();
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
            }
        }
    }

    /**
     *  上报新手任务列表
     */
    public static void reportUserNewPlayerTasklist(final Context ctx, int task_id, long progress, final HttpCallbackDecode callback) {
        try {
            Log.i(TAG, "reportUserNewPlayerTasklist:  task_id="+task_id+" ---- progress= "+ progress );
            UserTaskStatusRequestBean requestBean = new UserTaskStatusRequestBean();
            requestBean.setChannel_task_id(task_id);
            requestBean.setProgress(progress);
            requestBean.setApp_id(BaseAppUtil.getChannelID(ctx));
            requestBean.setMobile(LoginManager.getUserId(ctx));

            String args = new Gson().toJson(requestBean);
            (new RxVolley.Builder())
                    .setTag(ctx)
                    .shouldCache(false)
                    .url(SdkApi.updateUserTask() + "?data=" + args)
                    .callback(callback)
                    .doTask();
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
            }
        }
    }



    /**
     * 进群兑换码兑换
     */
    public static void exchange(final Context ctx, String code, final HttpCallbackDecode callback) {
        try {
            ExchangeRequestBean requestBean = new ExchangeRequestBean();
            requestBean.setCode(code);
            requestBean.setChannel_id(Integer.parseInt(BaseAppUtil.getChannelID(ctx)));
            requestBean.setMobile(LoginManager.getMobile(ctx));

            String args = new Gson().toJson(requestBean);
            (new RxVolley.Builder())
                    .setTag(ctx)
                    .shouldCache(false)
                    .url(SdkApi.exchange() + "?data=" + args)
                    .callback(callback)
                    .doTask();
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
            }
        }
    }

    /**
     * 进群兑换码兑换
     */
    public static void getJoinWechatContent(final Context ctx, final HttpCallbackDecode callback) {
        try {
            ExchangeRequestBean requestBean = new ExchangeRequestBean();
            requestBean.setChannel_id(Integer.parseInt(BaseAppUtil.getChannelID(ctx)));

            String args = new Gson().toJson(requestBean);
            (new RxVolley.Builder())
                    .setTag(ctx)
                    .shouldCache(false)
                    .url(SdkApi.getJoinWeChatQrcode() + "?data=" + args)
                    .callback(callback)
                    .doTask();
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
            }
        }
    }
}
