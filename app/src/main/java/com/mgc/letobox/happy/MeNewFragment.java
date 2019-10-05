package com.mgc.letobox.happy;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledong.lib.leto.config.AppConfig;
import com.leto.game.base.db.LoginControl;
import com.leto.game.base.event.DataRefreshEvent;
import com.leto.game.base.event.GetCoinEvent;
import com.leto.game.base.util.GameUtil;
import com.leto.game.base.util.IntentConstant;
import com.leto.game.base.util.MResource;
import com.mgc.letobox.happy.me.adapter.MeHomeAdapter;
import com.mgc.letobox.happy.me.bean.MeModuleBean;
import com.mgc.letobox.happy.util.LeBoxConstant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MeNewFragment extends Fragment {
    // views
    private RecyclerView _recyclerView;
    private SwipeRefreshLayout _refreshLayout;
    View _rootView;

    ViewGroup _adContainer;

    MeHomeAdapter _meHomeAdapter;

    // tracking login info version
    private int _loginInfoVersion;

    @Keep
    public static MeNewFragment create(AppConfig appConfig) {
        MeNewFragment f = new MeNewFragment();
        Bundle args = new Bundle();
        args.putParcelable(IntentConstant.EXTRA_APPCONFIG, appConfig);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get login info version
        _loginInfoVersion = getLoginInfoVersion();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // load view
        Context ctx = getActivity();
        _rootView = inflater.inflate(R.layout.leto_mgc_me_new_fragment, container, false);
        _refreshLayout = _rootView.findViewById(MResource.getIdByName(getActivity(), "R.id.refreshLayout"));
        _recyclerView = _rootView.findViewById(MResource.getIdByName(ctx, "R.id.recyclerView"));
        _adContainer = _rootView.findViewById(MResource.getIdByName(ctx, "R.id.ad_container"));

        _refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (_meHomeAdapter != null) {
                    // clear task list so that it will be reloaded
                    NewerTaskManager.mTaskBeanList.clear();

                    // reload
                    _meHomeAdapter.notifyDataSetChanged();
                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        _refreshLayout.setRefreshing(false);
                    }
                });

            }
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        _meHomeAdapter = new MeHomeAdapter(getActivity());
        _meHomeAdapter.setAdContainer(_adContainer);
        _meHomeAdapter.setFragment(this);

        initModules();

        _recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        _recyclerView.setAdapter(_meHomeAdapter);

        // return
        return _rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        // update if login info changed
        if(isLoginInfoUpdated(_loginInfoVersion)) {
            // clear task list so that it will be reloaded
            NewerTaskManager.mTaskBeanList.clear();

            // now reload list
            _meHomeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        // update
        if(!hidden && isLoginInfoUpdated(_loginInfoVersion)) {
            _meHomeAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void refreshCoin(GetCoinEvent coinEvent) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                _meHomeAdapter.notifyDataSetChanged();
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void refreshGame(DataRefreshEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                _meHomeAdapter.notifyDataSetChanged();
            }
        });

    }

    private boolean isLoginInfoUpdated(int ver) {
        return getLoginInfoVersion() > ver;
    }

    private int getLoginInfoVersion() {
        return GameUtil.loadInt(getContext(), LoginControl.FILE_LOGIN_INFO_VERSION);
    }

    private void initModules() {
        List<MeModuleBean> moduleBeanList = new ArrayList<>();
        moduleBeanList.add(new MeModuleBean(LeBoxConstant.LETO_ME_MODULE_COIN));
        moduleBeanList.add(new MeModuleBean(LeBoxConstant.LETO_ME_MODULE_MYGAMES));
        moduleBeanList.add(new MeModuleBean(LeBoxConstant.LETO_ME_MODULE_SIGININ));
        moduleBeanList.add(new MeModuleBean(LeBoxConstant.LETO_ME_MODULE_NEWER_TASK));
        //moduleBeanList.add(new MeModuleBean(LeBoxConstant.LETO_ME_MODULE_HIGH_COIN_TASK));
        moduleBeanList.add(new MeModuleBean(LeBoxConstant.LETO_ME_MODULE_DAILY_TASK));
        moduleBeanList.add(new MeModuleBean(LeBoxConstant.LETO_ME_MODULE_OTHER));

        _meHomeAdapter.setModels(moduleBeanList);

    }
}
