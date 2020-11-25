package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.leto.reward.OpenRedpacketView;
import com.leto.reward.RewardGridView;
import com.leto.reward.constant.RewardType;

/**
 * Create by zhaozhihui on 2019-09-06
 **/
public class TabRewardFragment extends BaseFragment {

    TextView tv_title;

    FrameLayout _redpacketLayout;
    FrameLayout _rewardLayout;
    OpenRedpacketView _redpacketView;

    RewardGridView _rewardView;

    @Keep
    public static TabRewardFragment newInstance() {
        TabRewardFragment fragment = new TabRewardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward, container, false);

        _redpacketLayout = view.findViewById(R.id.open_red_packet);
        _rewardLayout = view.findViewById(R.id.reward_grid);
        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("福利场");

        _redpacketView = new OpenRedpacketView(getActivity());
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        flp.gravity = Gravity.CENTER_HORIZONTAL;
        _redpacketLayout.addView(_redpacketView, flp );

        int [] rewardList = new int[]{
                RewardType.REWARD_TYPE_TUENTABLE, RewardType.REWARD_TYPE_IDIOM,
                RewardType.REWARD_TYPE_SCRATCH_CARD, RewardType.REWARD_TYPE_ANSWER,
                RewardType.REWARD_TYPE_GUESS_SINGLE, RewardType.REWARD_TYPE_PENDING,
        };
        _rewardView = new RewardGridView(getActivity(),rewardList);
        _rewardLayout.addView(_rewardView, flp );


        return view;
    }

}
