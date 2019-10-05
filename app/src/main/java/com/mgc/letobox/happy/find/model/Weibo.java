package com.mgc.letobox.happy.find.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class Weibo implements MultiItemEntity {

    /**
     * id : 6
     * uid : 1
     * content : 哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇
     * create_time : 1439781433
     * comment_count : 0
     * status : 1
     * is_top : 0
     * type : feed
     * data : {"source":null,"sourceId":"2"}
     * repost_count : 0
     * from : 手机安卓版
     * support_count : 0
     * user : {"uid":"1","title":"Lv1 实习","nickname":"admin","score1":"42"}
     */

    private String id;
    private String uid;
    private String content;
    private String create_time;
    private String comment_count;
    private String status;
    private String is_top;
    private String type;
    private DataBean data;
    private String repost_count;
    private String from;
    private String support_count;
    private UserBean user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public boolean has_image;

    public boolean isHas_image() {
        return has_image;
    }

    public void setHas_image(boolean has_image) {
        this.has_image = has_image;
    }

    public boolean isHas_video() {
        return has_video;
    }

    public void setHas_video(boolean has_video) {
        this.has_video = has_video;
    }

    public boolean has_video;

    public List<String> getMedia() {
        return media;
    }

    public void setMedia(List<String> media) {
        this.media = media;
    }

    private List<String> media;

    public int getContent_type() {
        return content_type;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }

    private int content_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getRepost_count() {
        return repost_count;
    }

    public void setRepost_count(String repost_count) {
        this.repost_count = repost_count;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSupport_count() {
        return support_count;
    }

    public void setSupport_count(String support_count) {
        this.support_count = support_count;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public int getItemType() {
        return content_type;
    }

    public static class DataBean {
        /**
         * source : null
         * sourceId : 2
         */

        private Object source;
        private String sourceId;

        public Object getSource() {
            return source;
        }

        public void setSource(Object source) {
            this.source = source;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }
    }

    public static class UserBean {
        /**
         * uid : 1
         * title : Lv1 实习
         * nickname : admin
         * score1 : 42
         */

        private String uid;
        private String title;
        private String nickname;
        private String score1;

        public boolean isFollow() {
            return isFollow;
        }

        public void setFollow(boolean follow) {
            isFollow = follow;
        }

        private boolean isFollow;

        public String getAvater() {
            return avater;
        }

        public void setAvater(String avater) {
            this.avater = avater;
        }

        private String avater;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getScore1() {
            return score1;
        }

        public void setScore1(String score1) {
            this.score1 = score1;
        }
    }
}
