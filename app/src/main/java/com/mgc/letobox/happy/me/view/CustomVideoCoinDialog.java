package com.mgc.letobox.happy.me.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.api.ApiContainer;
import com.ledong.lib.leto.config.AppConfig;
import com.ledong.lib.leto.mgc.AppChannel;
import com.ledong.lib.leto.mgc.MGCConst;
import com.ledong.lib.leto.mgc.bean.AddCoinResultBean;
import com.ledong.lib.leto.mgc.bean.PreAddCoinResultBean;
import com.ledong.lib.leto.mgc.model.MGCSharedModel;
import com.ledong.lib.leto.mgc.thirdparty.IMintage;
import com.ledong.lib.leto.mgc.thirdparty.ISignin;
import com.ledong.lib.leto.mgc.thirdparty.MintageRequest;
import com.ledong.lib.leto.mgc.thirdparty.MintageResult;
import com.ledong.lib.leto.mgc.thirdparty.SigninRequest;
import com.ledong.lib.leto.mgc.thirdparty.SigninResult;
import com.ledong.lib.leto.mgc.util.MGCApiUtil;
import com.ledong.lib.leto.trace.LetoTrace;
import com.ledong.lib.leto.utils.DeviceInfo;
import com.ledong.lib.leto.widget.ClickGuard;
import com.ledong.lib.minigame.GameCenterActivity;
import com.leto.game.base.event.DataRefreshEvent;
import com.leto.game.base.event.GetCoinEvent;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.statistic.GameStatisticManager;
import com.leto.game.base.statistic.StatisticEvent;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.MResource;
import com.leto.game.base.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 问题, 信息, 失败对话框, 统一样式: 标题, 图标, 信息, ok和cancel按钮
 */
@Keep
public class CustomVideoCoinDialog extends Dialog implements ApiContainer.IApiResultListener {
    @Override
    public void onApiSuccess(ApiContainer.ApiName n) {
        LetoTrace.d("preAddCoin", "video end ......");
        _videoViewed = true;
        // pre add coin
        preAddCoin();
    }

    @Override
    public void onApiFailed(ApiContainer.ApiName n, boolean aborted) {
        if (aborted) {
            _videoViewed = false;
            DialogUtil.dismissDialog();
            ToastUtil.s(_ctx, _ctx.getString(MResource.getIdByName(_ctx, "R.string.leto_mgc_need_view_video_complete")));
        } else {
            // TODO 如果不是中途放弃的处理
        }
    }

    public interface GameEndCoinDialogListener {
        /**
         * 退出时调用
         *
         * @param video   true表示用户看了视频
         * @param coinGot 用户最后获得的金币数
         */
        void onExit(boolean video, int coinGot);
    }

    // views
    private View _videoButton;
    private LinearLayout _myRewardView;
    private TextView _videoLabel;
    private ImageView _videoIconView;
    private TextView _auxButton;
    private TextView _titleLabel;
    private TextView _coinLabel;
    private TextView _myCoinLabel;
    private TextView _myMoneyLabel;
    private TextView _myRewardLabel;

    // when done, onClick is called
    private GameEndCoinDialogListener _doneListener;

    // model
    private AppConfig _appConfig;
    private int _addCoin; // coin to be added, already multiplied with ratio
    private long _gameTime;
    private long _todayTime;
    private boolean _videoViewed;
    private boolean _highCoin;
    private int _addCoinRatio;
    private boolean _coinAdded;

    // true means resume container when detach
    private boolean _shouldResumeContainer = true;

    // strings
    private String _loading;
    private String _confirm;
    private String _close;
    private String _leto_mgc_dollar;
    private String _leto_mgc_video_coin_failed;
    private String _leto_mgc_video_add_coin_failed;
    private String _leto_mgc_view_video;
    private String _leto_mgc_get_now;

    public int _dialogScene;

    Context _ctx;

    ApiContainer _apiContainer;

    ViewGroup _adContainer;


