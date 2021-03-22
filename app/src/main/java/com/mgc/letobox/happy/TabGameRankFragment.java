package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ledong.lib.minigame.GameCenterRankFragment;
import com.mgc.leto.game.base.utils.IntentConstant;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.letobox.happy.model.SharedData;

/**
 * Create by zhaozhihui on 2019-09-06
 **/
public class TabGameRankFragment extends BaseFragment {
    TextView tv_title;

    private int _gameCenterPosId;

    private String title;

    @Keep
    public static TabGameRankFragment newInstance() {
        return TabGameRankFragment.newInstance(SharedData.MGC_RANK_TAB_ID, "榜单");
    }

    @Keep
    public static TabGameRankFragment newInstance(int gameCenterPosId, String title) {
        TabGameRankFragment fragment = new TabGameRankFragment();
        Bundle args = new Bundle();
        args.putInt(IntentConstant.GAME_CENTER_POS_ID, gameCenterPosId);
        args.putString(IntentConstant.TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_list, container, false);

        tv_title = view.findViewById(R.id.tv_title);

        //状态栏适配高度
        View fake_status_bar = view.findViewById(R.id.fake_status_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fake_status_bar.getLayoutParams();
        params.height = StatusBarUtil.getStatusBarHeight(getContext());
        fake_status_bar.setLayoutParams(params);

        Bundle arguments = getArguments();
        if (arguments != null) {
            _gameCenterPosId = arguments.getInt(IntentConstant.GAME_CENTER_POS_ID, 0);
            title = arguments.getString(IntentConstant.TITLE);
            if (!TextUtils.isEmpty(title)) {
                tv_title.setText(title);
            }
        }

        // install content fragment
        Fragment fragment = GameCenterRankFragment.getInstance(_gameCenterPosId);
        getChildFragmentManager().beginTransaction()
                .add(R.id.home_content, fragment)
                .commit();

        return view;
    }

}
