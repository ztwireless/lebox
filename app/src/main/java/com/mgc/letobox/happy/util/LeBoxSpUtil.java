package com.mgc.letobox.happy.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.leto.game.base.db.LoginControl;

/**
 * Create by zhaozhihui on 2019-11-30
 **/
public class LeBoxSpUtil {

    public static final String PRFS_FIRST_LAUNCH = "prfs_first_launch";

    protected static SharedPreferences _SP = null;

    public static synchronized void init(Context context) {
        if (_SP == null) {
            _SP = context.getSharedPreferences(LeBoxSpUtil.class.getName(), Context.MODE_PRIVATE);
        }
    }

    public static void setFirstLaunch(boolean isFirst) {

        if (_SP != null) {
            _SP.edit().putBoolean(PRFS_FIRST_LAUNCH, isFirst).apply();
        }
    }

    public static boolean isFirstLaunch(){
        if (_SP != null) {
           return _SP.getBoolean(PRFS_FIRST_LAUNCH, true);
        }
        return true;
    }
}
