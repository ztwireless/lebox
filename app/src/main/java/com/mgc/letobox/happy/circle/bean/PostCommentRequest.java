package com.mgc.letobox.happy.circle.bean;

import com.mgc.leto.game.base.bean.BaseRequestBean;

public class PostCommentRequest extends BaseRequestBean {
    private int post_id;
    private String content;

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
