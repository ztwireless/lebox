package com.mgc.letobox.happy.find.bean;


import com.leto.game.base.bean.BaseRequestBean;

public class ArticleCommentRequestBean extends BaseRequestBean {
    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public int news_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String content;
}
