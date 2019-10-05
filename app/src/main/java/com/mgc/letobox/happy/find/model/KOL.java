package com.mgc.letobox.happy.find.model;

import java.io.Serializable;

public class KOL implements Serializable{
    public int id;
    public String nickname;
    public String cover_pic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCover_pic() {
        return cover_pic;
    }

    public void setCover_pic(String cover_pic) {
        this.cover_pic = cover_pic;
    }

    public int getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(int isfollow) {
        this.isfollow = isfollow;
    }

    public int isfollow;

    public String getLevel_pic() {
        return level_pic;
    }

    public void setLevel_pic(String level_pic) {
        this.level_pic = level_pic;
    }

    public String level_pic;
}
