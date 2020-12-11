package com.mgc.letobox.happy.me.holder;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ledong.lib.leto.Leto;
import com.leto.game.base.dialog.PrivacyWebDialog;
import com.leto.game.base.view.SwitchButtonO;
import com.mgc.leto.game.base.MgcAccountManager;
import com.mgc.leto.game.base.bean.LoginResultBean;
import com.mgc.leto.game.base.config.AppConfig;
import com.mgc.leto.game.base.event.DataRefreshEvent;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.listener.SyncUserInfoListener;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.mgc.bean.GetPrivacyContentResultBean;
import com.mgc.leto.game.base.mgc.model.MGCSharedModel;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil;
import com.mgc.leto.game.base.statistic.GameStatisticManager;
import com.mgc.leto.game.base.statistic.StatisticEvent;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.DataCleanManager;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.LeBoxMobileLoginActivity;
import com.mgc.letobox.happy.LeBoxProfileActivity;
import com.mgc.letobox.happy.follow.FollowInviteCodeActivity;
import com.mgc.letobox.happy.me.AboutMeActivity;
import com.mgc.letobox.happy.me.FeedBackActivity;
import com.mgc.letobox.happy.me.bean.MeModuleBean;
import com.mgc.letobox.happy.util.DialogUtil;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LeBoxUtil;

import org.greenrobot.eventbus.EventBus;


