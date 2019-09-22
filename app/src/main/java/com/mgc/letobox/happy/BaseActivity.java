package com.mgc.letobox.happy;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by liu hong liang on 2017/4/27.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();



    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }
}
