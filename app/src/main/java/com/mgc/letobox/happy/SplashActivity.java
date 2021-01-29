package com.mgc.letobox.happy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.ledong.lib.leto.Leto;
import com.ledong.lib.minigame.util.PrefetchCache;
import com.mgc.leto.game.base.LetoConst;
import com.mgc.leto.game.base.MgcAccountManager;
import com.mgc.leto.game.base.api.be.AdDotManager;
import com.mgc.leto.game.base.be.AdConst;
import com.mgc.leto.game.base.be.AdManager;
import com.mgc.leto.game.base.be.BaseAd;
import com.mgc.leto.game.base.be.IAdListener;
import com.mgc.leto.game.base.be.bean.AdConfig;
import com.mgc.leto.game.base.be.bean.mgc.MgcAdBean;
import com.leto.game.base.event.ShowProvicyEvent;
import com.leto.game.base.event.ShowRookieGiftEvent;
import com.mgc.leto.game.base.bean.LoginResultBean;
import com.mgc.leto.game.base.db.LoginControl;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.http.RxVolleyManager;
import com.mgc.leto.game.base.http.SdkConstant;
import com.mgc.leto.game.base.listener.IJumpListener;
import com.mgc.leto.game.base.listener.JumpError;
import com.mgc.leto.game.base.listener.SyncUserInfoListener;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.mgc.bean.CoinConfigResultBean;
import com.mgc.leto.game.base.mgc.bean.GetBenefitsSettingResultBean;
import com.mgc.leto.game.base.mgc.model.MGCSharedModel;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.sdk.LetoAdInfo;
import com.mgc.leto.game.base.statistic.AdInfo;
import com.mgc.leto.game.base.statistic.GameStatisticManager;
import com.mgc.leto.game.base.statistic.StatisticEvent;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.DeviceUtil;
import com.mgc.leto.game.base.utils.GameUtil;
import com.mgc.leto.game.base.utils.MainHandler;
import com.mgc.leto.game.base.widget.ModalDialog;
import com.mgc.letobox.happy.model.SharedData;
import com.mgc.letobox.happy.util.LeBoxSpUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks;

public class SplashActivity extends AppCompatActivity implements PermissionCallbacks {
    private final static String TAG = SplashActivity.class.getSimpleName();

    // messages
    private final int START_MAIN = 0;

    // request code
    private static final int REQUEST_CHECK_PERMISSION = 100;

    // cache file name
    private static final String LEBOX_CENSOR_CACHE_FILE = "__lebox_censor_mode";

    // use censor version?
    private boolean _censorMode = false;

    // 游戏id, 如果存在, 则要判断服务器返回的openType, 如果是1, 则打开该id指定的游戏
    private String _gameId;
    private boolean _needCheckOpenType;

    // 启动主程序的条件标识
    private boolean _permissionInited;
    private boolean _configFetched;
    private boolean _started;
    private int _configRetryCount = 3;

    private boolean _splashAdDone = false;

    private boolean _splashAdLoading = false;

    // config error dialog
    private boolean _needRerunConfig = true;
    private ModalDialog _configErrorDialog;

    // views
    private ImageView _splashHolder;
    private FrameLayout _splashAdContainer;
    private FrameLayout _splashAppContainer;

    // ad class
    private BaseAd _splashAd;

    AdConfig _adConfig;

    private String clientKey;

    MgcAdBean _mgcAdBean;

