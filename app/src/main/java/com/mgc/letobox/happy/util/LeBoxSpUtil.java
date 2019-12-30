package com.mgc.letobox.happy.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mgc.letobox.happy.model.FcmConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hugo.weaving.DebugLog;

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
    public static final String PRFS_TRY_PLAY_TOTAL_TIME = "prfs_try_play_total_time";
    public static final String PRFS_TODAY_PLAY_TOTAL_TIME = "prfs_today_play_total_time";
    public static final String PRFS_IDCARD = "prfs_idcard";
    public static final String PRFS_FCM_CONFIG = "prfs_fcm_config";

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
        String key = PRFS_BUBBLE_TIMES + "_" + gameId + "_" + getDay();
        return _SP.getInt(key, 0);
    }

    public static void bubbleOnce(String gameId) {
        String key = PRFS_BUBBLE_TIMES + "_" + gameId + "_" + getDay();
        _SP.edit().putInt(key, todayBubbleTimes(gameId) + 1).apply();
    }

    public static int todayHbrainTimes(String gameId) {
        String key = PRFS_HBRAIN_TIMES + "_" + gameId + "_" + getDay();
        return _SP.getInt(key, 0);
    }

    public static void hbrainOnce(String gameId) {
        String key = PRFS_HBRAIN_TIMES + "_" + gameId + "_" + getDay();
        _SP.edit().putInt(key, todayHbrainTimes(gameId) + 1).apply();

        String lastTimeKey = PRFS_HBRAIN_LAST_TIME + "_" + gameId + "_" + getDay();
        _SP.edit().putLong(lastTimeKey, System.currentTimeMillis()).apply();
    }

    public static long hbrainLastTime(String gameId) {
        String key = PRFS_HBRAIN_LAST_TIME + "_" + gameId + "_" + getDay();
        return _SP.getLong(key, 0);
    }

    @DebugLog
    public static long tryPlayTime() {
        return _SP.getLong(PRFS_TRY_PLAY_TOTAL_TIME, 0);
    }

    public static void tryPlayFor(long time) {
        long totalTime = tryPlayTime();
        _SP.edit().putLong(PRFS_TRY_PLAY_TOTAL_TIME, totalTime + time).apply();
    }

    @DebugLog
    public static long todayPlayTime() {
        String key = PRFS_TODAY_PLAY_TOTAL_TIME + "_" + getDay();
        return _SP.getLong(key, 0);
    }

    public static void todayPlayFor(long time) {
        String key = PRFS_TODAY_PLAY_TOTAL_TIME + "_" + getDay();
        long totalTime = todayPlayTime();
        _SP.edit().putLong(key, totalTime + time).apply();
    }

    public static void saveIdCard(String mobile, String idCard) {
        String key = PRFS_IDCARD + "_" + mobile;
        _SP.edit().putString(key, idCard).apply();
    }

    public static String getIdCard(String mobile) {
        String key = PRFS_IDCARD + "_" + mobile;
        return _SP.getString(key, "");
    }

    public static void saveFcmConfig(FcmConfig config) {
        if (config == null) return;
        String jsonText = new Gson().toJson(config);
        saveString(PRFS_FCM_CONFIG, jsonText);
    }

    public static FcmConfig getFcmConfig() {
        String jsonText = getString(PRFS_FCM_CONFIG);
        try {
            return new Gson().fromJson(jsonText, FcmConfig.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static void saveString(String key, String value) {
        _SP.edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return _SP.getString(key, "");
    }

    public static void saveInt(String key, int value) {
        _SP.edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return _SP.getInt(key, 0);
    }
}
