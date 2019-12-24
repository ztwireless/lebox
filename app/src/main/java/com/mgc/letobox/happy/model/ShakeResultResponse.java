package com.mgc.letobox.happy.model;

import android.support.annotation.Keep;

@Keep
public class ShakeResultResponse {
    public int code;
    public String msg;
    @Keep
    static class Data {
        int add_coins;
        int add_coins_type;
        int add_coins_multiple;
    }
}