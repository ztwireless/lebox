package com.mgc.letobox.happy.circle.bean;

import com.leto.game.base.bean.BaseRequestBean;

/**
 * Created by DELL on 2018/7/19.
 */

public class CircleCreateTieZiRequest extends BaseRequestBean {

    private int id;
    private int group_id;
    private String title;
    private String content;
    private String attach_id;
    private String pic_ids;

    public String getPic_ids() {
        return pic_ids;
    }

    public void setPic_ids(String pic_ids) {
        this.pic_ids = pic_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttach_id() {
        return attach_id;
    }

    public void setAttach_id(String attach_id) {
        this.attach_id = attach_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
