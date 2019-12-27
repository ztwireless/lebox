package com.mgc.letobox.happy.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Keep
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

    @Keep
    public static class Data {
        @SerializedName("bubble")
        Bubble BubbleObject;
        @SerializedName("shake")
        Shake ShakeObject;
        @SerializedName("levelhb")
        Upgrade UpgradeObject;
        @SerializedName("hbrain")
        Hbrain HbrainObject;
        @SerializedName("playgametask")
        Playgametask playgametask;


        // Getter Methods

        public Bubble getBubble() {
            return BubbleObject;
        }

        public Shake getShake() {
            return ShakeObject;
        }

        public Hbrain getHbrain() {
            return HbrainObject;
        }

        public Playgametask getPlaygametask() {
            return playgametask;
        }

        public void setPlaygameTask(Playgametask playgameTask){
            this.playgametask = playgameTask;
        }

        @Keep
        public static class Shake {
            public int is_open;
            public int max_times;
            public int default_x;
            public float default_y;
            public ArrayList<Integer> game_ids = new ArrayList<Integer>();
        }

        @Keep
        public static class Bubble {
            public int is_open;
            public int min_coins;
            public int max_coins;
            public int create_interval;
            public int create_max_times;
            public int coins_multiple;
            public int screen_max_times;
            public float left_upper;
            public float right_upper;
            public float left_lower;
            public float right_lower;
            public ArrayList<Integer> game_ids = new ArrayList<Integer>();
        }


        public Upgrade getUpgrade() {
            return UpgradeObject;
        }

        @Keep
        public static class Upgrade {
            public int is_open;
            public int default_x;
            public float default_y;
            public int coins_multiple;
            public ArrayList<Integer> game_ids = new ArrayList<Integer>();
        }

        @Keep
        public static class Hbrain {
            public int is_open;
            public int default_x;
            public float default_y;
            public int min_coins;
            public int max_coins;
            public int coins_multiple;
            public int cooling_time;
            public int create_max_times;
            public ArrayList<Integer> game_ids = new ArrayList<Integer>();
        }
        @Keep
        public static class Playgametask {
            public int is_open;
            public int default_x;
            public float default_y;
            public ArrayList<Integer> game_ids = new ArrayList<Integer>();
        }
    }
}