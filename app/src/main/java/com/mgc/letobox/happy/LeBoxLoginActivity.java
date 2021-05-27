package com.mgc.letobox.happy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.leto.game.base.LetoEvents;
import com.mgc.leto.game.base.MgcAccountManager;
import com.mgc.leto.game.base.bean.LoginResultBean;
import com.mgc.leto.game.base.event.DataRefreshEvent;
import com.mgc.leto.game.base.event.GetCoinEvent;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.listener.SyncUserInfoListener;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.DialogUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import static com.mgc.letobox.happy.util.LeBoxConstant.REQUEST_CODE;

public class LeBoxLoginActivity extends BaseActivity implements UMAuthListener, SyncUserInfoListener {
    // views
    private View _signInWechatBtn;
    private View _signInMobileBtn;
    private TextView _userAgreementLabel;
    private TextView _privacyAgreementLabel;
    private ImageView _backBtn;

    private int reqestCode = -1;

    private String openId;

    Handler mHandler;

    public static void start(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, LeBoxLoginActivity.class);
            context.startActivity(intent);
        }
    }

    public static void startActivityByRequestCode(Activity context, int requestCode) {
        if (null != context) {
            Intent intent = new Intent(context, LeBoxLoginActivity.class);
            intent.putExtra(REQUEST_CODE, requestCode);

            context.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#ffffff"));
        }

        // set content view
        setContentView(MResource.getIdByName(this, "R.layout.activity_lebox_login"));

        if (getIntent() != null) {
            reqestCode = getIntent().getIntExtra(LeBoxConstant.REQUEST_CODE, -1);
        }

        // find views
        _backBtn = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
        _signInWechatBtn = findViewById(MResource.getIdByName(this, "R.id.sign_in_wechat"));
        _signInMobileBtn = findViewById(MResource.getIdByName(this, "R.id.sign_in_mobile"));
        _userAgreementLabel = findViewById(MResource.getIdByName(this, "R.id.user_agreement"));
        _privacyAgreementLabel = findViewById(MResource.getIdByName(this, "R.id.privacy_agreement"));

        // back click
        _backBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                finish();
                return true;
            }
        });

        // agreement label underline & click
        _userAgreementLabel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        _userAgreementLabel.getPaint().setAntiAlias(true);
        _privacyAgreementLabel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        _privacyAgreementLabel.getPaint().setAntiAlias(true);
        _userAgreementLabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                com.mgc.letobox.happy.util.DialogUtil.showAgreement(LeBoxLoginActivity.this, "user.html");
                return true;
            }
        });
        _privacyAgreementLabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                com.mgc.letobox.happy.util.DialogUtil.showAgreement(LeBoxLoginActivity.this, "privacy.html");
                return true;
            }
        });

        // sign in wechat
        _signInWechatBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                getWechatAuthInfo();
                return true;
            }
        });

        // sign in mobile
        _signInMobileBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (reqestCode != -1) {
                    LeBoxMobileLoginActivity.startActivityByRequestCode(LeBoxLoginActivity.this, reqestCode);
                } else {
                    LeBoxMobileLoginActivity.start(LeBoxLoginActivity.this);
                }

                return true;
            }
        });

        mHandler = new Handler();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();

        // check if signed in, finish self
        if (LoginManager.isSignedIn(this)) {
            EventBus.getDefault().post(new DataRefreshEvent());
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        UMShareAPI.get(this).release();

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        // check if signed in, finish self
        if (LoginManager.isSignedIn(this)) {
            EventBus.getDefault().post(new DataRefreshEvent());
            finish();
        }
    }

    private void getWechatAuthInfo() {
        // get weixin info
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI umShareAPI = UMShareAPI.get(this);
        umShareAPI.setShareConfig(config);
        umShareAPI.getPlatformInfo(LeBoxLoginActivity.this, SHARE_MEDIA.WEIXIN, this);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        // show loading
        showLoading(false, getString(MResource.getIdByName(this, "R.string.leto_loading")));

        // sync account
        int gender = 0;
        try {
            gender = Integer.parseInt(map.get("gender"));
        } catch (NumberFormatException e) {
        }
        openId = map.get("unionid");

        final int userGender = gender;
        MGCApiUtil.bindWeiXin(LeBoxLoginActivity.this, map, new HttpCallbackDecode(LeBoxLoginActivity.this, null) {
            @Override
            public void onDataSuccess(Object data) {

                //延迟500ms 再调，防止服务器报同步账号 请求频繁
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MgcAccountManager.syncAccount(LeBoxLoginActivity.this,
                                map.get("unionid"),
                                "",
                                map.get("name"),
                                map.get("iconurl"),
                                userGender,
                                true,
                                null,
                                LeBoxLoginActivity.this
                        );
                    }
                }, 500);

            }

            @Override
            public void onFailure(String code, String message) {
                dismissLoading();
                ToastUtil.s(LeBoxLoginActivity.this, message + "(" + code + ")");

                //绑定微信账号失败
                if (LetoEvents.getLoginListener() != null) {
                    LetoEvents.getLoginListener().onCancel();
                }
            }
        });
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable t) {
        ToastUtil.s(LeBoxLoginActivity.this, "失败：" + t.getMessage());
        dismissLoading();

        //微信授权失败，通知登陆失败
        if (LetoEvents.getLoginListener() != null) {
            LetoEvents.getLoginListener().onCancel();
        }
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        ToastUtil.s(LeBoxLoginActivity.this, "取消了");
        dismissLoading();

        //微信授权取消，通知登陆取消
        if (LetoEvents.getLoginListener() != null) {
            LetoEvents.getLoginListener().onCancel();
        }
    }

    @Override
    public void onSuccess(LoginResultBean data) {
        if (LetoEvents.getLoginListener() != null) {
            int status = 0;
            int realNameStatus = data.getRealname_status();
            if (realNameStatus == 1 || realNameStatus == 2) {
                status = 1;
            }

            LetoEvents.getLoginListener().onLoginSuccess(openId, "", true, status, data.getBirthday());
        }
        EventBus.getDefault().post(new GetCoinEvent());
        dismissLoading();
        finish();
    }

    @Override
    public void onFail(String code, String message) {
        dismissLoading();
        ToastUtil.s(LeBoxLoginActivity.this, "账号刷新失败, 请重试（code=" + code + ")");

        //同步账号失败了，也通知取消
        if (LetoEvents.getLoginListener() != null) {
            LetoEvents.getLoginListener().onCancel();
        }
    }
}
