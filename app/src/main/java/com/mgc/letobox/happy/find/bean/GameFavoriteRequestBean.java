package com.mgc.letobox.happy.find.bean;

import com.mgc.leto.game.base.bean.BaseRequestBean;

public class GameFavoriteRequestBean extends BaseRequestBean {
    private int type;
    private String game_id;

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
