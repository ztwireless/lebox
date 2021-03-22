package com.mgc.letobox.happy;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ledong.lib.leto.Leto;
import com.ledong.lib.minigame.GameCenterHomeFragment;
import com.ledong.lib.minigame.SearchActivity;
import com.ledong.lib.minigame.event.HideTitleEvent;
import com.ledong.lib.minigame.view.RookieGuideView;
import com.leto.game.base.event.ShowRookieGiftEvent;
import com.mgc.leto.game.base.LetoEvents;
import com.mgc.leto.game.base.api.constant.Constant;
import com.mgc.leto.game.base.interfaces.ILetoGameContainer;
import com.mgc.leto.game.base.listener.ILetoLifecycleListener;
import com.mgc.leto.game.base.mgc.model.MGCSharedModel;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.IntentConstant;
import com.mgc.leto.game.base.utils.MainHandler;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.event.ShowBackEvent;
import com.mgc.letobox.happy.event.ShowRookieGuideEvent;
import com.mgc.letobox.happy.event.TabSwitchEvent;
import com.mgc.letobox.happy.model.SharedData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 小游戏中心Fragment
 **/
public class TabMiniGameFragment extends BaseFragment implements RookieGuideView.RookieGuideClickListener, ILetoLifecycleListener, View.OnLayoutChangeListener {

    TextView tv_title;
    RelativeLayout rl_search;
    private View _meContainer;

    private LinearLayout rl_title;
    private LinearLayout splitView;

    private int _gameCenterPosId;
    private GameCenterHomeFragment _fragment;

    // flag to avoid multiple jump
    private boolean _guideClicked;

    // check rookie guide max retry
    private int _checkRookieRetry = 10;

    // main game pos
    private int _rookieGuideGamePos = -1;

    private Runnable _checkRookieGuideRunnable = new Runnable() {
        @Override
        public void run() {
            // 如果超过最大重试次数, 返回
            if (_checkRookieRetry <= 0) {
                return;
            }
            _checkRookieRetry--;

            // check fragment
            if (_fragment == null || getActivity() == null) {
                MainHandler.getInstance().postDelayed(_checkRookieGuideRunnable, 500);
                return;
            }

            // check data
            _rookieGuideGamePos = _fragment.findPositionByStyle(Constant.GAME_LIST_SINGLE_ROTATION_CHART);
            if (_rookieGuideGamePos == -1) {
                MainHandler.getInstance().postDelayed(_checkRookieGuideRunnable, 500);
                return;
            }

            // find view
            View view = _fragment.getViewAtPosition(_rookieGuideGamePos);
            if (view == null) {
                MainHandler.getInstance().postDelayed(_checkRookieGuideRunnable, 500);
                return;
            }

            // get screen rect and show rookie guide view
            int[] loc = new int[2];
            view.getLocationOnScreen(loc);
            Point center = new Point(loc[0] + view.getWidth() / 2, loc[1] + view.getHeight() / 2);
            RookieGuideView.show(getActivity(), center, view.getWidth() / 4, TabMiniGameFragment.this);
        }
    };

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

        // register event bus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        rl_title = view.findViewById(R.id.rl_title);
        splitView = view.findViewById(R.id.splitline);

