package com.mgc.letobox.happy.find.bean;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class GameCommentRequestBean extends BaseRequestBean {

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int game_id;

    public String content;

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public int star;

    private String agent_id;
}
