package com.mgc.letobox.happy.circle.bean;

import com.mgc.letobox.happy.find.bean.BaseResultBean;

/**
 * Created by DELL on 2018/7/27.
 */

public class KOLGameListResponse extends BaseResultBean {

    /**
     * game_id : 362699
     * publicity : 搞笑脑洞来袭
     * star_cnt : 10
     * game_name : 史上最坑爹的游戏3-安卓版
     * game_icon : http://mgc-games.com:8711/upload/20180412/5acec8d6cc669.png
     */

    private int game_id;
    private String publicity;
    private float star_cnt;
    private String game_name;
    private String game_icon;

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getPublicity() {
        return publicity;
    }

    public void setPublicity(String publicity) {
        this.publicity = publicity;
    }

    public float getStar_cnt() {
        return star_cnt;
    }

    public void setStar_cnt(float star_cnt) {
        this.star_cnt = star_cnt;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGame_icon() {
        return game_icon;
    }

    public void setGame_icon(String game_icon) {
        this.game_icon = game_icon;
    }
}
