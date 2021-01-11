package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ledong.lib.minigame.bean.GameCenterData_Signin;
import com.ledong.lib.minigame.bean.SigninStatus;
import com.mgc.leto.game.base.api.constant.Constant;
import com.mgc.leto.game.base.mgc.bean.CoinDialogScene;
import com.mgc.leto.game.base.mgc.dialog.IMGCCoinDialogListener;
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil;
import com.mgc.leto.game.base.statistic.GameStatisticManager;
import com.mgc.leto.game.base.statistic.StatisticEvent;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.GlideUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.widget.ClickGuard;


public class SigninHolder extends CommonViewHolder<GameCenterData_Signin> {
    private ImageView iv_picture;
    private TextView tv_coin;
    private TextView tv_status, video_multiple;
    private LinearLayout video_tag;

    private int _style;

    ViewGroup _adContainer;


    public static SigninHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_list_item_game_signin"), parent, false);
        return new SigninHolder(view);
    }

    public SigninHolder(View view) {
        super(view);
        Context ctx = view.getContext();
        this.tv_coin = view.findViewById(MResource.getIdByName(ctx, "R.id.tv_coin"));
        this.iv_picture = view.findViewById(MResource.getIdByName(ctx, "R.id.picture"));
        this.tv_status = view.findViewById(MResource.getIdByName(ctx, "R.id.tv_sign_status"));
        this.video_tag = view.findViewById(MResource.getIdByName(ctx, "R.id.ll_video_tag"));
        this.video_multiple = view.findViewById(MResource.getIdByName(ctx, "R.id.video_multiple"));

    }

    @Override
    public void onBind(final GameCenterData_Signin signin, final int position) {
        // name & desc
        final Context ctx = itemView.getContext();

        itemView.setOnClickListener(null);

        int status = signin.getStatus();
        String weekDate = getWeek(signin.getDay());

        if (signin.getIs_today() == 1) {

            if (status == SigninStatus.SIGNIN_UNRECEIVED.getValue()) {
                tv_status.setText("已签");
                tv_status.setTextColor(ColorUtil.parseColor("#FFFFCC4D"));

                video_tag.setBackgroundResource(MResource.getIdByName(ctx, "R.drawable.leto_mgc_signin_video_tag_bg_red"));
                video_tag.setVisibility(View.VISIBLE);
                video_multiple.setText("" + signin.getMultiple_reward());

                itemView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                    @Override
                    public boolean onClicked() {
                        signin(ctx, signin);

                        reportClick(ctx);

                        return true;
                    }
                });


            } else if (status == SigninStatus.SIGNIN_RECEIVED.getValue()) {
                tv_status.setText("已签");
                tv_status.setTextColor(ColorUtil.parseColor("#FFCBD6D3"));

                video_tag.setBackgroundResource(MResource.getIdByName(ctx, "R.drawable.leto_mgc_signin_video_tag_bg"));
                video_tag.setVisibility(View.GONE);

            } else if (status == SigninStatus.SIGNIN_VIDEO_RECEIVED.getValue()) {
                tv_status.setText("已签");
                tv_status.setTextColor(ColorUtil.parseColor("#FFCBD6D3"));

                video_tag.setBackgroundResource(MResource.getIdByName(ctx, "R.drawable.leto_mgc_signin_video_tag_bg"));
                video_tag.setVisibility(View.VISIBLE);
                video_multiple.setText("" + signin.getMultiple_reward());

            } else {
                tv_status.setText("今天");
                tv_status.setTextColor(ColorUtil.parseColor("#FFFFCC4D"));
                video_tag.setBackgroundResource(MResource.getIdByName(ctx, "R.drawable.leto_mgc_signin_video_tag_bg_red"));
                video_tag.setVisibility(View.VISIBLE);
                video_multiple.setText("" + signin.getMultiple_reward());

                itemView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                    @Override
                    public boolean onClicked() {
                        signin(ctx, signin);

                        reportClick(ctx);

                        return true;
                    }
                });
            }
        } else {
            if (status == SigninStatus.UNSIGNIN.getValue()) {
                tv_status.setText(weekDate);
                tv_status.setTextColor(ColorUtil.parseColor("#FFFFCC4D"));
                video_tag.setBackgroundResource(MResource.getIdByName(ctx, "R.drawable.leto_mgc_signin_video_tag_bg_red"));
                video_tag.setVisibility(View.VISIBLE);
                video_multiple.setText("" + signin.getMultiple_reward());
            } else if (status == SigninStatus.UNABLE.getValue()) {
                tv_status.setText(weekDate);
                tv_status.setTextColor(ColorUtil.parseColor("#FFCBD6D3"));
                video_tag.setBackgroundResource(MResource.getIdByName(ctx, "R.drawable.leto_mgc_signin_video_tag_bg"));
                video_tag.setVisibility(View.GONE);

            } else if (status > SigninStatus.UNSIGNIN.getValue()) {
                tv_status.setTextColor(ColorUtil.parseColor("#FFCBD6D3"));
                tv_status.setText("已签");

                if (status == SigninStatus.SIGNIN_VIDEO_RECEIVED.getValue()) {
                    video_tag.setBackgroundResource(MResource.getIdByName(ctx, "R.drawable.leto_mgc_signin_video_tag_bg"));
                    video_tag.setVisibility(View.VISIBLE);
                    video_multiple.setText("" + signin.getMultiple_reward());
                } else if (status == SigninStatus.SIGNIN_RECEIVED.getValue()) {
                    video_tag.setBackgroundResource(MResource.getIdByName(ctx, "R.drawable.leto_mgc_signin_video_tag_bg"));
                    video_tag.setVisibility(View.GONE);
                } else {
                    video_tag.setBackgroundResource(MResource.getIdByName(ctx, "R.drawable.leto_mgc_signin_video_tag_bg_red"));
                    video_tag.setVisibility(View.VISIBLE);
                    video_multiple.setText("" + signin.getMultiple_reward());
                }
            }
        }

        GlideUtil.load(ctx, signin.getCoins_pic(), iv_picture);
    }



    public void setAdContainer(ViewGroup adContainer){
        _adContainer = adContainer;
    }



    private String getWeek(int week) {

        switch (week) {

            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 7:
                return "周日";
            default:
                return "周一";
        }
    }

    private void signin(final Context ctx, GameCenterData_Signin signin) {
        MGCDialogUtil.showMGCCoinDialogWithAdContainer(ctx, null, signin.getSign_coins(), signin.getMultiple_reward(), CoinDialogScene.SIGN_IN, _adContainer, new IMGCCoinDialogListener() {
            @Override
            public void onExit(boolean video, int coinGot) {

            }
        });
    }

    private void reportClick(Context ctx){
        String benefit_type = Constant.BENEFITS_TYPE_REWARD_SIGN;
        GameStatisticManager.statisticBenefitLog(ctx, BaseAppUtil.getChannelID(ctx), StatisticEvent.LETO_BENEFITS_MODULE_CLICK.ordinal(),
                0, 0, 0, 0, benefit_type, 0);
    }
}