package com.mgc.letobox.happy.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Keep
public class PlayGameResult {

    public float code;
    public String msg;
    @SerializedName("data")
    Data DataObject;
//    @SerializedName("games")
//    Games GamesObject;

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
        public String banners;
        public ArrayList<Game> games = new ArrayList<Game>();

    }
    @Keep
    public static class Game{
        public int game_id;
        public String game_name;
        public String game_icon;
        public String game_desc;
        public String packagename;
        public String download_url;
    }

}