package com.mgc.letobox.happy.find.model;

import com.mgc.letobox.happy.find.bean.CommentBean;

import java.util.List;

public class Comment {

    /**
     * id : 19
     * uid : 82833
     * app : Game
     * mod : index
     * row_id : 362736
     * parse : 0
     * content : 好玩
     * create_time : 30分钟前
     * pid : 0
     * status : 1
     * ip : 0
     * area :
     * is_support : 0
     * user : {"uid":"82833","username":"154432","nickname":"18519135802","avatar128":"http://mgc-games.com:8711/upload/20180619/5b28af0a20c13.png"}
     */

    private String id;
    private String uid;
    private String app;
    private String mod;
    private String row_id;
    private String parse;
    private String content;

    public List<CommentBean> getContent_new() {
        return content_new;
    }

    public void setContent_new(List<CommentBean> content_new) {
        this.content_new = content_new;
    }

    private List<CommentBean> content_new;
    private String create_time;
    private String pid;
    private String status;
    private String ip;
    private String area;
    private int is_support;
    private UserBean user;

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

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public String getRow_id() {
        return row_id;
    }

    public void setRow_id(String row_id) {
        this.row_id = row_id;
    }

    public String getParse() {
        return parse;
    }

    public void setParse(String parse) {
        this.parse = parse;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getIs_support() {
        return is_support;
    }

    public void setIs_support(int is_support) {
        this.is_support = is_support;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * uid : 82833
         * username : 154432
         * nickname : 18519135802
         * avatar128 : http://mgc-games.com:8711/upload/20180619/5b28af0a20c13.png
         */

        private String uid;
        private String username;
        private String nickname;
        private String avatar128;

        public int getGame_star() {
            return game_star;
        }

        public void setGame_star(int game_star) {
            this.game_star = game_star;
        }

        private int game_star;

        public String getLevel_pic() {
            return level_pic;
        }

        public void setLevel_pic(String level_pic) {
            this.level_pic = level_pic;
        }

        private String level_pic;


        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar128() {
            return avatar128;
        }

        public void setAvatar128(String avatar128) {
            this.avatar128 = avatar128;
        }
    }

    public int getIssue_id() {
        return issue_id;
    }

    public void setIssue_id(int issue_id) {
        this.issue_id = issue_id;
    }

    private int issue_id;
}
