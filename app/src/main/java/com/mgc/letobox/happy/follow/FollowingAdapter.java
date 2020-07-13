package com.mgc.letobox.happy.follow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mgc.leto.game.base.bean.SHARE_PLATFORM;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.DensityUtil;
import com.mgc.leto.game.base.utils.GlideUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.follow.bean.FollowAwaken;
import com.mgc.letobox.happy.follow.bean.FollowingUser;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Create by zhaozhihui on 2020-03-05
 **/
public class FollowingAdapter extends BaseQuickAdapter<FollowingUser, BaseViewHolder> {

    private Context mContext;

    IFollowShareListener _shareListener;

    public FollowingAdapter(Context context, List data) {
        super(R.layout.item_my_following_user, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowingUser item) {
        LinearLayout tagLayout = helper.getView(R.id.ll_tags);
        tagLayout.removeAllViews();


        ((TextView) helper.getView(R.id.tv_username)).setText(item.nickname);

        ((TextView) helper.getView(R.id.tv_cash)).setText(new DecimalFormat("#.##").format(item.c_award) + "元");

        GlideUtil.loadRoundedCorner(mContext,
                item.portrait,
                (ImageView) helper.getView(R.id.iv_photo),
                20,
                R.mipmap.default_avatar);

        FollowAwaken awaken = item.awaken;
        if (awaken != null) {
            if(!TextUtils.isEmpty(awaken.message)) {
                TextView textView = new TextView(mContext);
                textView.setTextSize(9);
                textView.setTextColor(ColorUtil.parseColor("#FFFE6118"));
                textView.setText(awaken.message);
                textView.setPadding(DensityUtil.dip2px(mContext, 7), DensityUtil.dip2px(mContext, 3),
                        DensityUtil.dip2px(mContext, 7), DensityUtil.dip2px(mContext, 3));

                GradientDrawable drawable = new GradientDrawable();
                drawable.setCornerRadius(DensityUtil.dip2px(mContext, 2));
                drawable.setColor(Color.parseColor("#FFFFFF"));
                drawable.setStroke(1, Color.parseColor("#FFFE6118"));
                textView.setBackground(drawable);
                tagLayout.addView(textView);
            }

            if(awaken.status == 1){
                //不需要唤醒
                (helper.getView(R.id.btn_awake)).setOnClickListener(null);
                (helper.getView(R.id.btn_awake)).setBackgroundResource(MResource.getIdByName(mContext, "R.drawable.follow_awake_button_bg_gray"));
                (helper.getView(R.id.awake_award)).setVisibility(View.GONE);
            }else{
                (helper.getView(R.id.btn_awake)).setBackgroundResource(MResource.getIdByName(mContext, "R.drawable.follow_awake_button_bg"));
                (helper.getView(R.id.awake_award)).setVisibility(View.VISIBLE);

                (helper.getView(R.id.btn_awake)).setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                    @Override
                    public boolean onClicked() {
                        // 唤醒
                        doAwake();
                        return true;
                    }
                });
            }
        }else{
            (helper.getView(R.id.btn_awake)).setBackgroundResource(MResource.getIdByName(mContext, "R.drawable.follow_awake_button_bg"));
            (helper.getView(R.id.awake_award)).setVisibility(View.VISIBLE);

            (helper.getView(R.id.btn_awake)).setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                @Override
                public boolean onClicked() {
                    // 唤醒
                    doAwake();
                    return true;
                }
            });
        }

    }

    public void setShareListener(IFollowShareListener l){
        _shareListener = l;
    }

    public void doAwake(){
        if(_shareListener!=null){
            _shareListener.onShare(SHARE_PLATFORM.WEIXIN);
        }
    }
}
