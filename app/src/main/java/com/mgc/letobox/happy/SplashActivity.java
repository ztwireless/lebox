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
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.MgcAccountManager;
import com.ledong.lib.leto.api.ad.MainHandler;
import com.ledong.lib.leto.mgc.bean.CoinConfigResultBean;
import com.ledong.lib.leto.mgc.model.MGCSharedModel;
import com.ledong.lib.leto.mgc.util.MGCApiUtil;
import com.ledong.lib.leto.widget.ModalDialog;
import com.ledong.lib.minigame.util.PrefetchCache;
import com.leto.game.base.ad.AdManager;
import com.leto.game.base.ad.BaseAd;
import com.leto.game.base.ad.IAdListener;
import com.leto.game.base.ad.bean.AdConfig;
import com.leto.game.base.bean.LoginResultBean;
import com.leto.game.base.db.LoginControl;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.SdkConstant;
import com.leto.game.base.listener.IJumpListener;
import com.leto.game.base.listener.JumpError;
import com.leto.game.base.listener.SyncUserInfoListener;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DeviceUtil;
import com.leto.game.base.util.GameUtil;
import com.mgc.letobox.happy.model.SharedData;
import com.mgc.letobox.happy.util.LeBoxSpUtil;

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

    // views
    private ImageView _splashHolder;
    private FrameLayout _splashAdContainer;
    private FrameLayout _splashAppContainer;

    // ad class
    private BaseAd _splashAd;

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
                        Leto.getInstance().jumpMiniGameWithAppId(SplashActivity.this, _gameId, new IJumpListener() {
                            @Override
                            public void onDownloaded(String path) {
                            }

                            @Override
                            public void onLaunched() {
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

        // get config
        if (!MGCSharedModel.isCoinConfigInited()) {
            doGetConfig();
        } else {
            _configFetched = true;
            startSplashAd();
        }

        // prefetch game center data
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
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                        showExitDialog("获取全局配置失败, 暂时无法启动, 请稍后重试");
                    }
                }
            }
        });
    }

    private void showExitDialog(String msg) {
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
                _handler.sendEmptyMessageDelayed(START_MAIN, 2000);
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
        } catch (Exception e) {
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
        if(LeBoxSpUtil.isFirstLaunch()) {
            _splashAdDone = true;
            startMain(true);
        } else {
            new Handler().postDelayed(new Runnable() {
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
            public void onPresent(String platform) {
                MainHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        _splashHolder.setVisibility(View.GONE);
                        if (null != _splashAdContainer) {
                            _splashAdContainer.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onClick(String platform) {

            }

            @Override
            public void onDismissed(String platform) {
                _splashAdDone = true;
                startMain(true);
            }

            @Override
            public void onFailed(String platform, String s) {
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
            public void onAdLoaded(String platform, int size) {
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
            public void onStimulateSuccess(String platform) {

            }
        });
        if (null != _splashAd) {
            _splashAd.show();
        }else{
            _splashAdDone = true;
            startMain(true);
        }
    }
}
