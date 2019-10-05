package com.mgc.letobox.happy.find.bean;

import com.leto.game.base.bean.BaseRequestBean;

public class FollowRequestBean extends BaseRequestBean {

    public int getFollow_who() {
        return follow_who;
    }

    public void setFollow_who(int follow_who) {
        this.follow_who = follow_who;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int follow_who;

    private int type;



}
