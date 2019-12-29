package com.mgc.letobox.happy.bean;

import android.support.annotation.Keep;

/**
 * Create by zhaozhihui on 2019-12-25
 **/
@Keep
public class ShakeResultBean {
    public int add_coins;
    public int add_coins_type; // 1 提现金币, 2. 游戏内物品
    public int add_coins_multiple;
    public String propid; // 当add_coins_type为2时表示游戏内物品id
}
