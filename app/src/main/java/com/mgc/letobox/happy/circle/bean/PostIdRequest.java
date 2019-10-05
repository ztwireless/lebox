package com.mgc.letobox.happy.circle.bean;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Created by DELL on 2018/7/24.
 */

public class PostIdRequest extends BaseRequestBean {

    private int post_id;
    private int page;

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
