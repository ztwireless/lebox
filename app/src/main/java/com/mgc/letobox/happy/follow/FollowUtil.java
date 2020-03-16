package com.mgc.letobox.happy.follow;

import android.content.Context;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.ledong.lib.leto.LetoConst;
import com.ledong.lib.leto.mgc.bean.BaseUserRequestBean;
import com.ledong.lib.leto.mgc.bean.SignInRequestBean;
import com.leto.game.base.bean.BasePageRequetBean;
import com.leto.game.base.bean.LetoError;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.http.SdkApi;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.mgc.letobox.happy.follow.bean.FollowBindRequestBean;
import com.mgc.letobox.happy.follow.bean.FollowInviteApprenticesRequestBean;

/**
 * Create by zhaozhihui on 2020-03-04
 **/
public class FollowUtil {

    private static final String URL_COMMON_QUESTION =  SdkApi.getRequestUrl() + "invatepage/problem";  //常见问题
    private static final String URL_INVITE_RULE =  SdkApi.getRequestUrl() + "invatepage/invaterule";  // 邀请规则
    private static final String URL_VALID_FRIEND =  SdkApi.getRequestUrl() + "invatepage/friends";   //有效好友
    private static final String URL_INVITE_GUIDE =  SdkApi.getRequestUrl() + "invatepage/secret_book";   //邀请好友攻略
    private static final String URL_AWAKE_FRIEND =  SdkApi.getRequestUrl() + "invatepage/wakeuprule";   //唤醒好友规则
    private static final String URL_FACE_TO_FACE_DOWNLOAD =  SdkApi.getRequestUrl() + "invatepage/sweepcode";   //面对面扫码


    public static void startCommonQuestion(Context context) {
        if (context == null)
            return;
        FollowWebViewActivity.start(context, "常见问题", URL_COMMON_QUESTION);
    }


    public static void startInviteRule(Context context) {
        if (context == null)
            return;
        FollowWebViewActivity.start(context, "邀请规则", URL_INVITE_RULE);
    }

    public static void startValidFriend(Context context) {
        if (context == null)
            return;
        FollowWebViewActivity.start(context, "有效好友", URL_VALID_FRIEND);
    }

    public static void startFriendGuide(Context context) {
        if (context == null)
            return;
        FollowWebViewActivity.start(context, "邀请好友秘籍", URL_INVITE_GUIDE);
    }

    public static void startAwakeFriend(Context context) {
        if (context == null)
            return;
        FollowWebViewActivity.start(context, "唤醒好友规则", URL_AWAKE_FRIEND);
    }

    public static void startFaceToFace(Context context) {
        if (context == null)
            return;
        FollowWebViewActivity.start(context, "面对面扫码", URL_FACE_TO_FACE_DOWNLOAD);
    }




    public static void getInviteIndex(final Context ctx, final HttpCallbackDecode callback) {
        try {

            BaseUserRequestBean requestBean = new BaseUserRequestBean(ctx);
            requestBean.setChannel_id(Integer.parseInt(BaseAppUtil.getChannelID(ctx)));
            HttpParamsBuild httpParamsBuild = new HttpParamsBuild(new Gson().toJson(requestBean), true);
            callback.setAuthkey(httpParamsBuild.getAuthkey());
            callback.setLoadingCancel(false);
            callback.setShowTs(true);
            callback.setShowLoading(true);

            (new RxVolley.Builder()).url(SdkApi.getInviteIndex()).params(httpParamsBuild.getHttpParams()).httpMethod(1).callback(callback).setTag(ctx).shouldCache(false).doTask();

        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
            }
        }

    }


    public static void getInviteApprentices(final Context ctx, int page, int offest, final HttpCallbackDecode callback) {
        try {

            FollowInviteApprenticesRequestBean requestBean = new FollowInviteApprenticesRequestBean();
            requestBean.setUser_token(LoginManager.getUserToken(ctx));
            requestBean.setMobile(LoginManager.getMobile(ctx));
            requestBean.setPage(page);
            requestBean.setOffset(offest);

            requestBean.setChannel_id(Integer.parseInt(BaseAppUtil.getChannelID(ctx)));

            HttpParamsBuild httpParamsBuild = new HttpParamsBuild(new Gson().toJson(requestBean), true);
            callback.setAuthkey(httpParamsBuild.getAuthkey());
            callback.setShowTs(true);
            (new RxVolley.Builder()).url(SdkApi.getInviteApprentices()).params(httpParamsBuild.getHttpParams()).httpMethod(1).callback(callback).setTag(ctx).shouldCache(false).doTask();


        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
            }
        }
    }


    public static void follow(final Context ctx, String code, final HttpCallbackDecode callback) {
        try {
            FollowBindRequestBean requestBean = new FollowBindRequestBean();
            requestBean.setUser_token(LoginManager.getUserToken(ctx));
            requestBean.setMobile(LoginManager.getUserId(ctx));
            requestBean.setCode(code);
            requestBean.setChannel_id(Integer.parseInt(BaseAppUtil.getChannelID(ctx)));
            HttpParamsBuild httpParamsBuild = new HttpParamsBuild(new Gson().toJson(requestBean), true);
            callback.setAuthkey(httpParamsBuild.getAuthkey());
            callback.setLoadingCancel(false);
            callback.setShowTs(true);
            callback.setShowLoading(true);
            (new RxVolley.Builder()).url(SdkApi.getInviteBind()).params(httpParamsBuild.getHttpParams()).httpMethod(1).callback(callback).setTag(ctx).shouldCache(false).doTask();

        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailure(LetoError.UNKNOW_EXCEPTION, e.getLocalizedMessage());
            }
        }

    }



}
