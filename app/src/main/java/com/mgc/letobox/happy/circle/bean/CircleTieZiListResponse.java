package com.mgc.letobox.happy.circle.bean;

import com.mgc.letobox.happy.find.bean.CommentBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 2018/7/19.
 */

public class CircleTieZiListResponse implements Serializable {

    /**
     * template_id : 3
     * post_id : 31
     * title : 哈哈哈哈
     * comment : 0
     * date : 07月20日 17:56
     * pics : [{"path":"http://mgc-games.com:8711/picture/2/83120/5b51b1be3b31a.png"}]
     * kol : {"id":"83120","nickname":"151210","cover_pic":"http://mgc-games.com:8711/upload/20180710/5b445a240223a.png","isfollow":0}
     */

    private int template_id;
    private int post_id;
    private String title;
    private String comment;
    private String date;
    private KolBean kol;
    private List<Pics> pics;
    private int is_edit;
    private int is_delete;
    private String content;

    public List<CommentBean> getContent_new() {
        return content_new;
    }

    public void setContent_new(List<CommentBean> content_new) {
        this.content_new = content_new;
    }

    private List<CommentBean> content_new;
    private String pic_ids;
    private boolean isEdtextAndDel; // true:编辑帖子刷新 false:删除帖子刷新

    public boolean isEdtextAndDel() {
        return isEdtextAndDel;
    }

    public void setEdtextAndDel(boolean edtextAndDel) {
        isEdtextAndDel = edtextAndDel;
    }

    public String getPic_ids() {
        return pic_ids;
    }

    public void setPic_ids(String pic_ids) {
        this.pic_ids = pic_ids;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(int is_edit) {
        this.is_edit = is_edit;
    }

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public KolBean getKol() {
        return kol;
    }

    public void setKol(KolBean kol) {
        this.kol = kol;
    }

    public List<Pics> getPics() {
        return pics;
    }

    public void setPics(List<Pics> pics) {
        this.pics = pics;
    }

    public class Pics implements Serializable {

        private String id;
        private String url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public class KolBean implements Serializable {
        /**
         * id : 83120
         * nickname : 151210
         * cover_pic : http://mgc-games.com:8711/upload/20180710/5b445a240223a.png
         * isfollow : 0
         */

        private int id;
        private String nickname;
        private String cover_pic;
        private int isfollow;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCover_pic() {
            return cover_pic;
        }

        public void setCover_pic(String cover_pic) {
            this.cover_pic = cover_pic;
        }

        public int getIsfollow() {
            return isfollow;
        }

        public void setIsfollow(int isfollow) {
            this.isfollow = isfollow;
        }

        public String getLevel_pic() {
            return level_pic;
        }

        public void setLevel_pic(String level_pic) {
            this.level_pic = level_pic;
        }

        public String level_pic;
    }
}
