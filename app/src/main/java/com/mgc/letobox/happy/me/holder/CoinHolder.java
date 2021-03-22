package com.mgc.letobox.happy.me.holder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgc.leto.game.base.LetoEvents;
import com.mgc.leto.game.base.api.constant.Constant;
import com.mgc.leto.game.base.bean.LoginResultBean;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.mgc.AppChannel;
import com.mgc.leto.game.base.mgc.ExchangeActivity;
import com.mgc.leto.game.base.mgc.WithdrawActivity;
import com.mgc.leto.game.base.mgc.bean.GetUserCoinResultBean;
import com.mgc.leto.game.base.mgc.model.MGCSharedModel;
import com.mgc.leto.game.base.mgc.thirdparty.IWithdraw;
import com.mgc.leto.game.base.mgc.thirdparty.WithdrawRequest;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil;
import com.mgc.leto.game.base.statistic.GameStatisticManager;
import com.mgc.leto.game.base.statistic.StatisticEvent;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.DialogUtil;
import com.mgc.leto.game.base.utils.GlideUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.LeBoxLoginActivity;
import com.mgc.letobox.happy.LeBoxMobileLoginActivity;
import com.mgc.letobox.happy.LeBoxProfileActivity;
import com.mgc.letobox.happy.follow.FollowInviteActivity;
import com.mgc.letobox.happy.me.bean.MeModuleBean;
import com.mgc.letobox.happy.util.LeBoxConstant;

public class CoinHolder extends CommonViewHolder<MeModuleBean> {
    // views
    private TextView _totalCoinLabel;
    private TextView _todayCoinLabel;
    private View _withdrawView;
    private TextView _withdrawTextView;
    private View _totalCoinView;
    private View _todayCoinView;
    private TextView _nameLabel;
    private ImageView _avatarView;
    private View _profileContainer;
    private TextView _sigLabel;
    private TextView _moneyLabel;
    private TextView _invideCodeLabel;
    private TextView _copyCodeLabel;
    LinearLayout _coinView;
    LinearLayout _myCoinFieldView;
    LinearLayout _todayCoinFieldView;
    LinearLayout _withdrawField;
    RelativeLayout _inviteField;

    View _splitSpace;

    Context _ctx;

    private GetUserCoinResultBean _model;


    // strings
    private String _loading;
    private String _leto_mgc_failed_get_user_coin;
    private String _leto_mgc_dollar;

    public static CoinHolder create(Context ctx, ViewGroup parent) {
        View convertView = LayoutInflater.from(ctx).inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_coin"), parent, false);
        return new CoinHolder(convertView);
    }

    public CoinHolder(View itemView) {
        super(itemView);

        // find views
        _ctx = itemView.getContext();

        //状态栏适配高度
        View fake_status_bar = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.fake_status_bar"));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fake_status_bar.getLayoutParams();
        params.height = StatusBarUtil.getStatusBarHeight(_ctx);
        fake_status_bar.setLayoutParams(params);

        _splitSpace = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.split_space"));
        _totalCoinLabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.total_coin"));
        _todayCoinLabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.today_coin"));
        _withdrawView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.withdraw"));
        _withdrawTextView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.tv_withdraw"));
        _totalCoinView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.ll_total_coin"));
        _todayCoinView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.ll_today_coin"));
        _nameLabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.name"));
        _avatarView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.avatar"));
        _profileContainer = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.profile_container"));
        _sigLabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.signature"));
        _coinView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.coin_view"));
        _withdrawField = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.withdraw_field"));
        _myCoinFieldView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.mycoin_field"));
        _todayCoinFieldView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.todaycoin_field"));
        _moneyLabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.money"));
        _inviteField = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.rl_invite"));
        _invideCodeLabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.invite_code"));
        _copyCodeLabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.copy_code"));

        // get strings
        _loading = _ctx.getString(MResource.getIdByName(_ctx, "R.string.leto_loading"));
        _leto_mgc_failed_get_user_coin = _ctx.getString(MResource.getIdByName(_ctx, "R.string.leto_mgc_failed_get_user_coin"));
        _leto_mgc_dollar = _ctx.getString(MResource.getIdByName(_ctx, "R.string.leto_mgc_dollar"));
    }

