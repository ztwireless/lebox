package com.mgc.letobox.happy.find.bean;

import com.leto.game.base.bean.BaseRequestBean;

public class GameFavoriteRequestBean extends BaseRequestBean {

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    private String game_id;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;



}
