package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ledong.lib.leto.Leto;
import com.leto.reward.LetoRewardManager;
import com.leto.reward.RewardGridView;
import com.leto.reward.constant.RewardType;
import com.mgc.leto.game.base.utils.GlideUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.me.bean.MeModuleBean;

public class RewardGameHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
//    RecyclerView _recyclerView;

    Context _context;

    FrameLayout _rewardLayout;
    ImageView _banbanLayout;

    ImageView leto_turntable;
    ImageView leto_answer;
    ImageView leto_scratch_card;

    public static RewardGameHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_reward_game"), parent, false);
        return new RewardGameHolder(ctx, view);
    }

    public RewardGameHolder(Context context, View view) {
        super(view);

        _context = context;
        _splitSpace = itemView.findViewById(MResource.getIdByName(context, "R.id.split_space"));

//        this._recyclerView = view.findViewById(MResource.getIdByName(context, "R.id.recyclerView"));
        this.leto_answer = view.findViewById(MResource.getIdByName(context, "R.id.leto_answer"));
        this.leto_scratch_card = view.findViewById(MResource.getIdByName(context, "R.id.leto_scratch_card"));
        this.leto_turntable = view.findViewById(MResource.getIdByName(context, "R.id.leto_turntable"));

        GlideUtil.load(context, MResource.getIdByName(context, "R.drawable.leto_reward_turntable"), this.leto_turntable);
        GlideUtil.load(context, MResource.getIdByName(context, "R.drawable.leto_reward_answer"), this.leto_answer);
        GlideUtil.load(context, MResource.getIdByName(context, "R.drawable.leto_reward_scratch_card_big"), this.leto_scratch_card);

        leto_turntable.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                LetoRewardManager.startDanzhuanpan(_context);
                return true;
            }
        });
        leto_scratch_card.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                LetoRewardManager.startGuaGuaCard(_context);
                return true;
            }
        });
        leto_answer.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                LetoRewardManager.startDaTi(_context, true);
                return true;
            }
        });
    }

    @Override
    public void onBind(final MeModuleBean signin, final int position) {
        // name & desc
//        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        _splitSpace.setVisibility(View.GONE);
    }
}