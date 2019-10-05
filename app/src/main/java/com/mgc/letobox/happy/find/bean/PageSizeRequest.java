package com.mgc.letobox.happy.find.bean;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Created by DELL on 2018/7/19.
 */

public class PageSizeRequest extends BaseRequestBean {

    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