public class OtherHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    // views
    private View _clearCacheView;
    private View _showCoinFloatView;
    private SwitchButtonO _showCoinFloatSwitch;
    private View _csWechatView;
    private TextView _wechatLabel;
    private View _agreemeView;
    private View _inviteView;
    private View _deleteAccountView;
    private View _feedBackView;
    private View _logoutView;  //退出登陆
    private View _aboutMeView;  //关于我们

    Context _ctx;

    AppConfig _appConfig;


    public static OtherHolder create(Context ctx, ViewGroup parent) {
        View convertView = LayoutInflater.from(ctx).inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_other"), parent, false);
        return new OtherHolder(ctx, convertView);
    }

    public OtherHolder(Context context, final View itemView) {
        super(itemView);

        // find views
        _ctx = context;
        _splitSpace = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.split_space"));
        _clearCacheView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.clear_cache"));
        _showCoinFloatView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.coin_float_switch_bar"));
        _showCoinFloatSwitch = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.coin_float_switch"));
        _csWechatView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.customer_service"));
        _wechatLabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.wechat"));
        _agreemeView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.agreement_view"));
        _inviteView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.invite_view"));
        _deleteAccountView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.delete_account"));
        _feedBackView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.feed_back"));
        _logoutView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.log_out"));
        _aboutMeView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.about_me"));

        _aboutMeView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                AboutMeActivity.start(_ctx);
                return true;
            }
        });

        _inviteView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                FollowInviteCodeActivity.startActivityByRequestCode((Activity) _ctx, LeBoxConstant.REQUEST_CODE_TASK_INVITE_CODE);
                return true;
            }
        });

        if(MGCSharedModel.isShowInvite || MGCSharedModel.isShowInviteInGameCenter){
            _inviteView.setVisibility(View.VISIBLE);
        }else{
            _inviteView.setVisibility(View.GONE);
        }


        // clear cache
        _clearCacheView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                MGCDialogUtil.showClearCacheDialog(_ctx, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            try {
                                // clear
                                DataCleanManager.clearCache(_ctx);
//
//                                // clear my games
//                                _myGames = null;
//                                if(_listView.getAdapter() != null) {
//                                    _listView.getAdapter().notifyDataSetChanged();
//                                }
//                                _myGamesPanel.setVisibility(View.GONE);

                                // report
                                report(StatisticEvent.LETO_COIN_GAMECENTER_CLEAR.ordinal());
                            } catch (Exception e) {
                            }
                        }
                    }
                });
                return true;
            }
        });

        // show coin float
        _showCoinFloatView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                _showCoinFloatSwitch.setChecked(!_showCoinFloatSwitch.isChecked());
                return true;
            }
        });
        _showCoinFloatSwitch.setChecked(MGCSharedModel.shouldShowCoinFloat(_ctx));
        _showCoinFloatSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MGCSharedModel.setShowCoinFloat(_ctx, isChecked);

                // report
                report(StatisticEvent.LETO_COIN_GAMECENTER_TIMER_SWITCH.ordinal());
            }
        });

        // wechat click
        _csWechatView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                Context ctx = itemView.getContext();
                ClipboardManager clipboardManager = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null) {
                    ClipData clipData = ClipData.newPlainText("wechat", MGCSharedModel.customerServiceWechat);
                    clipboardManager.setPrimaryClip(clipData);
                    ToastUtil.s(ctx, "客服微信号已拷贝到剪贴板");
                }
                return true;
            }
        });

        // privacy show
        _agreemeView.setVisibility(MGCSharedModel.isShowPrivacy ? View.VISIBLE : View.GONE);

        // privacy click
        _agreemeView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                MGCApiUtil.getPrivacyContent(_ctx, new HttpCallbackDecode<GetPrivacyContentResultBean>(_ctx, null) {
                    @Override
                    public void onDataSuccess(final GetPrivacyContentResultBean data) {
                        LetoTrace.d("Leto", "data =" + new Gson().toJson(data));
                        try {
                            if (_ctx != null) {
                                ((Activity) _ctx).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        PrivacyWebDialog.show((Activity) _ctx, data.getInfo() == null ? "协议内容需要在后台配置!" : data.getInfo(), false);
                                    }
                                });
                            }
                        } catch (Throwable e) {

                        }
                    }
                });
                return true;
            }
        });

        _appConfig = new AppConfig(BaseAppUtil.getChannelID(_ctx), LoginManager.getUserId(_ctx));

        _deleteAccountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showConfirmDialog(_ctx, "确定注销账号吗?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE) {

                            LeBoxUtil.deleteAccount(_ctx, new HttpCallbackDecode<Object>(_ctx,null ) {
                                @Override
                                public void onDataSuccess(Object data) {
                                    switchToTemp();
                                }
                                @Override
                                public void onFailure(String code, String message) {

                                    ToastUtil.s(_ctx, "注销失败： " + message);
                                }
                            });

                        }
                    }
                });
            }
        });

        _feedBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedBackActivity.start(context);
            }
        });

        _logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showConfirmDialog(_ctx, "确定退出账号吗?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE) {
                            switchToTemp();
                        }
                    }
                });
            }
        });
    }

    public void switchToTemp(){
        // first we need clear local cache to switch a temp account
        LoginResultBean _loginInfo = Leto.switchToTempAccount(_ctx);

        // then we sync this temp account
        MgcAccountManager.syncAccount(_ctx, "", _loginInfo.getMobile(), false, new SyncUserInfoListener() {
            @Override
            public void onSuccess(LoginResultBean data) {
                EventBus.getDefault().post(new DataRefreshEvent());
            }

            @Override
            public void onFail(String code, String message) {
                ToastUtil.s(_ctx, "退出登录失败");
            }
        });
    }

    @Override
    public void onBind(final MeModuleBean model, int position) {
        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        // wechat label
        _wechatLabel.setText(MGCSharedModel.customerServiceWechat);
    }

    /**
     * 上报事件
     */
    private void report(int event) {
        if (_appConfig != null) {
            GameStatisticManager.statisticCoinLog(_ctx, _appConfig.getAppId(), event, _appConfig.getClientKey(),
                    _appConfig.getPackageType(), _appConfig.getMgcGameVersion(), MGCSharedModel.shouldShowCoinFloat(_ctx), 0, 0, 0, _appConfig.getCompact(), 0);
        }
    }
}
