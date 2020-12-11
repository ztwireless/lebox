package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledong.lib.leto.main.LetoFragment;
import com.mgc.leto.game.base.interfaces.ILetoGameContainer;
import com.mgc.leto.game.base.main.BaseFragment;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.IntentConstant;

/**
 * 单款游戏Fragment
 **/
public class TabGameFragment extends BaseFragment {
    private static final String TAG = TabGameFragment.class.getSimpleName();
    private int _gameCenterPosId;
    private LetoFragment _fragment;

    private boolean mIsUserVisibleHint = true;

    @Keep
    public static TabGameFragment newInstance() {
        return TabGameFragment.newInstance(17);
    }

    @Keep
    public static TabGameFragment newInstance(int gameCenterPosId) {
        TabGameFragment fragment = new TabGameFragment();
        Bundle args = new Bundle();
        args.putInt(IntentConstant.GAME_CENTER_POS_ID, gameCenterPosId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game, container, false);


        // get extra
        Bundle extra = getArguments();
        String appId = extra.getString(IntentConstant.APP_ID);
        appId = "1000025";

        // add fragment
        _fragment = LetoFragment.create(getContext(), appId, true);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.leto_container, _fragment)
                .commit();
        return view;
    }


    public ILetoGameContainer getLetoContainer() {
        return _fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        LetoTrace.d(TAG, "onPause");
        if (_fragment != null) {
            _fragment.onPause();
        }
    }

    @Override
    public void onResume() {
        LetoTrace.d(TAG, "onResume");
        super.onResume();
        if (mIsUserVisibleHint) {
            LetoTrace.d(TAG, "set gameFragment onResume");
            if (_fragment != null) {
                _fragment.onResume();
            }
        }
    }

    /**
     * 当fragment结合viewpager时 会调用此方法
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LetoTrace.d(TAG, "setUserVisibleHint: " + isVisibleToUser);

        mIsUserVisibleHint = isVisibleToUser;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mIsUserVisibleHint = !hidden;
        LetoTrace.d(TAG, "onHiddenChanged: " + hidden);

        if (_fragment != null) {
            if (hidden) {
                _fragment.pauseContainer();
            } else {
                _fragment.resumeContainer();
            }
        }
    }

}
