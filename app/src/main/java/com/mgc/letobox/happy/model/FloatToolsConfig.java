package com.mgc.letobox.happy.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FloatToolsConfig {
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

    public static class Data {
        @SerializedName("bubble")
        Bubble BubbleObject;
        @SerializedName("shake")
        Shake ShakeObject;


        // Getter Methods

        public Bubble getBubble() {
            return BubbleObject;
        }

        public Shake getShake() {
            return ShakeObject;
        }

        public static class Shake {
            public int is_open;
            public int max_times;
            public int default_x;
            public float default_y;
            public ArrayList<Integer> game_ids = new ArrayList<Integer>();
        }

        public static class Bubble {
            public int is_open;
            public int min_coins;
            public int max_coins;
            public int create_interval;
            public int create_max_times;
            public int coins_multiple;
            public float left_upper;
            public float right_upper;
            public float left_lower;
            public float right_lower;
            public ArrayList<Integer> game_ids = new ArrayList<Integer>();
        }
    }
}