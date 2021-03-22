package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgc.leto.game.base.LetoEvents;
import com.mgc.leto.game.base.api.constant.Constant;
import com.mgc.leto.game.base.bean.LoginResultBean;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.mgc.ExchangeActivity;
import com.mgc.leto.game.base.mgc.WithdrawActivity;
import com.mgc.leto.game.base.mgc.bean.GetUserCoinResultBean;
import com.mgc.leto.game.base.mgc.model.MGCSharedModel;
import com.mgc.leto.game.base.mgc.thirdparty.IWithdraw;
import com.mgc.leto.game.base.mgc.thirdparty.WithdrawRequest;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil;
import com.mgc.leto.game.base.utils.DialogUtil;
import com.mgc.leto.game.base.utils.GlideUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.me.bean.MeModuleBean;


public class UserCoinHolder extends CommonViewHolder<MeModuleBean> {
    // views
    private TextView _titleLabel;
    private ImageView _headerView;
    private View _spaceBar;
    private FrameLayout _taskView;
    private FrameLayout _inviteView;
    private ImageView _lotteyView;
    private TextView _coinView, _moneyView;
    private View _profileView;
    private TextView _drawcashView;
    private ImageView _redpacketView;

    // models
    private GetUserCoinResultBean _model;

    Context _ctx;


    public static UserCoinHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_reward_item_profile"), parent, false);
        return new UserCoinHolder(ctx, view);
    }

    public UserCoinHolder(Context ctx, View view) {
        super(view);

        // views
        _ctx = itemView.getContext();

        //状态栏适配高度
        View fake_status_bar = itemView.findViewById(R.id.fake_status_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fake_status_bar.getLayoutParams();
        params.height = StatusBarUtil.getStatusBarHeight(ctx);
        fake_status_bar.setLayoutParams(params);

        _spaceBar = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_split_space"));
        _taskView = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_task_view"));
        _inviteView = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_invite_view"));
        _headerView = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_header"));
        _coinView = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_coin"));
        _moneyView = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_money"));
        _lotteyView = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_lottery"));
        _drawcashView = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_bubble"));
        _profileView = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_profile"));
        _redpacketView = itemView.findViewById(MResource.getIdByName(ctx, "R.id.leto_redpacket"));

    }

    @Override
    public void onBind(final MeModuleBean signin, final int position) {
        // name & desc
        final Context ctx = itemView.getContext();

        _spaceBar.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        // withdraw click
        _profileView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
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

        updateProfile();

        // get user coin
        doGetUserCoin(ctx);

    }

    private void thirdpartyWithdraw() {

        IWithdraw withdrawInterface = LetoEvents.getThirdpartyWithdraw();
        if (withdrawInterface != null) {
            withdrawInterface.requestWithdraw(_ctx, new WithdrawRequest(_ctx));
        } else {
//            onThirdpartyWithdrawFail();
        }
    }



    private void updateProfile() {
        // get login info
        LoginResultBean loginInfo = LoginManager.getUserLoginInfo(_ctx);
        if (loginInfo != null) {
            // if temp account, use default avatar builtin
            // if not, load avatar
            if (!LoginManager.isSignedIn(_ctx)) {
                // avatar
                _headerView.setImageDrawable(_ctx.getResources().getDrawable(MResource.getIdByName(_ctx, "R.drawable.leto_mgc_default_avatar")));
            } else {
                // avatar
                if (TextUtils.isEmpty(loginInfo.getPortrait())) {
                    _headerView.setImageDrawable(_ctx.getResources().getDrawable(MResource.getIdByName(_ctx, "R.drawable.leto_mgc_no_avatar")));
                } else {
                    GlideUtil.loadCircle(_ctx, loginInfo.getPortrait(), _headerView);
                }
            }
        }
    }

    private void doGetUserCoin(final Context _ctx) {

        MGCApiUtil.getUserCoin(_ctx, new HttpCallbackDecode<GetUserCoinResultBean>(_ctx, null) {
            @Override
            public void onDataSuccess(GetUserCoinResultBean data) {
                if (data != null) {
                    onGetUserCoinOK(data);
                } else {
                    hintRetryGetUserCoin(_ctx);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);

                // hint retry, or debug
                if (Constant.FAKE_DATA) {
                    onGetUserCoinOK(GetUserCoinResultBean.debugFakeData());
                } else {
                    hintRetryGetUserCoin(_ctx);
                }
            }
        });
    }

    private void onGetUserCoinOK(GetUserCoinResultBean data) {
        _model = data;

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                if (_coinView != null) {
                    _coinView.setText(String.valueOf(MGCSharedModel.myCoin));
                }

                if (_moneyView != null) {
                    _moneyView.setText(String.format("%.02f元",
                            (float) MGCSharedModel.myCoin / MGCSharedModel.coinRmbRatio));

                }

                // dismiss dialog
                DialogUtil.dismissDialog();
            }
        });
    }

    private void hintRetryGetUserCoin(final Context _ctx) {
        DialogUtil.dismissDialog();
        MGCDialogUtil.showRetryDialog(_ctx, "获取金币失败", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    DialogUtil.showDialog(_ctx, "");
                    doGetUserCoin(_ctx);
                }
            }
        });
    }

}