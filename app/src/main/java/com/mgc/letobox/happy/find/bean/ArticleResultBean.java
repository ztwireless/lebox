package com.mgc.letobox.happy.find.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mgc.letobox.happy.find.model.KOL;

import java.io.Serializable;
import java.util.List;


public class ArticleResultBean implements MultiItemEntity, Serializable {
    private String title;//	STRING	登出公告

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }

    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private int template_id;
    private int news_id;
    private String description;
    private String content;
    private int comment;
    private String date;
    private List<String > pics;

    @Override
    public int getItemType() {
        return template_id;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public KOL getKol() {
        return kol;
    }

    public void setKol(KOL kol) {
        this.kol = kol;
    }

    private KOL kol;

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    private String releaseTime;

    public String getFmpic() {
        return fmpic;
    }

    public void setFmpic(String fmpic) {
        this.fmpic = fmpic;
    }

    private String fmpic;
}
