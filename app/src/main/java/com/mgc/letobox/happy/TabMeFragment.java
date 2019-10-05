package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ledong.lib.leto.config.AppConfig;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;

/**
 * Create by zhaozhihui on 2019-09-06
 **/
public class TabMeFragment extends BaseFragment {

    TextView tv_title;

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
        View view = inflater.inflate(R.layout.fragment_game_list, container, false);


        tv_title = view.findViewById(R.id.tv_title);

        tv_title.setText("我的");

        AppConfig appConfig = new AppConfig(BaseAppUtil.getChannelID(getActivity()), LoginManager.getUserId(getActivity()));

        // install content fragment
        fragment = MeNewFragment.create(appConfig);
        getChildFragmentManager().beginTransaction()
                .add(R.id.home_content, fragment)
                .commit();

        return view;
    }

    @Override
    public  void  onResume(){
        super.onResume();

        fragment.onResume();
    }

}
