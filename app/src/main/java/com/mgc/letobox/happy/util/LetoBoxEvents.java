package com.mgc.letobox.happy.util;

import com.mgc.letobox.happy.listener.IRewardedVideoListener;

/**
 * Create by zhaozhihui on 2020/12/21
 **/
public class LetoBoxEvents {

    public static IRewardedVideoListener getRewardedVideoListener() {
        return _rewardedVideoListener;
    }

    public static void setRewardedVideoListener(IRewardedVideoListener IRewardedVideoListener) {
        LetoBoxEvents._rewardedVideoListener = IRewardedVideoListener;
    }

    public static IRewardedVideoListener _rewardedVideoListener;
}