    public CustomVideoCoinDialog(@NonNull Context context, String title, int coin, int videoMultiple, int dialogScene, GameEndCoinDialogListener doneListener) {
        super(context, MResource.getIdByName(context, "R.style.LetoCustomDialog"));

        _ctx = context;

        _apiContainer = new ApiContainer(_ctx, null, null);

        // init
        _doneListener = doneListener;

        _dialogScene = dialogScene;


        // load content view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(MResource.getIdByName(context, "R.layout.leto_mgc_dialog_game_coin"), null);

        // views
        _titleLabel = view.findViewById(MResource.getIdByName(context, "R.id.title"));
        _videoButton = view.findViewById(MResource.getIdByName(context, "R.id.video"));
        _videoLabel = view.findViewById(MResource.getIdByName(context, "R.id.video_label"));
        _videoIconView = view.findViewById(MResource.getIdByName(context, "R.id.video_icon"));
        _auxButton = view.findViewById(MResource.getIdByName(context, "R.id.auxiliary_button"));
        _coinLabel = view.findViewById(MResource.getIdByName(context, "R.id.coin"));
        _myCoinLabel = view.findViewById(MResource.getIdByName(context, "R.id.my_coin"));
        _myMoneyLabel = view.findViewById(MResource.getIdByName(context, "R.id.my_money"));
        _myRewardView = view.findViewById(MResource.getIdByName(context, "R.id.ll_reward"));
        _myRewardLabel = view.findViewById(MResource.getIdByName(context, "R.id.my_reward"));

        // get strings
        _loading = context.getString(MResource.getIdByName(context, "R.string.loading"));
        _confirm = context.getString(MResource.getIdByName(context, "R.string.confirm"));
        _close = context.getString(MResource.getIdByName(context, "R.string.close"));
        _leto_mgc_dollar = context.getString(MResource.getIdByName(context, "R.string.leto_mgc_dollar"));
        _leto_mgc_video_coin_failed = context.getString(MResource.getIdByName(context, "R.string.leto_mgc_video_coin_failed"));
        _leto_mgc_video_add_coin_failed = context.getString(MResource.getIdByName(context, "R.string.leto_mgc_video_add_coin_failed"));
        _leto_mgc_view_video = context.getString(MResource.getIdByName(context, "R.string.leto_mgc_view_video"));
        _leto_mgc_get_now = context.getString(MResource.getIdByName(context, "R.string.leto_mgc_get_now"));

        // add coin, empty for now, need get from network
        _coinLabel.setText("");

        setTitle(title);


        // my coin
        _myCoinLabel.setText(String.valueOf(MGCSharedModel.myCoin));

        // my money
        _myMoneyLabel.setText(String.format("%.02f%s",
                (float) MGCSharedModel.myCoin / MGCSharedModel.coinRmbRatio, _leto_mgc_dollar));

        // video button
        _videoButton.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (_auxButton.getVisibility() == View.VISIBLE) {
                    showVideo();
                } else if (_highCoin) {
                    exit();
                } else if (!_coinAdded) {
                    LetoTrace.d("preAddCoin", "click add coin btn");
                    preAddCoin();
                } else {
                    exit();
                }

                return true;
            }
        });
        if (!_highCoin) {
            _videoLabel.setText(String.format("%sX%d", _videoLabel.getText(), videoMultiple == 0 ? MGCSharedModel.coinVideoRatio : videoMultiple));
        }

        // auxiliary button
        _auxButton.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                // 高倍模式关闭是没有金币的, 点关闭的话给用户提示
                // 非高倍模式则直接领取
                if (!_highCoin) {
                    preAddCoin();
                }
                return true;
            }
        });

        if (_dialogScene == 1 && AppChannel.PPTV.getValue().equalsIgnoreCase(BaseAppUtil.getChannelID(context))) {

            if (Leto.getInstance() != null && Leto.getInstance().getLetoSignInRewardListener() != null) {
                _myRewardView.setVisibility(View.VISIBLE);
                _myRewardLabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                    @Override
                    public boolean onClicked() {

                        if (Leto.getInstance() != null && Leto.getInstance().getLetoSignInRewardListener() != null) {
                            Leto.getInstance().getLetoSignInRewardListener().show(_ctx);
                        }
                        return true;
                    }
                });
            }

        }

        // update buttons
        updateButtons();

        // set content view
        setContentView(view);
        setCanceledOnTouchOutside(true);

        //通过window来设置位置、高宽
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        window.setGravity(Gravity.CENTER);
        params.width = DeviceInfo.getWidth(context);

