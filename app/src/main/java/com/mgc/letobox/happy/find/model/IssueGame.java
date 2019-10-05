package com.mgc.letobox.happy.find.model;

import com.mgc.letobox.happy.find.bean.GameBean;

public class IssueGame extends GameBean {
    private String name;
    private String agurl;
    private String classify;
    private String gametype;

    public String getPublicity() {
        return publicity;
    }

    public void setPublicity(String publicity) {
        this.publicity = publicity;
    }

    private String publicity;  //宣传语

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgurl() {
        return agurl;
    }

    public void setAgurl(String agurl) {
        this.agurl = agurl;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getGametype() {
        return gametype;
    }

    public void setGametype(String gametype) {
        this.gametype = gametype;
    }
}
