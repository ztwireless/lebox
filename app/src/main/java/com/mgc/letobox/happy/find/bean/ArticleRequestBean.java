package com.mgc.letobox.happy.find.bean;


public class ArticleRequestBean extends ListBaseRequestBean {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int type;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    private int category;
}