//		// set flag
//		_coinAdded = false;
//
//		// hide button, change button text
//		_auxButton.setVisibility(View.GONE);
//		_videoLabel.setText(_confirm);
//		_videoIconView.setVisibility(View.GONE);
//
        // update coin label
        _coinLabel.setText(String.format("+%d", coin));


    }

    @Override
    public void onBackPressed() {
        if (_coinAdded) {
            exit();
        } else {
            // ignore back if user doesn't get coin yet
        }
    }

    private void updateButtons() {
        // 如果广告没打开, 隐藏视频按钮
        // 如果不是高倍模式, 检查视频系数和剩余播放次数, 系数小于等于1, 或无剩余次数, 隐藏视频按钮
        // 如果没有得到leto容器, 隐藏视频按钮
        if (!MGCSharedModel.adEnabled ||
                (!_highCoin && (MGCSharedModel.coinVideoRatio <= 1 || MGCSharedModel.leftVideoTimes <= 0))) {
            _auxButton.setVisibility(View.GONE);
            _videoLabel.setText(_leto_mgc_get_now);
            _videoIconView.setVisibility(View.GONE);
        }
    }

    public void setTitle(String t) {
        _titleLabel.setText(t);
    }


    private void exit() {
        if (_doneListener != null) {
            _doneListener.onExit(_videoViewed, _addCoin);
        }
        dismiss();
    }

    public void setAuxButtonText(String t) {
        _auxButton.setText(t);
    }

    private void exitWithMsg(String msg) {
        DialogUtil.dismissDialog();
        ToastUtil.s(getContext(), msg);
        exit();
    }

    /**
     * 开始第三方发币流程
     */
    private void doThirdpartySignin(int coin) {
        // show loading
        Context ctx = getContext();
        DialogUtil.showDialog(ctx, _loading);

        // request mintage
        IMintage mintageInterface = Leto.getInstance().getThirdpartyMintage();
        if (mintageInterface != null && coin > 0) {
            mintageInterface.requestMintage(ctx, new MintageRequest(ctx, _appConfig.getAppId(), coin) {
                @Override
                public void notifyMintageResult(MintageResult result) {
                    if (result.getErrCode() == 0) {
                        onCoinAdded(result.getCoin());
                    } else {
                        onCoinAddFailed(_leto_mgc_video_add_coin_failed);
                    }
                }
            });
        } else {
            onCoinAddFailed(_leto_mgc_video_add_coin_failed);
        }
    }

    /**
     * 开始第三方发币流程
     */
    private void addThirdpartyCoin(int coin) {
        // show loading
        Context ctx = getContext();
        DialogUtil.showDialog(ctx, _loading);

        // request mintage
        IMintage mintageInterface = Leto.getInstance().getThirdpartyMintage();
        if (mintageInterface != null && coin > 0) {
            mintageInterface.requestMintage(ctx, new MintageRequest(ctx, _appConfig.getAppId(), coin) {
                @Override
                public void notifyMintageResult(MintageResult result) {
                    if (result.getErrCode() == 0) {
                        onCoinAdded(result.getCoin());
                    } else {
                        onCoinAddFailed(_leto_mgc_video_add_coin_failed);
                    }
                }
            });
        } else {
            onCoinAddFailed(_leto_mgc_video_add_coin_failed);
        }
    }


    private void onCoinAddFailed(String msg) {
        exitWithMsg(msg);
    }

    private void onCoinAdded(int coin) {
        // set flag
        _coinAdded = true;

        // hide button, change button text
        _auxButton.setVisibility(View.GONE);
        _videoLabel.setText(_confirm);
        _videoIconView.setVisibility(View.GONE);

        // update coin label
        _coinLabel.setText(String.format("+%d", coin));

        // update my coin
        _myCoinLabel.setText(String.valueOf(MGCSharedModel.myCoin + coin));

        // update my money label
        _myMoneyLabel.setText(String.format("%.02f%s",
                (float) (MGCSharedModel.myCoin + coin) / MGCSharedModel.coinRmbRatio,
                _leto_mgc_dollar));

        // report
        if (_dialogScene == 0) {
            if (_highCoin) {
                report(StatisticEvent.LETO_HIGH_COIN_GOT.ordinal(), coin, _addCoinRatio);
            } else if (_videoViewed) {
                report(StatisticEvent.LETO_COIN_GAMECENTER_VIDEO_GETCOINS.ordinal(), coin, _addCoinRatio);
            } else {
                report(StatisticEvent.LETO_COIN_GAMECENTER_GETCOINS.ordinal(), coin, 1);
            }
        } else if (_dialogScene == 1) {
            if (_videoViewed) {
                report(StatisticEvent.LETO_SIGN_LOOKVIDEOGETCOINS.ordinal(), coin, _addCoinRatio);
            } else {
                report(StatisticEvent.LETO_SIGN_GETCOINS.ordinal(), coin, 1);
            }
        }

        // dismiss loading
        DialogUtil.dismissDialog();

        // report
        IMintage mintageItf = Leto.getInstance().getThirdpartyMintage();
        if (MGCSharedModel.thirdpartyCoin && mintageItf != null) {
            int scene = getScene();
            MGCApiUtil.reportThirdpartyMintage(getContext(), _appConfig.getAppId(), (int) (_gameTime / 1000), coin, scene);
        }

        EventBus.getDefault().post(new DataRefreshEvent());
    }

    private int getScene() {

        switch (_dialogScene) {
            case 0:  //玩游戏
                return _videoViewed ? (_highCoin ? MGCConst.ADD_COIN_BY_VIDEO : MGCConst.ADD_COIN_BY_VIDEO_AFTER_GAME) : MGCConst.ADD_COIN_BY_PLAYING_GAME;

            case 1:  //签到
                return _videoViewed ? MGCConst.ADD_COIN_BY_SIGNIN_VIDEO : MGCConst.ADD_COIN_BY_SIGNIN;

            case 2:  //做任务
                return MGCConst.ADD_COIN_BY_GAME_TASK_REWARD;

            default:
                return MGCConst.ADD_COIN_BY_VIDEO;

        }

    }

    private void preAddCoin() {
        if (_dialogScene == 1 && null != Leto.getInstance().getThirdpartySignin()) {
            // request signin
            ISignin signinInterface = Leto.getInstance().getThirdpartySignin();
            signinInterface.requestSignin(_ctx, new SigninRequest(_ctx) {

                @Override
                public void notifySigninResult(SigninResult result) {
                    try {
                        if (result != null && result.getErrCode() == 0) {
                            doPreAddCoin();
                        } else {
                            onCoinAddFailed(_ctx.getResources().getString(MResource.getIdByName(_ctx, "R.string.leto_mgc_signin_fail")));
                        }
                    } catch (Exception e) {

                    }
                }
            });

        } else {
            doPreAddCoin();
        }
    }

    private void doPreAddCoin() {
        final Context ctx = getContext();
        DialogUtil.showDialog(ctx, _loading);
        MGCApiUtil.preAddCoin(ctx, BaseAppUtil.getChannelID(ctx), (int) (_todayTime / 1000), (int) (_gameTime / 1000), getScene(), new HttpCallbackDecode<PreAddCoinResultBean>(ctx, null) {
            @Override
            public void onDataSuccess(PreAddCoinResultBean data) {
                if (data != null) {
                    _addCoin = data.getAdd_coins();
                    _addCoinRatio = data.getCoins_multiple();
                    IMintage mintageInterface = Leto.getInstance().getThirdpartyMintage();
                    if (MGCSharedModel.thirdpartyCoin && mintageInterface != null) {
                        addThirdpartyCoin(_addCoin);
                    } else {
                        addCoin(data.getCoins_token(), _addCoin);
                    }
                } else {
                    onCoinAddFailed(_leto_mgc_video_coin_failed);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                onCoinAddFailed(_leto_mgc_video_coin_failed);
            }
        });
    }

    private void addCoin(String token, int coin) {
        final Context ctx = getContext();
        MGCApiUtil.addCoin(ctx, BaseAppUtil.getChannelID(ctx), coin, token, getScene(), new HttpCallbackDecode<AddCoinResultBean>(ctx, null) {
            @Override
            public void onDataSuccess(AddCoinResultBean data) {
                if (data != null) {
                    _addCoin = data.getAdd_coins();
                    onCoinAdded(_addCoin);
                } else {
                    onCoinAddFailed(_leto_mgc_video_add_coin_failed);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (_dialogScene == 1) {
                    if (TextUtils.isEmpty(msg)) {
                        onCoinAddFailed(_ctx.getResources().getString(MResource.getIdByName(_ctx, "R.string.leto_mgc_signin_fail")));
                    } else {
                        onCoinAddFailed(msg);
                    }

                } else {
                    onCoinAddFailed(_leto_mgc_video_add_coin_failed);
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();

                EventBus.getDefault().post(new GetCoinEvent());
            }
        });
    }

    /**
     * 事件上报
     */
    private void report(int event, int coins, int ratio) {
        if (_appConfig != null) {
            GameStatisticManager.statisticCoinLog(getContext(), _appConfig.getAppId(), event, _appConfig.getClientKey(),
                    _appConfig.getPackageType(), _appConfig.getMgcGameVersion(), MGCSharedModel.shouldShowCoinFloat(getContext()), coins, 0, ratio, _appConfig.getCompact(), 0);
        }
    }


    public void setAdContainer(ViewGroup adContainer) {
        _adContainer = adContainer;
        if (_apiContainer != null) {
            _apiContainer.setAdContainer(_adContainer);
        }
    }


    private void showVideo() {

        if (_apiContainer != null) {
            _apiContainer.showVideo(this);
        }

    }
}
