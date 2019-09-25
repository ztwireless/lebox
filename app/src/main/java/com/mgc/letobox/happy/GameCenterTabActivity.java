package com.mgc.letobox.happy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.config.AppConfig;
import com.ledong.lib.leto.listener.ILetoPlayedDurationListener;
import com.ledong.lib.minigame.bean.TabBean;
import com.leto.game.base.ad.AdManager;
import com.leto.game.base.event.DataRefreshEvent;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.ColorUtil;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.IntentConstant;
import com.leto.game.base.util.MResource;
import com.leto.game.base.util.StatusBarUtil;
import com.mgc.letobox.happy.event.TabSwitchEvent;
import com.mgc.letobox.happy.me.bean.TaskResultBean;
import com.mgc.letobox.happy.me.view.TaskCoinDialog;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/8/4.
 */

public class GameCenterTabActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private final String TAG = "GameCenterActivity";


    RadioButton tabBtn1;
    RadioButton tabBtn2;
    RadioButton tabBtn3;
    RadioButton tabBtn4;
    RadioButton tabBtn5;
    RadioGroup tabGroup;

    Fragment curFragment;

    TabMiniGameFragment fragment1;
    TabGameRankFragment fragment2;
    TabChallengeFragment fragment3;
    TabCategoryFragment fragment4;
    TabMeFragment fragment5;


    String orientation = "portrait";
    String srcAppId;
    String srcAppPath;

    AlertDialog alertDialog;

    int mTabIndex;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init leto
        Leto.init(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#ffffff"));
        }
        // set content view
        setContentView(MResource.getIdByName(this, "R.layout.activity_tab_gamecenter"));

        orientation = getIntent().getStringExtra(IntentConstant.ACTION_APP_ORIENTATION);
        srcAppId = getIntent().getStringExtra(IntentConstant.SRC_APP_ID);
        srcAppPath = getIntent().getStringExtra(IntentConstant.SRC_APP_PATH);

        tabBtn1 = findViewById(MResource.getIdByName(this, "R.id.tab_btn1"));
        tabBtn2 = findViewById(MResource.getIdByName(this, "R.id.tab_btn2"));
        tabBtn3 = findViewById(MResource.getIdByName(this, "R.id.tab_btn3"));
        tabBtn4 = findViewById(MResource.getIdByName(this, "R.id.tab_btn4"));
        tabBtn5 = findViewById(MResource.getIdByName(this, "R.id.tab_btn5"));
        tabGroup = findViewById(MResource.getIdByName(this, "R.id.tab_group"));

        // detect system version, if too low, give a hint
        checkSystemVersion();

        try {
            //init rxVolley
            RxVolley.setRequestQueue(RequestQueue.newRequestQueue(BaseAppUtil.getDefaultSaveRootPath(this, "RxVolley")));
        } catch (Exception e) {

        }


        tabGroup.setOnCheckedChangeListener(this);
        tabGroup.check(R.id.tab_btn1);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        Leto.getInstance().setLetoPlayedDurationListener(new ILetoPlayedDurationListener() {
            @Override
            public void getPlayedDurations(String gameId, long duration) {

                Log.i(TAG, "gameId: " + gameId + "-------------duration: " + duration);

                reportTaskProgress(duration);
            }
        });

        NewerTaskManager.getTaskList(this, null);


        AdManager.getInstance().getTmTaskList(this);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void checkSystemVersion() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            //创建AlertDialog的构造器的对象
            AlertDialog.Builder builder = new AlertDialog.Builder(GameCenterTabActivity.this);
            //设置构造器标题
//            builder.setTitle("提示");
            //构造器对应的图标
//            builder.setIcon(R.mipmap.ic_launcher);
            //构造器内容,为对话框设置文本项(之后还有列表项的例子)
            builder.setCancelable(false);
            builder.setMessage(getString(MResource.getIdByName(GameCenterTabActivity.this, "R.string.leto_toast_the_system_version_low")));
            //为构造器设置确定按钮,第一个参数为按钮显示的文本信息，第二个参数为点击后的监听事件，用匿名内部类实现
            builder.setPositiveButton(getString(MResource.getIdByName(GameCenterTabActivity.this, "R.string.leto_know_it")), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    finish();
                }
            });
            //为构造器设置取消按钮,若点击按钮后不需要做任何操作则直接为第二个参数赋值null
