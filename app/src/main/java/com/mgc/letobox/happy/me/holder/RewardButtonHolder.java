package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leto.reward.LetoRewardManager;
import com.leto.reward.activity.LetoStepActivity;
import com.leto.reward.constant.RewardType;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.me.adapter.RewardButtonAdapter;
import com.mgc.letobox.happy.me.bean.MeModuleBean;
import com.mgc.letobox.happy.me.bean.RewardButtonBean;

import java.util.ArrayList;
import java.util.List;

public class RewardButtonHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    RecyclerView _recyclerView;

    Context _context;

    RewardButtonAdapter _adapter;

    List<RewardButtonBean> _rewardButtonList;


    public static RewardButtonHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_reward_button"), parent, false);
        return new RewardButtonHolder(ctx, view);
    }

    public RewardButtonHolder(Context context, View view) {
        super(view);

        _context = context;
        _splitSpace = itemView.findViewById(MResource.getIdByName(context, "R.id.split_space"));

        this._recyclerView = view.findViewById(MResource.getIdByName(context, "R.id.leto_recyclerView"));

        _rewardButtonList = new ArrayList<>();

        RewardButtonBean button1 = new RewardButtonBean();
        RewardButtonBean button2 = new RewardButtonBean();
        RewardButtonBean button3 = new RewardButtonBean();
        RewardButtonBean button4 = new RewardButtonBean();
        RewardButtonBean button5 = new RewardButtonBean();
        button1.setType(RewardType.REWARD_TYPE_FOOD);
        button1.setName("吃饭赚钱");
        button1.setResId(R.mipmap.leto_reward_button_food);
        button2.setType(RewardType.REWARD_TYPE_DRINKING);
        button2.setName("喝水赚钱");
        button2.setResId(R.mipmap.leto_reward_button_drink);
        button3.setType(RewardType.REWARD_TYPE_SLEEPING);
        button3.setName("睡觉赚钱");
        button3.setResId(R.mipmap.leto_reward_button_sleep);

        button4.setName("新手任务");
        button4.setResId(R.mipmap.leto_reward_button_newer_task);
        button4.setType(RewardType.REWARD_TYPE_TASK_NEWER);
        button5.setName("日常任务");
        button5.setResId(R.mipmap.leto_reward_button_daily_task);
        button5.setType(RewardType.REWARD_TYPE_TASK_DAILY);

        _rewardButtonList.add(button1);
        _rewardButtonList.add(button2);
        _rewardButtonList.add(button3);
        _rewardButtonList.add(button4);
        _rewardButtonList.add(button5);

        _adapter = new RewardButtonAdapter(_context, _rewardButtonList);

        _recyclerView.setLayoutManager(new GridLayoutManager(_context, 5));

        _recyclerView.setAdapter(_adapter);

    }

    @Override
    public void onBind(final MeModuleBean signin, final int position) {
        // name & desc
//        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        _splitSpace.setVisibility( View.GONE );
    }
}