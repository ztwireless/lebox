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
import com.ledong.lib.leto.config.AppConfig;
import com.ledong.lib.leto.mgc.bean.GetPrivacyContentResultBean;
import com.ledong.lib.leto.mgc.model.MGCSharedModel;
import com.ledong.lib.leto.mgc.util.MGCApiUtil;
import com.ledong.lib.leto.mgc.util.MGCDialogUtil;
import com.ledong.lib.leto.trace.LetoTrace;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.statistic.GameStatisticManager;
import com.leto.game.base.statistic.StatisticEvent;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.MResource;
import com.leto.game.base.util.StorageUtil;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.SwitchButtonO;
import com.mgc.letobox.happy.dialog.PrivacyWebDialog;
import com.mgc.letobox.happy.me.bean.MeModuleBean;


public class OtherHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    // views
    private View _clearCacheView;
    private View _showCoinFloatView;
    private SwitchButtonO _showCoinFloatSwitch;
    private View _csWechatView;
    private TextView _wechatLabel;
    private View _agreemeView;

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

        // clear cache
        _clearCacheView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                MGCDialogUtil.showClearCacheDialog(_ctx, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE) {
                            try {
                                // clear
                                StorageUtil.clearCache(_ctx);
//
//                                // clear my games
//                                _myGames = null;
//                                if(_listView.getAdapter() != null) {
//                                    _listView.getAdapter().notifyDataSetChanged();
//                                }
//                                _myGamesPanel.setVisibility(View.GONE);

                                // report
                                report(StatisticEvent.LETO_COIN_GAMECENTER_CLEAR.ordinal());
                            } catch (Exception e){
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
                if(clipboardManager != null) {
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
                        ((Activity)_ctx).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PrivacyWebDialog.show((Activity) _ctx, data.getInfo() == null?"协议内容需要在后台配置!":data.getInfo(), false);
                            }
                        });
                    }
                });
                return true;
            }
        });

        _appConfig = new AppConfig(BaseAppUtil.getChannelID(_ctx), LoginManager.getUserId(_ctx));
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
        if(_appConfig != null) {
            GameStatisticManager.statisticCoinLog(_ctx, _appConfig.getAppId(), event, _appConfig.getClientKey(),
                    _appConfig.getPackageType(), _appConfig.getMgcGameVersion(), MGCSharedModel.shouldShowCoinFloat(_ctx), 0, 0, 0, _appConfig.getCompact(), 0);
        }
    }
}
