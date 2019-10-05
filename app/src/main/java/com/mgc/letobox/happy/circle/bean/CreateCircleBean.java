package com.mgc.letobox.happy.circle.bean;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Created by DELL on 2018/7/18.
 */

public class CreateCircleBean extends BaseRequestBean {

    private String title;
    private String detail;
    private String logo;
    private String background;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
