package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ledong.lib.leto.Leto;
import com.leto.reward.RewardGridView;
import com.leto.reward.constant.RewardType;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.letobox.happy.me.bean.MeModuleBean;

public class RewardGridHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
//    RecyclerView _recyclerView;

    Context _context;

    FrameLayout _rewardLayout;

    public static RewardGridHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_reward_grid"), parent, false);
        return new RewardGridHolder(ctx, view);
    }

    public RewardGridHolder(Context context, View view) {
        super(view);

        _context = context;
        _splitSpace = itemView.findViewById(MResource.getIdByName(context, "R.id.split_space"));
        this._rewardLayout = view.findViewById(MResource.getIdByName(context, "R.id.reward_grid"));

        RewardGridView _rewardView;

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        flp.gravity = Gravity.CENTER_HORIZONTAL;

        int [] rewardList = new int[]{

                RewardType.REWARD_TYPE_GUESS_SINGLE, RewardType.REWARD_TYPE_IDIOM,
        };
        _rewardView = new RewardGridView(context,rewardList);
        _rewardLayout.addView(_rewardView, flp );


    }

    @Override
    public void onBind(final MeModuleBean signin, final int position) {
        // name & desc
//        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        _splitSpace.setVisibility( View.GONE );
    }
}