        //状态栏适配高度
        View fake_status_bar = view.findViewById(R.id.fake_status_bar);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) fake_status_bar.getLayoutParams();
        params.height = StatusBarUtil.getStatusBarHeight(getActivity());
        fake_status_bar.setLayoutParams(params);

        //如果盒子 红包开关打开了，则隐藏上部搜索栏
        if (!MGCSharedModel.isShowGameCenterTitle) {
            rl_title.setVisibility(View.GONE);
            splitView.setVisibility(View.GONE);
        }

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
        _fragment = GameCenterHomeFragment.getInstance(_gameCenterPosId, true);
        _fragment.setMgcActivity(true);
        getChildFragmentManager().beginTransaction()
                .add(R.id.game_ceneter_content, _fragment)
                .commit();

        // 添加自己为leto生命周期监听器以便弹出新手红包
        LetoEvents.addLetoLifecycleListener(this);

        // 添加一个layout监听器, 因为如果网络较慢, 一开始游戏的大图不会显示, 会显示占位图, 这样新手引导的位置
        // 就会偏上, 一旦大图加载完成, 就不对了, 所以需要重新计算新手引导位置
        _fragment.setExternalLayoutListener(this);

        return view;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onHideTitle(final HideTitleEvent event) {
        if (event == null) {
            return;
        }
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        hideTitle(event._isHide);
                    } catch (Throwable t) {

                    }
                }
            });
        }
    }


    public void hideTitle(boolean isHide) {
        if (rl_title != null) {
            if (isHide && rl_title.getVisibility() == View.VISIBLE) {
                rl_title.setVisibility(View.GONE);
                splitView.setVisibility(View.GONE);
            } else if (!isHide && rl_title.getVisibility() == View.GONE) {
                rl_title.setVisibility(View.VISIBLE);
                splitView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unregister event bus
        EventBus.getDefault().unregister(this);

        // 移除监听器
        LetoEvents.removeLetoLifecycleListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onShowRookieGuide(ShowRookieGuideEvent event) {
        // reset flag
        _guideClicked = false;
        _checkRookieRetry = 10;

        // ensure old rookie guide view is removed
        RookieGuideView.removeActive();

        // 开始检查是否可以显示新手引导, 如果不行, 隔一段时间再检查, 直到能显示为止
        MainHandler.getInstance().post(_checkRookieGuideRunnable);
    }

    @Override
    public void onRookieGuideClicked() {
        if (_fragment != null && !_guideClicked && _rookieGuideGamePos != -1) {
            _guideClicked = true;
            _fragment.performClickAtGame(_rookieGuideGamePos, 0);
        }
    }

    private void fixRookieGuideViewIfNeeded() {
        if (RookieGuideView.hasActive()) {
            // 计算新的中心
            if (_fragment == null || getActivity() == null) {
                return;
            }
            _rookieGuideGamePos = _fragment.findPositionByStyle(Constant.GAME_LIST_SINGLE_ROTATION_CHART);
            if (_rookieGuideGamePos == -1) {
                return;
            }
            View view = _fragment.getViewAtPosition(_rookieGuideGamePos);
            if (view == null) {
                return;
            }
            int[] loc = new int[2];
            view.getLocationOnScreen(loc);
            Point center = new Point(loc[0] + view.getWidth() / 2, loc[1] + view.getHeight() / 2);

            // get old circle center
            RookieGuideView v = RookieGuideView.getActive();
            Point oldCenter = v.getCircleCenter();

            // 如果新的中心不一样, 重新显示rookie guide
            if (!center.equals(oldCenter)) {
                RookieGuideView.removeActive();
                RookieGuideView.show(getActivity(), center, view.getWidth() / 4, this);
            }
        }
    }

    @Override
    public void onLetoAppLaunched(ILetoGameContainer letoContainer, String appId) {
    }

    @Override
    public void onLetoAppLoaded(ILetoGameContainer letoContainer, String appId) {
    }

    @Override
    public void onLetoAppShown(final ILetoGameContainer letoContainer, String appId) {
        // 如果是引导等待的游戏, 显示新手红包
        if (MGCSharedModel.isRookieGiftAvailable()) {
            ShowRookieGiftEvent e = new ShowRookieGiftEvent();
            e.appId = appId;
            EventBus.getDefault().postSticky(e);
        }

        // remove rookie guide view
        RookieGuideView.removeActive();
    }

    @Override
    public void onLetoAppPaused(ILetoGameContainer letoContainer, String appId) {

    }

    @Override
    public void onLetoAppResumed(ILetoGameContainer letoContainer, String appId) {

    }

    @Override
    public void onLetoAppExit(ILetoGameContainer letoContainer, String appId) {

    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        fixRookieGuideViewIfNeeded();
    }

    @Override
    public void onResume() {
        super.onResume();
        LetoTrace.d("TabMiniGameFragment", "onResume");
        if (_fragment != null) {
            _fragment.onResume();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        LetoTrace.d("TabMiniGameFragment", "onPause");
        if (_fragment != null) {
            _fragment.onPause();
        }
    }
}
