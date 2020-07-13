package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.leto.game.base.config.AppConfig;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.event.ShowBackEvent;
import com.mgc.letobox.happy.event.TabSwitchEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Create by zhaozhihui on 2019-09-06
 **/
public class TabMeFragment extends BaseFragment {

    TextView tv_title;

    Fragment fragment;
    private ImageView _btnBack;

    private int _backTabId;
    private boolean _backVisible;

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

        // register event bus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        tv_title = view.findViewById(R.id.tv_title);
        _btnBack = view.findViewById(R.id.back);
        _btnBack.setVisibility(_backVisible ? View.VISIBLE : View.GONE);

        // 返回到需要返回的tab
        _btnBack.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                EventBus.getDefault().post(new TabSwitchEvent(-1, _backTabId));
                _btnBack.setVisibility(View.GONE);
                return true;
            }
        });

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
    public void onDestroyView() {
        super.onDestroyView();

        // unregister event bus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public  void  onResume(){
        super.onResume();

        fragment.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        // 如果切换了, back按钮也就不要再显示了
        if(hidden) {
            _backVisible = false;
            if(_btnBack != null) {
                _btnBack.setVisibility(View.GONE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onShowBack(ShowBackEvent event) {
        _backTabId = event.backTabId;
        _backVisible = true;
        if(_btnBack != null) {
            _btnBack.setVisibility(View.VISIBLE);
        }
    }
}
