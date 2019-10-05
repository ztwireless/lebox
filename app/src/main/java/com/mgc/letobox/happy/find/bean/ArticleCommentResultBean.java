package com.mgc.letobox.happy.find.bean;

import java.util.List;

public class ArticleCommentResultBean {


    /**
     * id : 20
     * uid : 1
     * app : News
     * mod : index
     * row_id : 1
     * parse : 0
     * content : 我很调皮
     * create_time : 刚刚
     * pid : 0
     * status : 1
     * ip : 0
     * area :
     * user : {"uid":"1","signature":"","title":"Lv3 转正","nickname":"admin","rank_link":[],"fans":"0","following":"0","score1":"133","score":"133","level":{"current":"Lv3 转正","before_level_need":100,"next":"Lv4 助理","upgrade_require":200,"left":67,"percent":"33.0"},"avatar128":"http://mob.com/Public/images/default_avatar_128_128.jpg","avatar256":"http://mob.com/Public/images/default_avatar_256_256.jpg","avatar64":"http://mob.com/Public/images/default_avatar_64_64.jpg","avatar32":"http://mob.com/Public/images/default_avatar_32_32.jpg","avatar512":"http://mob.com/Public/images/default_avatar_512_512.jpg","tags":[]}
     * is_landlord : 0
     */

    private String id;
    private String uid;
    private String app;
    private String mod;
    private String row_id;
    private String parse;
    private String content;
    private String create_time;
    private String pid;
    private String status;
    private String ip;
    private String area;
    private UserBean user;
    private String is_landlord;

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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getIs_landlord() {
        return is_landlord;
    }

    public void setIs_landlord(String is_landlord) {
        this.is_landlord = is_landlord;
    }

    public static class UserBean {
        /**
         * uid : 1
         * signature :
         * title : Lv3 转正
         * nickname : admin
         * rank_link : []
         * fans : 0
         * following : 0
         * score1 : 133
         * score : 133
         * level : {"current":"Lv3 转正","before_level_need":100,"next":"Lv4 助理","upgrade_require":200,"left":67,"percent":"33.0"}
         * avatar128 : http://mob.com/Public/images/default_avatar_128_128.jpg
         * avatar256 : http://mob.com/Public/images/default_avatar_256_256.jpg
         * avatar64 : http://mob.com/Public/images/default_avatar_64_64.jpg
         * avatar32 : http://mob.com/Public/images/default_avatar_32_32.jpg
         * avatar512 : http://mob.com/Public/images/default_avatar_512_512.jpg
         * tags : []
         */

        private String uid;
        private String signature;
        private String title;
        private String nickname;
        private String fans;
        private String following;
        private String score1;
        private String score;
        private LevelBean level;
        private String avatar128;
        private String avatar256;
        private String avatar64;
        private String avatar32;
        private String avatar512;
        private List<?> rank_link;
        private List<?> tags;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
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

        public String getFans() {
            return fans;
        }

        public void setFans(String fans) {
            this.fans = fans;
        }

        public String getFollowing() {
            return following;
        }

        public void setFollowing(String following) {
            this.following = following;
        }

        public String getScore1() {
            return score1;
        }

        public void setScore1(String score1) {
            this.score1 = score1;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public LevelBean getLevel() {
            return level;
        }

        public void setLevel(LevelBean level) {
            this.level = level;
        }

        public String getAvatar128() {
            return avatar128;
        }

        public void setAvatar128(String avatar128) {
            this.avatar128 = avatar128;
        }

        public String getAvatar256() {
            return avatar256;
        }

        public void setAvatar256(String avatar256) {
            this.avatar256 = avatar256;
        }

        public String getAvatar64() {
            return avatar64;
        }

        public void setAvatar64(String avatar64) {
            this.avatar64 = avatar64;
        }

        public String getAvatar32() {
            return avatar32;
        }

        public void setAvatar32(String avatar32) {
            this.avatar32 = avatar32;
        }

        public String getAvatar512() {
            return avatar512;
        }

        public void setAvatar512(String avatar512) {
            this.avatar512 = avatar512;
        }

        public List<?> getRank_link() {
            return rank_link;
        }

        public void setRank_link(List<?> rank_link) {
            this.rank_link = rank_link;
        }

        public List<?> getTags() {
            return tags;
        }

        public void setTags(List<?> tags) {
            this.tags = tags;
        }

        public static class LevelBean {
            /**
             * current : Lv3 转正
             * before_level_need : 100
             * next : Lv4 助理
             * upgrade_require : 200
             * left : 67
             * percent : 33.0
             */

            private String current;
            private int before_level_need;
            private String next;
            private int upgrade_require;
            private int left;
            private String percent;

            public String getCurrent() {
                return current;
            }

            public void setCurrent(String current) {
                this.current = current;
            }

            public int getBefore_level_need() {
                return before_level_need;
            }

            public void setBefore_level_need(int before_level_need) {
                this.before_level_need = before_level_need;
            }

            public String getNext() {
                return next;
            }

            public void setNext(String next) {
                this.next = next;
            }

            public int getUpgrade_require() {
                return upgrade_require;
            }

            public void setUpgrade_require(int upgrade_require) {
                this.upgrade_require = upgrade_require;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public String getPercent() {
                return percent;
            }

            public void setPercent(String percent) {
                this.percent = percent;
            }
        }
    }
}
