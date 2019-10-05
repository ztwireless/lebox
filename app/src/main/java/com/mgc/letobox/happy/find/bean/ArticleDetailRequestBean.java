package com.mgc.letobox.happy.find.bean;


import com.leto.game.base.bean.BaseRequestBean;

public class ArticleDetailRequestBean extends BaseRequestBean {

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    int  news_id;
}
