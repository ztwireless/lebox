package com.mgc.letobox.happy.follow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.follow.bean.FollowRankUser;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Create by zhaozhihui on 2020-03-05
 **/
public class FollowRankAdapter extends BaseQuickAdapter<FollowRankUser, BaseViewHolder> {

    private Context mContext;

    public FollowRankAdapter(Context context, List data) {
        super(R.layout.item_follow_rank, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowRankUser item) {

        int position = helper.getLayoutPosition();

        if (position % 2 == 1) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(DensityUtil.dip2px(mContext, 4));
            drawable.setColor(Color.parseColor("#FFFFF8F5"));
            helper.itemView.setBackground(drawable);
        } else {
            helper.itemView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        }


        ((TextView) helper.getView(R.id.tv_name)).setText(item.nickname);

        SpannableString inviteNumString = new SpannableString(String.valueOf(item.invate_mem_count) + "人");
        inviteNumString.setSpan(new ForegroundColorSpan(0xFFFE6118), 0, String.valueOf(item.invate_mem_count).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) helper.getView(R.id.tv_invite_count)).setText(inviteNumString);

        String award =  new DecimalFormat("#.##").format(item.invate_mem_award);
        SpannableString incomingString = new SpannableString(award + "(元)");
        incomingString.setSpan(new ForegroundColorSpan(0xFFFE6118), 0, award.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) helper.getView(R.id.tv_invite_award)).setText(incomingString);

        GlideUtil.loadRoundedCorner(mContext,
                item.portrait,
                (ImageView) helper.getView(R.id.iv_avatar),
                12,
                R.mipmap.default_avatar);
    }
}
