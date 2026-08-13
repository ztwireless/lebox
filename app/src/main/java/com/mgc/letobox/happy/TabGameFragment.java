package com.mgc.letobox.happy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
    private Button _installButton;
    private Button _runButton;
    private Button _uninstallButton;
    private boolean _installing;

    private boolean mIsUserVisibleHint = true;

    GameCenterTabActivity _activity;

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

        _installButton = view.findViewById(R.id.apk_game_install);
        _runButton = view.findViewById(R.id.apk_game_run);
        _uninstallButton = view.findViewById(R.id.apk_game_uninstall);
        setupApkGameButtons();

        _activity = (GameCenterTabActivity) getActivity();

        _activity.registerMyTouchListener(_fragment.getTouchListenr());

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.leto_container, _fragment)
                .commit();
        return view;
    }

    private void setupApkGameButtons() {
        _installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                installApkGame();
            }
        });
        _runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchApkGame();
            }
        });
        _uninstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uninstallApkGame();
            }
        });
        refreshApkGameButtons();
    }

    private void installApkGame() {
        final Context context = getContext();
        if (context == null || _installing) {
            return;
        }
        _installing = true;
        refreshApkGameButtons();
        Toast.makeText(context, "正在安装到盒子沙盒…", Toast.LENGTH_SHORT).show();
        final Context appContext = context.getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApkSandboxManager.install(appContext);
                    showOperationResult(ApkSandboxManager.GAME_NAME + " 已安装到盒子", true);
                } catch (Throwable error) {
                    Log.e(TAG, "Unable to install game into sandbox", error);
                    showOperationResult("沙盒安装失败：" + safeMessage(error), false);
                }
            }
        }, "sandbox-game-installer").start();
    }

    private void showOperationResult(final String message, final boolean success) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _installing = false;
                refreshApkGameButtons();
                Context context = getContext();
                if (context != null) {
                    Toast.makeText(context, message, success ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void launchApkGame() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        if (!ApkSandboxManager.isInstalled()) {
            Toast.makeText(context, "请先安装 " + ApkSandboxManager.GAME_NAME, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ApkSandboxManager.launch()) {
            Toast.makeText(context, "沙盒启动失败", Toast.LENGTH_LONG).show();
        }
    }

    private void uninstallApkGame() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        if (!ApkSandboxManager.isInstalled()) {
            Toast.makeText(context, ApkSandboxManager.GAME_NAME + " 尚未安装", Toast.LENGTH_SHORT).show();
            return;
        }
        ApkSandboxManager.uninstall();
        refreshApkGameButtons();
        Toast.makeText(context, "已从盒子沙盒卸载", Toast.LENGTH_SHORT).show();
    }

    private void refreshApkGameButtons() {
        Context context = getContext();
        if (context == null || _installButton == null) {
            return;
        }
        boolean installed = ApkSandboxManager.isInstalled();
        _installButton.setEnabled(!_installing && !installed);
        _installButton.setText(_installing ? "准备中…" : "安装");
        _runButton.setEnabled(installed);
        _uninstallButton.setEnabled(installed);
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
        refreshApkGameButtons();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (_activity != null && _fragment != null) {
            _activity.unRegisterMyTouchListener(_fragment.getTouchListenr());
        }
    }

    private static String safeMessage(Throwable error) {
        String message = error.getMessage();
        return message == null ? error.getClass().getSimpleName() : message;
    }

}