    @Override
    public void onBind(final MeModuleBean model, int position) {
        // space
        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        // update user profile
        updateProfile();

        // label
        // 更新金币数, 因为提现会减少金币, 先更新一次, 然后再通过网络请求更新
        _totalCoinLabel.setText(String.valueOf(MGCSharedModel.myCoin));
        _todayCoinLabel.setText(String.valueOf(MGCSharedModel.todayCoin));
        _moneyLabel.setText(String.format("%.02f%s", (float) MGCSharedModel.myCoin / MGCSharedModel.coinRmbRatio, _leto_mgc_dollar));

        if (MGCSharedModel.coinExchageType == Constant.COIN_CONSUME_TYPE_EXCHANGE) {
            _withdrawTextView.setText("立即兑换");
        } else {
            _withdrawTextView.setText("立即提现");

            //步数宝
            if (BaseAppUtil.getChannelID(_ctx).equals(AppChannel.BUSHUBAO.getValue())) {
                _withdrawTextView.setText("兑换燃力");
            }
        }

        if (MGCSharedModel.isShowInvite) {
            _inviteField.setVisibility(View.VISIBLE);
        } else {
            _inviteField.setVisibility(View.GONE);
        }

        _inviteField.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                //点击上报
                GameStatisticManager.statisticBenefitLog(_ctx, "", StatisticEvent.LETO_FOLLOW_INVITE.ordinal(), 0, 0, 0, 0, Constant.BENEFITS_TYPE_INVITE, 0);

                FollowInviteActivity.start(_ctx);

                return true;
            }
        });

        _inviteField.setVisibility(View.GONE);


        // withdraw click
        _withdrawView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                if (MGCSharedModel.coinExchageType == Constant.COIN_CONSUME_TYPE_EXCHANGE) {
                    ExchangeActivity.start(_ctx);
                } else {
                    IWithdraw withdrawInterface = LetoEvents.getThirdpartyWithdraw();
                    if (MGCSharedModel.thirdpartyWithdraw && withdrawInterface != null) {
                        thirdpartyWithdraw();
                    } else {
                        WithdrawActivity.start(_ctx);
                    }
                }

                return true;
            }
        });

        // total coin click
        _totalCoinView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (LetoEvents.getThirdpartyCoinListener() != null) {
                    LetoEvents.getThirdpartyCoinListener().onTotalCoin();
                }
                return true;
            }
        });

        // today coin click
        _todayCoinView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (LetoEvents.getThirdpartyCoinListener() != null) {
                    LetoEvents.getThirdpartyCoinListener().onTodayCoin();
                }
                return true;
            }
        });
        if (MGCSharedModel.hideMycoins) {
            _myCoinFieldView.setVisibility(View.GONE);
            _todayCoinFieldView.setVisibility(View.GONE);
        }
        if (MGCSharedModel.hideExchangeBtn) {
            _withdrawField.setVisibility(View.GONE);
        }
        if (MGCSharedModel.hideExchangeBtn && MGCSharedModel.hideMycoins) {
            _coinView.setVisibility(View.GONE);
        }

        // profile click
        _profileContainer.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                if (!LoginManager.isSignedIn(_ctx)) {
                    if (BaseAppUtil.getMetaBooleanValue(_ctx, "MGC_ENABLE_WECHAT_LOGIN")) {
                        LeBoxLoginActivity.start(_ctx);
                    } else {
                        LeBoxMobileLoginActivity.startActivityByRequestCode((Activity) _ctx, LeBoxConstant.REQUEST_CODE_TASK_PHONE_LOGIN);
                    }
                } else {
                    LeBoxProfileActivity.start(_ctx);
                }
                return true;
            }
        });

        // get user coin
        doGetUserCoin();
    }

    private void updateProfile() {
        // get login info
        LoginResultBean loginInfo = LoginManager.getUserLoginInfo(_ctx);
        if (loginInfo != null) {
            // if temp account, use default avatar builtin
            // if not, load avatar
            if (!LoginManager.isSignedIn(_ctx)) {
                // avatar
                _avatarView.setImageResource(MResource.getIdByName(_ctx, "R.mipmap.lebox_no_avatar"));

                // name
                _nameLabel.setText(String.format("游客%s", loginInfo.getMem_id()));

                // sig
                _sigLabel.setText("登录后同步游戏记录");

                _invideCodeLabel.setVisibility(View.GONE);
                _copyCodeLabel.setVisibility(View.GONE);
            } else {
                // avatar
                if (TextUtils.isEmpty(loginInfo.getPortrait())) {
                    _avatarView.setImageResource(MResource.getIdByName(_ctx, "R.mipmap.lebox_no_avatar"));
                } else {
                    GlideUtil.loadCircle(_ctx, loginInfo.getPortrait(), _avatarView);
                }

                // name
                if (BaseAppUtil.getMetaBooleanValue(_ctx, "MGC_ENABLE_WECHAT_LOGIN")) {
                    String nickname = loginInfo.getNickname();
                    _nameLabel.setText(nickname);
                } else {
                    String mobile = loginInfo.getMobile();
                    if(mobile.length()==11){
                        mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
                    }else{
                        mobile = mobile.substring(0, 11);
                    }
                    _nameLabel.setText(mobile);
                }

                // sig
//                _sigLabel.setText("边玩游戏边赚钱");
                _sigLabel.setText("邀请码：");

                _invideCodeLabel.setVisibility(View.VISIBLE);
                _copyCodeLabel.setVisibility(View.VISIBLE);

                _invideCodeLabel.setText(String.valueOf(loginInfo.getInvate_code()));

                _copyCodeLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseAppUtil.copyToSystem(_ctx, "" + loginInfo.getInvate_code());
                        ToastUtil.s(_ctx, "复制成功");
                    }
                });
            }
        }
    }

    private void onThirdpartyWithdrawFail() {
        // seems nothing need to be done
    }

    private void thirdpartyWithdraw() {

        IWithdraw withdrawInterface = LetoEvents.getThirdpartyWithdraw();
        if (withdrawInterface != null) {
            withdrawInterface.requestWithdraw(_ctx, new WithdrawRequest(_ctx));
        } else {
            onThirdpartyWithdrawFail();
        }
    }

    private void doGetUserCoin() {

        MGCApiUtil.getUserCoin(_ctx, new HttpCallbackDecode<GetUserCoinResultBean>(_ctx, null) {
            @Override
            public void onDataSuccess(GetUserCoinResultBean data) {
                if (data != null) {
                    onGetUserCoinOK(data);
                } else {
                    hintRetryGetUserCoin();
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);

                // hint retry, or debug
                if (Constant.FAKE_DATA) {
                    onGetUserCoinOK(GetUserCoinResultBean.debugFakeData());
                } else {
                    hintRetryGetUserCoin();
                }
            }
        });
    }

    private void onGetUserCoinOK(GetUserCoinResultBean data) {
        _model = data;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // update labels
                _totalCoinLabel.setText(String.valueOf(_model.getCoins()));
                _todayCoinLabel.setText(String.valueOf(_model.getToday_coins()));
                _moneyLabel.setText(String.format("%.02f%s", (float) _model.getCoins() / MGCSharedModel.coinRmbRatio, _leto_mgc_dollar));

                // dismiss dialog
                DialogUtil.dismissDialog();
            }
        });
    }

    private void hintRetryGetUserCoin() {
        DialogUtil.dismissDialog();
        MGCDialogUtil.showRetryDialog(_ctx, _leto_mgc_failed_get_user_coin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    DialogUtil.showDialog(_ctx, _loading);
                    doGetUserCoin();
                }
            }
        });
    }
}