//            builder.setNegativeButton("不呀",null);
            //为构造器设置一个比较中性的按钮，比如忽略、稍后提醒等
//            builder.setNeutralButton("稍后提醒",null);
            //利用构造器创建AlertDialog的对象,实现实例化
            alertDialog = builder.create();
            alertDialog.show();
        }

    }

    @Keep
    public static void start(Context context, String orientation, String appId, String appPath) {
        if (null != context) {
            Intent intent = new Intent(context, GameCenterTabActivity.class);
            intent.putExtra(IntentConstant.ACTION_APP_ORIENTATION, orientation);
            intent.putExtra(IntentConstant.SRC_APP_ID, appId);
            intent.putExtra(IntentConstant.SRC_APP_PATH, appPath);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        GlideUtil.clearMemory(this);

        RxVolley.getRequestQueue().cancelAll(GameCenterTabActivity.this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchTab(TabSwitchEvent event) {

        if (event != null) {
            int tabIndex = event.tabindex;
            switch (tabIndex) {
                case 0:
                    tabGroup.check(R.id.tab_btn1);
                    break;
                case 2:
                    tabGroup.check(R.id.tab_btn3);
                    break;
                default:

                    break;
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int i) {
        Fragment fragment = null;
        switch (i) {
            case R.id.tab_btn1:

                if (null == fragment1) {
                    fragment1 = (TabMiniGameFragment) TabMiniGameFragment.newInstance(17);
                }
                fragment = fragment1;
                mTabIndex = 0;
                break;
            case R.id.tab_btn2:
                if (null == fragment2) {
                    fragment2 = (TabGameRankFragment) TabGameRankFragment.newInstance(18, "榜单");
                }
                fragment = fragment2;
                mTabIndex = 1;
                break;
            case R.id.tab_btn3:
                if (null == fragment3) {
                    fragment3 = (TabChallengeFragment) TabChallengeFragment.newInstance(19,"挑战");
                }
                fragment = fragment3;
                mTabIndex = 2;
                break;
            case R.id.tab_btn4:
                if (null == fragment4) {
                    fragment4 = TabCategoryFragment.newInstance();
                }
                fragment = fragment4;
                mTabIndex = 3;
                break;
            case R.id.tab_btn5:
                if (null == fragment5) {
                    AppConfig appConfig = new AppConfig(BaseAppUtil.getChannelID(this), LoginManager.getUserId(this));
                    fragment5 = TabMeFragment.newInstance();
                }
                fragment = fragment5;
                mTabIndex = 4;
                break;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.container, fragment);
        }
        if (curFragment == fragment) {
            return;
        }

        if (null != curFragment) {
            //fragmentTransaction.hide(curFragment).show(fragment).replace(R.id.container, fragment).commit();
            fragmentTransaction.hide(curFragment).show(fragment).commit();
        } else {
            fragmentTransaction.commit();
        }

        curFragment = fragment;
    }


    private class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabBeans.get(position).getName();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
    }

    List<Fragment> mFragmentList = new ArrayList<>();

    List<TabBean> mTabBeans = new ArrayList<>();


    public void reportTaskProgress(long progress) {

        List<TaskResultBean> taskResultBeans = NewerTaskManager.getCompleteNewerTaskId(this, "", progress);
        if (taskResultBeans != null && taskResultBeans.size() > 0) {
            showTaskDialog(taskResultBeans, 0, 0);
        }

        EventBus.getDefault().post(new DataRefreshEvent());

    }

    Handler mTaskHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            List<TaskResultBean> taskBeans = (List<TaskResultBean>) msg.obj;
            int pos = msg.arg1;
            int action = msg.arg2;

            showTaskDialog(taskBeans, pos, action);

        }

    };


    private void showTaskDialog(final List<TaskResultBean> taskBeans, final int pos, final int action) {


        TaskCoinDialog d = new TaskCoinDialog(this,
                this.getString(MResource.getIdByName(this, "R.string.leto_mgc_dialog_newer_task_title")), taskBeans.get(pos), new TaskCoinDialog.GameEndCoinDialogListener() {
            @Override
            public void onExit(boolean video, int coinGot) {

                if (taskBeans.size() > pos + 1) {

                    Message msg = new Message();
                    msg.obj = taskBeans.get(pos + 1);
                    msg.arg1 = pos + 1;
                    msg.arg2 = action;
                    mTaskHandler.sendMessage(msg);
                }
            }
        });
        d.show();
    }


}