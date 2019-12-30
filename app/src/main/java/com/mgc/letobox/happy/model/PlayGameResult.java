package com.mgc.letobox.happy.model;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class PlayGameResult implements Serializable {

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
    public static class Data implements Serializable{
        public String banners;
        public List<Game> games = new ArrayList<Game>();
        public int coins;
        public int coins_multiple;

    }
    @Keep
    public static class Game implements Serializable{
        public int game_id;
        public String game_name;
        public String game_icon;
        public String game_desc;
        public String packagename;
        public String download_url;
    }

}