    private Handler _handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case START_MAIN:
                    if (!_needCheckOpenType || MGCSharedModel.openType == MGCSharedModel.OPEN_TYPE_BOX) {
                        Intent intent = new Intent(SplashActivity.this, GameCenterTabActivity.class);
                        intent.putExtra("censorMode", _censorMode);
                        startActivity(intent);
                        finish();
                    } else {
                        Leto.jumpMiniGameWithAppId(SplashActivity.this, _gameId, new IJumpListener() {
                            @Override
                            public void onDownloaded(String path) {
                            }

                            @Override
                            public void onLaunched() {
                                // 如果新手红包可用, 发送一个事件让leto activity显示新手红包
                                if (MGCSharedModel.isRookieGiftAvailable()) {
                                    ShowRookieGiftEvent e = new ShowRookieGiftEvent();
                                    e.appId = _gameId;
                                    EventBus.getDefault().postSticky(e);
                                }

                                // 如果显示隐私协议, 发送一个事件让leto activity显示相应对话框
                                if (MGCSharedModel.isShowPrivacy) {
                                    ShowProvicyEvent e = new ShowProvicyEvent();
                                    EventBus.getDefault().postSticky(e);
                                }

                                // 结束自己
                                finish();
                            }

                            @Override
                            public void onError(final JumpError code, String message) {
                                _handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showExitDialog(String.format("启动失败, 请稍后重试, 错误码: %d", code.ordinal()));
                                    }
                                });
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set content view
        setContentView(R.layout.activity_splash);

        // find views
        _splashHolder = findViewById(R.id.splash_holder);
        _splashAdContainer = findViewById(R.id.splash_ad_container);
        _splashAppContainer = findViewById(R.id.splash_app_container);

        // get device bean
        if (SdkConstant.deviceBean == null) {
            SdkConstant.deviceBean = DeviceUtil.getDeviceBean(this);
        }
        SdkConstant.userToken = LoginControl.getUserToken();

        // get game id
        _gameId = BaseAppUtil.getMetaStringValue(this, "MGC_GAMEID");
        _needCheckOpenType = !TextUtils.isEmpty(_gameId);
        if (!_needCheckOpenType) {
            _configFetched = true; // 如果不需要检查openType, 直接设置为true
        }

        clientKey =String.valueOf(System.currentTimeMillis()) ;

        // sync account
        syncAccount();

        // init permission
        initPermission();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(TAG, "onPostCreate");
        // try load cached config, if no cache, set censor mode to true
        if (GameUtil.hasCacheFile(this, LEBOX_CENSOR_CACHE_FILE)) {
            _censorMode = GameUtil.loadInt(this, LEBOX_CENSOR_CACHE_FILE) == 1;
        } else {
            _censorMode = true;
        }
    }

    private void prefetchGameCenter() {

        SharedData.MGC_HOME_TAB_ID = BaseAppUtil.getMetaIntValue(this, "MGC_HOME_TAB_ID");
        SharedData.MGC_RANK_TAB_ID = BaseAppUtil.getMetaIntValue(this, "MGC_RANK_TAB_ID");
        SharedData.MGC_CHALLENGE_TAB_ID = BaseAppUtil.getMetaIntValue(this, "MGC_CHALLENGE_TAB_ID");
        PrefetchCache.getInstance().prefetchGameCenter(this, SharedData.MGC_HOME_TAB_ID, 1, null);
        PrefetchCache.getInstance().prefetchGameCenter(this, SharedData.MGC_RANK_TAB_ID, 0, null);
        PrefetchCache.getInstance().prefetchGameCenter(this, SharedData.MGC_CHALLENGE_TAB_ID, 1, null);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        // 如果需要重新检查配置, 开始检查
        if (_needRerunConfig) {
            // set flag
            _needRerunConfig = false;

            // dismiss dialog
            if (_configErrorDialog != null) {
                _configErrorDialog.dismiss();
                _configErrorDialog = null;
            }

            // get config
            if (!MGCSharedModel.isCoinConfigInited()) {
                doGetConfig();
            } else {
                _configFetched = true;
            }

            // get benefit settings
            if (!MGCSharedModel.isBenefitSettingsInited()) {
                doGetBenefitSettings();
            }
            startSplashAd();

            // prefetch game center data
            prefetchGameCenter();
        }
    }

    private void doGetBenefitSettings() {
        MGCApiUtil.getBenefitSettings(this, new HttpCallbackDecode<GetBenefitsSettingResultBean>(this, null) {
            @Override
            public void onDataSuccess(GetBenefitsSettingResultBean data) {
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
            }
        });
    }

