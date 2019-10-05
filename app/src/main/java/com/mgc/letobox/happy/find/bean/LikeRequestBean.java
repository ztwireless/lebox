package com.mgc.letobox.happy.find.bean;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Created by liu hong liang on 2016/11/12.
 */

public class LikeRequestBean extends BaseRequestBean {
    private String appname; //应用名：圈子帖子Group 资讯News
    private String table;   //表名：圈子帖子group_post 资讯news_detail

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }

    private int row; //应用id ，eg：帖子id 资讯id
    private String jump;  //跳转链接 ，圈子帖子group/index/detail 资讯 无

}
