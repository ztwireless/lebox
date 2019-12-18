package com.mgc.letobox.happy.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Create by zhaozhihui on 2019-11-30
 **/
public class LeBoxSpUtil {

    public static final String PRFS_FIRST_LAUNCH = "prfs_first_launch";
    public static final String PRFS_SHAKE_TIMES = "prfs_shake_times";
    public static final String PRFS_BUBBLE_TIMES = "prfs_bubble_times";

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

    public static boolean isFirstLaunch() {
        if (_SP != null) {
            return _SP.getBoolean(PRFS_FIRST_LAUNCH, true);
        }
        return true;
    }

    public static int todayShakeTimes(String gameId) {
        String key = PRFS_SHAKE_TIMES + "_" + gameId + "_" + getDay();
        return _SP.getInt(key, 0);
    }

    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
    private static String getDay() {
        return dayFormat.format(new Date());
    }

    public static void shakeOnce(String gameId) {
        String key = PRFS_SHAKE_TIMES + "_" + gameId + "_" + getDay();
        _SP.edit().putInt(key, todayShakeTimes(gameId) + 1).apply();
    }

    public static int todayBubbleTimes(String gameId) {
        String key = PRFS_BUBBLE_TIMES + "_" + gameId + "_"  + getDay();
        return _SP.getInt(key, 0);
    }

    public static void bubbleOnce(String gameId) {
        String key = PRFS_BUBBLE_TIMES + "_" + gameId + "_"  + getDay();
        _SP.edit().putInt(key, todayBubbleTimes(gameId) + 1).apply();
    }
}