    private void doGetConfig() {
        MGCApiUtil.getCoinConfig(this, new HttpCallbackDecode<CoinConfigResultBean>(this, null) {
            @Override
            public void onDataSuccess(CoinConfigResultBean data) {
                // get censor mode
                _censorMode = data.getIs_audit() == 1;

                // save to local
                GameUtil.saveInt(SplashActivity.this, data.getIs_audit(), LEBOX_CENSOR_CACHE_FILE);

                // start main if can
                _configFetched = true;
                startSplashAd();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);

                // 如果需要检查openType, 则必须等待config完成, 如果失败, 则需要重试, 设置最大重试3次
                if (_needCheckOpenType) {
                    _configRetryCount--;
                    if (_configRetryCount > 0) {
                        _handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                doGetConfig();
                            }
                        }, 100);
                    } else {
                        // 只能提示退出了
                        onConfigError("获取全局配置失败, 暂时无法启动, 请稍后重试");
                    }
                }
            }
        });
    }

    private void onConfigError(String msg) {
        // 设置标志, 以便onResume时重新尝试获取配置
        _needRerunConfig = true;
        _configRetryCount = 3;

        // ensure no other dialog
        if (_configErrorDialog != null) {
            _configErrorDialog.dismiss();
            _configErrorDialog = null;
        }

        // 显示一个错误对话框
        _configErrorDialog = showExitDialog(msg);
    }

    private ModalDialog showExitDialog(String msg) {
        ModalDialog dialog = new ModalDialog(SplashActivity.this);
        dialog.setMessage(msg);
        dialog.setRightButton("退出", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.setMessageTextColor("#666666");
        dialog.setMessageTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        dialog.setLeftButtonTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        dialog.setRightButtonTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        dialog.setLeftButtonTextColor("#999999");
        dialog.setRightButtonTextColor("#FF3D9AF0");
        dialog.show();
        return dialog;
    }

    private void initPermission() {
        //for test, can remove later
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            afterPermissionCheck();
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CHECK_PERMISSION);
        }
    }

    private void startMain(boolean loadedAd) {

		/*
		 启动主程序分需要满足三个条件
		 1.
		 1. 如果没有配置MGC_GAMEID, 则在权限检查后即可启动盒子
		 2. 如果配置了MGC_GAMEID, 则必须等待getCoinConfig完成以便检查openType, 因此getCoinConfig一旦失败则需要重试, 重试3次后还失败则退出.
		 	第二个条件是权限检查完成. 两个条件都达到时, 检查openType, 2则启动盒子, 1则启动游戏
		 */
        if (_permissionInited && _configFetched && !_started && _splashAdDone) {
            if (loadedAd) {
                _handler.sendEmptyMessageDelayed(START_MAIN, 500);
            } else {
                _handler.sendEmptyMessageDelayed(START_MAIN, 1000);
            }
            _started = true;

            // 清空第一次启动标志
            LeBoxSpUtil.setFirstLaunch(false);
        }
    }

    @AfterPermissionGranted(REQUEST_CHECK_PERMISSION)
    void afterPermissionCheck() {
        // init rx volley
        try {
            RxVolley.setRequestQueue(RequestQueue.newRequestQueue(BaseAppUtil.getDefaultSaveRootPath(this, "mgc")));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        // check if we can start main
        _permissionInited = true;
        startMain(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CHECK_PERMISSION) {
            // try start main
            _permissionInited = true;
            startMain(false);
        }
    }

    public void syncAccount() {
        if (!LoginManager.isSignedIn(this)) {
            MgcAccountManager.syncAccount(this, "", "", false, new SyncUserInfoListener() {
                @Override
                public void onSuccess(LoginResultBean data) {

                }

                @Override
                public void onFail(String code, String message) {

                }
            });
        }
    }

    public void startSplashAd() {
        // 如果是第一次启动, 不获取splash ad
        // 否则延迟1秒尝试获取splash ad
        if (LeBoxSpUtil.isFirstLaunch()) {
            _splashAdDone = true;
            startMain(true);
        } else {
            _handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initSplashAd();
                }
            }, 1000);
        }
    }

    public void initSplashAd() {
        // 如果没有广告容器, 放弃载入splash
        if (_splashAdContainer == null) {
            _splashAdDone = true;
            startMain(true);
            return;
        }

        // 如果没有splash配置, 放弃载入splash ad
        AdConfig adConfig = AdManager.getInstance().getActiveSplashAdConfig(true);
        if (adConfig == null) {
            _splashAdDone = true;
            startMain(false);
            return;
        }

        // 开始载入splash ad
        _splashAd = AdManager.getInstance().getSplashAD(SplashActivity.this, adConfig, _splashAdContainer, 1, new IAdListener() {
            @Override
            public void onPresent(LetoAdInfo platform) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        _splashHolder.setVisibility(View.GONE);
                        if (null != _splashAdContainer) {
                            _splashAdContainer.setVisibility(View.VISIBLE);
                        }

                        //上报给mgc
                        if (null != _mgcAdBean && !TextUtils.isEmpty(_mgcAdBean.mgcExposeReportUrl)) {
                            AdDotManager.showDot(_mgcAdBean.mgcExposeReportUrl, null);
                        }
                    }
                });
            }

            @Override
            public void onClick(LetoAdInfo platform) {

                //上报给mgc
                if (null != _mgcAdBean && !TextUtils.isEmpty(_mgcAdBean.mgcClickReportUrl)) {
                    AdDotManager.showDot(_mgcAdBean.mgcClickReportUrl, null);
                }

            }

            @Override
            public void onDismissed(LetoAdInfo platform) {
                _splashAdDone = true;
                startMain(true);
            }

            @Override
            public void onFailed(LetoAdInfo platform, String s) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != _splashAdContainer) {
                            _splashAdContainer.setVisibility(View.GONE);
                        }
                        _splashAdDone = true;
                        startMain(true);
                    }
                });
            }

            @Override
            public void onAdLoaded(LetoAdInfo platform, int size) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        if (null != _splashAdContainer) {
                            _splashAdContainer.setVisibility(View.VISIBLE);
                        }
                        if (null != _splashHolder) {
                            _splashHolder.setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onStimulateSuccess(LetoAdInfo platform) {

            }
        });
        if (null != _splashAd) {
            if(_splashAdLoading) {
                LetoTrace.d(TAG, "start give up, and skip....");
                return;
            }

            if (_mgcAdBean == null) {
                _mgcAdBean = new MgcAdBean();
            }
            _mgcAdBean.finalAdFrom = AdConst.AD_FROM_SDK;
            _mgcAdBean.appId = adConfig.app_id;
            _mgcAdBean.posId = adConfig.getSplash_pos_id();
            _mgcAdBean.platform = adConfig.getPlatform();

            _mgcAdBean.buildMgcReportUrl(SplashActivity.this, "", adConfig.id, AdConst.AD_TYPE_SPLASH);

            reportAdRequest();


            Log.d(TAG, "splash ad show....");
            _splashAdLoading = true;

            _splashAd.show();

        } else {
            Log.d(TAG, "splash is null, skip....");
            _splashAdDone = true;
            startMain(true);
        }
    }

    private void report(int login_type, String adinfo, int videoScene) {

        GameStatisticManager.statisticGameLog(SplashActivity.this, BaseAppUtil.getChannelID(SplashActivity.this), login_type, 0, 0, clientKey, 0, LetoConst.EVENT_SUCCESS, "", 0, "", adinfo,
                false, 0, 0, 0, 0, 0, 0, 0, "", videoScene,
                null);
    }

    private AdInfo getAdInfo(AdConfig adConfig){
        AdInfo adinfo = new AdInfo();
        adinfo.setAd_type(AdConst.AD_TYPE_SPLASH);
        adinfo.setApp_id(BaseAppUtil.getChannelID(SplashActivity.this));
        adinfo.setChannel_id(BaseAppUtil.getChannelID(SplashActivity.this));
        adinfo.setMobile(LoginManager.getMobile(SplashActivity.this));
        adinfo.setOrigin(adConfig != null ? adConfig.id : 0);
        adinfo.setAction_type(_splashAd != null ? _splashAd.getActionType() : AdConst.AD_ACTION_DOWNLOAD);
        return adinfo;
    }

    private void reportAdRequest() {

        //请求广告上报
        AdInfo adinfo = getAdInfo(_adConfig);
        report(StatisticEvent.LETO_INGAMEAD_REQUEST.ordinal(), new Gson().toJson(adinfo), 0);
    }

}
