package com.mgc.letobox.happy.circle.bean;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Created by DELL on 2018/7/24.
 */

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
