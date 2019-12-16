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
import com.mgc.letobox.happy.event.ShowBackEvent;
import com.mgc.letobox.happy.event.TabSwitchEvent;
import com.mgc.letobox.happy.model.SharedData;

import org.greenrobot.eventbus.EventBus;

/**
 * Create by zhaozhihui on 2019-09-06
 **/
public class TabMiniGameFragment extends BaseFragment {

    TextView tv_title;
    RelativeLayout rl_search;
    private View _meContainer;

    private int _gameCenterPosId;

    @Keep
    public static TabMiniGameFragment newInstance() {
        return TabMiniGameFragment.newInstance(SharedData.MGC_HOME_TAB_ID);
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
        _meContainer = view.findViewById(R.id.me_container);

        rl_search.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                SearchActivity.start(getActivity());
                return true;
            }
        });

        _meContainer.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                EventBus.getDefault().post(new TabSwitchEvent(-1, R.id.tab_me));
                EventBus.getDefault().postSticky(new ShowBackEvent(R.id.tab_game));
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
