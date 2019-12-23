package com.mgc.letobox.happy.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class ShakeResult {

    public float code;
    public String msg;
    @SerializedName("data")
    Data DataObject;

    // Getter Methods

    public float getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return DataObject;
    }

    @Keep
    public static class Data {
        public int add_coins;
        public int add_coins_type;
        public int add_coins_multiple;
    }
}