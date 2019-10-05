package com.mgc.letobox.happy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.ledong.lib.leto.MgcAccountManager;
import com.ledong.lib.leto.main.BaseActivity;
import com.ledong.lib.leto.mgc.bean.CoinConfigResultBean;
import com.ledong.lib.leto.mgc.model.MGCSharedModel;
import com.ledong.lib.leto.mgc.util.MGCApiUtil;
import com.ledong.lib.minigame.util.PrefetchCache;
import com.leto.game.base.bean.LoginResultBean;
import com.leto.game.base.db.LoginControl;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.http.SdkConstant;
import com.leto.game.base.listener.SyncUserInfoListener;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DeviceUtil;
import com.leto.game.base.util.GameUtil;
import com.mgc.letobox.happy.bean.StartUpBean;
import com.mgc.letobox.happy.bean.StartupResultBean;
import com.mgc.letobox.happy.util.LeBoxUtil;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private final static  String TAG = SplashActivity.class.getSimpleName();
    private ImageView iv_splash;

    private final int  START_MAIN =0;
    private final int  START_LOGIN =1;
    private final int  CHECK_VERSION =2;
    private final int  GO_APP = 3;
    private String mark = "";

    private static final int REQUEST_CODE_WRITE_PERMISSION = 2003;

    private static final String LEBOX_CENSOR_CACHE_FILE = "__lebox_censor_mode";

    Context mContext;

    // use censor version?
    private boolean _censorMode = false;

    private Handler mHandler= new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case START_MAIN:
                    Intent intent = new Intent(SplashActivity.this, GameCenterTabActivity.class);
                    intent.putExtra("censorMode", _censorMode);
                    startActivity(intent);
                    finish();
                    break;

                case GO_APP:

                    if(!TextUtils.isEmpty(LoginControl.getUserToken())){
                        //启动时初始化agent
//                        SdkConstant.MGC_AGENT = LoginControl.getAgentgame();
                        SdkConstant.userToken = LoginControl.getUserToken();
                        mHandler.sendEmptyMessageDelayed(START_MAIN, 2500);
                    }else{
                        mHandler.sendEmptyMessageDelayed(START_LOGIN, 2500);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;

//        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(SdkConstant.ACTION_SESSION_EXPIRED));

        if (SdkConstant.deviceBean == null) {
            SdkConstant.deviceBean = DeviceUtil.getDeviceBean(this);
        }
        SdkConstant.userToken = LoginControl.getUserToken();

        syncAccount();

        initPermission();

        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // try load cached config, if no cache, set censor mode to true
        if(GameUtil.hasCacheFile(this, LEBOX_CENSOR_CACHE_FILE)) {
            _censorMode = GameUtil.loadInt(this, LEBOX_CENSOR_CACHE_FILE) == 1;
        } else {
            _censorMode = true;
        }

        // get config
        if(!MGCSharedModel.isCoinConfigInited()) {
            MGCApiUtil.getCoinConfig(this, new HttpCallbackDecode<CoinConfigResultBean>(this, null) {
                @Override
                public void onDataSuccess(CoinConfigResultBean data) {
                    // get censor mode
                    _censorMode = data.getIs_audit() == 1;

                    // save to local
                    GameUtil.saveInt(SplashActivity.this, data.getIs_audit(), LEBOX_CENSOR_CACHE_FILE);
                }
            });
        }

        // prefetch game center data
        PrefetchCache.getInstance().prefetchGameCenter(this, 17, 1, null);
        PrefetchCache.getInstance().prefetchGameCenter(this, 18, 0, null);
        PrefetchCache.getInstance().prefetchGameCenter(this, 19, 1, null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Log.i(TAG, "onDestroy");
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);

    }


    private void initPermission() {
        //for test, can remove later
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            afterPermissionCheck();
        } else {
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }


    @AfterPermissionGranted(100)
    void afterPermissionCheck() {

        try {
            RxVolley.setRequestQueue(RequestQueue.newRequestQueue(BaseAppUtil.getDefaultSaveRootPath(this,"mgc")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mHandler.sendEmptyMessageDelayed(START_MAIN, 2500);
    }

    void afterStartup() {
        mHandler.sendEmptyMessageDelayed(START_MAIN, 2500);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i(TAG, "onPermissionsGranted");

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.i(TAG, "onPermissionsDenied");
        if(requestCode==100) {
            afterStartup();
        }else if(requestCode==2003){
            finish();
        }
    }

    public void syncAccount(){
        if(!LoginManager.isSignedIn(this)) {
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

    void callStartup () {
        StartUpBean startUpBean = new StartUpBean();
        startUpBean.setUser_token(SdkConstant.userToken);
        int open_cnt = 1;
        startUpBean.setOpen_cnt(open_cnt + "");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(new Gson().toJson(startUpBean));
        HttpCallbackDecode httpCallbackDecode = new HttpCallbackDecode<StartupResultBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(StartupResultBean data) {
                if (data != null) {
                    LoginControl.saveUserToken(data.getUser_token());
                    SdkConstant.userToken = data.getUser_token();
                    SdkConstant.SERVER_TIME_INTERVAL = data.getTimestamp() - System.currentTimeMillis();

//                    if(data.getIs_login()==1) {
                    mHandler.sendEmptyMessage(GO_APP);
//                    }else{
//                        mHandler.sendEmptyMessage(START_LOGIN);
//                    }
                }
            }
            @Override
            public void onFailure(String code, String msg) {
                Toast.makeText(SplashActivity.this, "初始化失败, 请重新打开App", Toast.LENGTH_SHORT).show();
            }
        };
        httpCallbackDecode.setShowTs(false);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(LeBoxUtil.getStartup(), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }
}
