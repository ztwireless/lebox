package com.mgc.letobox.happy.find.bean;

import com.mgc.leto.game.base.bean.BaseRequestBean;

public class ListBaseRequestBean extends BaseRequestBean {
    public ListBaseRequestBean() {
        page = 0;
        offset = 10;
    }

    public int page;  // 页码默认为0

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int offset;  // 每页调取条数，默认为10
}
