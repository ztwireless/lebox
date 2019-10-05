package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ledong.lib.leto.widget.ClickGuard;
import com.ledong.lib.minigame.GameCenterHomeFragment;
import com.ledong.lib.minigame.SearchActivity;
import com.leto.game.base.util.IntentConstant;

/**
 * Create by zhaozhihui on 2019-09-06
 **/
public class TabMiniGameFragment extends BaseFragment {

    TextView tv_title;
    RelativeLayout rl_search;

    private int _gameCenterPosId;

    @Keep
    public static TabMiniGameFragment newInstance() {
        return TabMiniGameFragment.newInstance(17);
    }

    @Keep
    public static TabMiniGameFragment newInstance(int gameCenterPosId) {
        TabMiniGameFragment fragment = new TabMiniGameFragment();
        Bundle args = new Bundle();
        args.putInt(IntentConstant.GAME_CENTER_POS_ID, gameCenterPosId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_minigame, container, false);

        rl_search = view.findViewById(R.id.rl_search);


        rl_search.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                SearchActivity.start(getActivity());

                return true;
            }
        });


        Bundle arguments = getArguments();
        if (arguments != null) {
            _gameCenterPosId = arguments.getInt(IntentConstant.GAME_CENTER_POS_ID, 0);
        }

        // install content fragment
        Fragment fragment = GameCenterHomeFragment.getInstance(_gameCenterPosId);
        getChildFragmentManager().beginTransaction()
                .add(R.id.home_content, fragment)
                .commit();

        return view;
    }

}
