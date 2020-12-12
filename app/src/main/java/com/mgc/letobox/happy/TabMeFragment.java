package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgc.leto.game.base.config.AppConfig;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.BaseAppUtil;


/**
 * Create by zhaozhihui on 2019-09-06
 **/
public class TabMeFragment extends BaseFragment {

    Fragment fragment;

    @Keep
    public static TabMeFragment newInstance() {
        TabMeFragment fragment = new TabMeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        AppConfig appConfig = new AppConfig(BaseAppUtil.getChannelID(getActivity()), LoginManager.getUserId(getActivity()));

        // install content fragment
        fragment = MeNewFragment.create(appConfig);
        getChildFragmentManager().beginTransaction()
                .add(R.id.home_me, fragment)
                .commit();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (fragment != null) {
            fragment.onDestroyView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LetoTrace.d("TabMeFragment", "onResume");
        if (fragment != null) {
            fragment.onResume();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        LetoTrace.d("TabMeFragment", "onPause");
        if (fragment != null) {
            fragment.onPause();
        }
    }
}
