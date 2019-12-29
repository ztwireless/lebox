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
    public static final String PRFS_LAST_SHAKE_TIME = "prfs_last_shake_time";
    public static final String PRFS_BUBBLE_TIMES = "prfs_bubble_times";
    public static final String PRFS_HBRAIN_TIMES = "prfs_hbrain_times";
    public static final String PRFS_HBRAIN_LAST_TIME = "prfs_hbrain_last_time";

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

    public static long lastShakeTime(String gameId) {
        String key = PRFS_LAST_SHAKE_TIME + "_" + gameId + "_" + getDay();
        return _SP.getLong(key, 0);
    }

    private static SimpleDateFormat dayFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault());
    private static String getDay() {
        return dayFormat.format(new Date());
    }

    public static void shakeOnce(String gameId) {
        String key = PRFS_SHAKE_TIMES + "_" + gameId + "_" + getDay();
        _SP.edit().putInt(key, todayShakeTimes(gameId) + 1).apply();

        String lastShakeTime = PRFS_LAST_SHAKE_TIME + "_" + gameId + "_" + getDay();
        _SP.edit().putLong(lastShakeTime, System.currentTimeMillis()).apply();
    }

    public static int todayBubbleTimes(String gameId) {
        String key = PRFS_BUBBLE_TIMES + "_" + gameId + "_"  + getDay();
        return _SP.getInt(key, 0);
    }

    public static void bubbleOnce(String gameId) {
        String key = PRFS_BUBBLE_TIMES + "_" + gameId + "_"  + getDay();
        _SP.edit().putInt(key, todayBubbleTimes(gameId) + 1).apply();
    }

    public static int todayHbrainTimes(String gameId) {
        String key = PRFS_HBRAIN_TIMES + "_" + gameId + "_"  + getDay();
        return _SP.getInt(key, 0);
    }

    public static void hbrainOnce(String gameId) {
        String key = PRFS_HBRAIN_TIMES + "_" + gameId + "_"  + getDay();
        _SP.edit().putInt(key, todayHbrainTimes(gameId) + 1).apply();

        String lastTimeKey = PRFS_HBRAIN_LAST_TIME + "_" + gameId + "_"  + getDay();
        _SP.edit().putLong(lastTimeKey, System.currentTimeMillis()).apply();
    }

    public static long hbrainLastTime(String gameId) {
        String key = PRFS_HBRAIN_LAST_TIME + "_" + gameId + "_"  + getDay();
        return _SP.getLong(key, 0);
    }

    public static void saveString(String key,String value){
        _SP.edit().putString(key,value).apply();
    }
    public static String getString(String key){
        return _SP.getString(key,"");
    }
    public static void saveInt(String key,int value){
        _SP.edit().putInt(key,value).apply();
    }
    public static int getInt(String key){
        return _SP.getInt(key,0);
    }

}
