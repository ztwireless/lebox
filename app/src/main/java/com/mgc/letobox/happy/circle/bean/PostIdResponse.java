package com.mgc.letobox.happy.circle.bean;

import com.google.gson.annotations.SerializedName;
import com.leto.game.base.bean.BaseRequestBean;
import com.mgc.letobox.happy.find.bean.CommentBean;
import com.mgc.letobox.happy.find.bean.ShareBean;

import java.util.List;

/**
 * Created by DELL on 2018/7/24.
 */

// TODO 这个基类怎么是BaseRequestBean?
public class PostIdResponse extends BaseRequestBean {

    /**
     * id : 69
     * uid : 83120
     * group_id : 55
     * title : 我的帖子。
     * parse : 0
     * content : <!DOCTYPE html><html><head><meta charset="UTF-8"><title></title>
     <style>
     html,body{
     width: 100%;
     height: 100%;

     }
     img{
     width: 100%;
     }
     </style>

     </head><body>发表帖子详情内容。。。。。。<p><img src='http://download.mgc-games.com/picture/2/83120/5b596a1c50bff.jpg'/></p><p><img src='http://download.mgc-games.com/picture/2/83120/5b596a1d2f8bc.jpg'/></p><p><img src='http://download.mgc-games.com/picture/2/83120/5b596a1da59a7.jpg'/></p></body></html>
     * create_time : 07月26日 14:28
     * update_time : 07月26日 14:28
     * status : 1
     * last_reply_time : 今天11:39
     * view_count : 100
     * reply_count : 2
     * is_top : 0
     * cate_id : 0
     * summary : 发表帖子详情内容。。。。。。
     * cover : http://download.mgc-games.com/picture/2/83120/5b596a1c50bff.jpg
     * covers : [{"id":"582","url":"http://download.mgc-games.com/picture/2/83120/5b596a1c50bff.jpg"},{"id":"583","url":"http://download.mgc-games.com/picture/2/83120/5b596a1d2f8bc.jpg"},{"id":"584","url":"http://download.mgc-games.com/picture/2/83120/5b596a1da59a7.jpg"}]
     * cover_ids : 582,583,584
     * share_url : http://sns.mgc-games.com/index.php?s=/group/index/detail/id/69.html
     * group_title : 我的圈子
     * group_uid : 83118
     * user : {"uid":"83120","title":"Lv1 实习","nickname":"151210","rank_link":[],"score1":"0","sex":"0","birthday":"0000-00-00","qq":"","login":"37","reg_ip":"0","reg_time":"1529896691","last_login_ip":"2088632816","last_login_time":"1532775432","status":"2","last_login_role":"1","show_role":"1","signature":"","pos_province":"0","pos_city":"0","pos_district":"0","pos_community":"0","score2":"0","score3":"0","score4":"0","con_check":"0","total_check":"0","fans":"6","session_id":"","alive_line":"0","id":"83120","username":"151210","password":"tcMQrH","pay_pwd":"tcMQrH","email":"","mobile":"18701134428","from":"6","imei":"865795030434152","agentgame":"","app_id":"362679","agent_id":"0","update_time":"1529924740","truename":"熊忠宇","idcard":"360103199105084114","bindmobile":"18701134428","bindemail":"","portrait":"http://mgc-games.com:8711/upload/20180710/5b445a240223a.png","regist_ip":"115.171.244.140","parent_mem_id":"0","p_mem_id":"0","wh_user_id":"0","wh_passwd":"12345678","public_key":"MGC7NEZ7BsuRsbyjFqfWEqercxwS8ijpz5KqdDhWdffVPZiPAtgL3","affiliation":"1","real_nickname":"151210","avatar128":"http://sns.mgc-games.com/Uploads/AvatarPublic/images/default_avatar_128_128.jpg","avatar512":"http://sns.mgc-games.com/Uploads/AvatarPublic/images/default_avatar_512_512.jpg","is_follow":0}
     * supportCount : 0
     * BookmarkCount : 0
     * is_join : 1
     * is_collect : 0
     * is_support : 0
     */

    private String id;
    private String uid;
    private String group_id;
    private String title;
    private String parse;
    private String content;
    private String create_time;
    private String update_time;
    private String status;
    private String last_reply_time;
    private String view_count;
    private int reply_count;
    private String is_top;
    private String cate_id;
    private String summary;
    private String cover;
    private String cover_ids;
    private String share_url;
    private String group_title;
    private String group_uid;
    private UserBean user;
    private String supportCount;
    private String BookmarkCount;
    private int is_join; // 0:未加入  1:加入
    private String is_collect;
    private String is_support;
    private List<CoversBean> covers;
    private String share_img;

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

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

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<CommentBean> getContent_new() {
        return content_new;
    }

    public void setContent_new(List<CommentBean> content_new) {
        this.content_new = content_new;
    }

    private List<CommentBean> content_new;

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_reply_time() {
        return last_reply_time;
    }

    public void setLast_reply_time(String last_reply_time) {
        this.last_reply_time = last_reply_time;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover_ids() {
        return cover_ids;
    }

    public void setCover_ids(String cover_ids) {
        this.cover_ids = cover_ids;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getGroup_title() {
        return group_title;
    }

    public void setGroup_title(String group_title) {
        this.group_title = group_title;
    }

    public String getGroup_uid() {
        return group_uid;
    }

    public void setGroup_uid(String group_uid) {
        this.group_uid = group_uid;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(String supportCount) {
        this.supportCount = supportCount;
    }

    public String getBookmarkCount() {
        return BookmarkCount;
    }

    public void setBookmarkCount(String BookmarkCount) {
        this.BookmarkCount = BookmarkCount;
    }

    public int getIs_join() {
        return is_join;
    }

    public void setIs_join(int is_join) {
        this.is_join = is_join;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getIs_support() {
        return is_support;
    }

    public void setIs_support(String is_support) {
        this.is_support = is_support;
    }

    public List<CoversBean> getCovers() {
        return covers;
    }

    public void setCovers(List<CoversBean> covers) {
        this.covers = covers;
    }

    public static class UserBean {
        /**
         * uid : 83120
         * title : Lv1 实习
         * nickname : 151210
         * rank_link : []
         * score1 : 0
         * sex : 0
         * birthday : 0000-00-00
         * qq :
         * login : 37
         * reg_ip : 0
         * reg_time : 1529896691
         * last_login_ip : 2088632816
         * last_login_time : 1532775432
         * status : 2
         * last_login_role : 1
         * show_role : 1
         * signature :
         * pos_province : 0
         * pos_city : 0
         * pos_district : 0
         * pos_community : 0
         * score2 : 0
         * score3 : 0
         * score4 : 0
         * con_check : 0
         * total_check : 0
         * fans : 6
         * session_id :
         * alive_line : 0
         * id : 83120
         * username : 151210
         * password : tcMQrH
         * pay_pwd : tcMQrH
         * email :
         * mobile : 18701134428
         * from : 6
         * imei : 865795030434152
         * agentgame :
         * app_id : 362679
         * agent_id : 0
         * update_time : 1529924740
         * truename : 熊忠宇
         * idcard : 360103199105084114
         * bindmobile : 18701134428
         * bindemail :
         * portrait : http://mgc-games.com:8711/upload/20180710/5b445a240223a.png
         * regist_ip : 115.171.244.140
         * parent_mem_id : 0
         * p_mem_id : 0
         * wh_user_id : 0
         * wh_passwd : 12345678
         * public_key : MGC7NEZ7BsuRsbyjFqfWEqercxwS8ijpz5KqdDhWdffVPZiPAtgL3
         * affiliation : 1
         * real_nickname : 151210
         * avatar128 : http://sns.mgc-games.com/Uploads/AvatarPublic/images/default_avatar_128_128.jpg
         * avatar512 : http://sns.mgc-games.com/Uploads/AvatarPublic/images/default_avatar_512_512.jpg
         * is_follow : 0
         */

        private int uid;
        private String title;
        private String nickname;

        public String getLevel_pic() {
            return level_pic;
        }

        public void setLevel_pic(String level_pic) {
            this.level_pic = level_pic;
        }

        private String level_pic;
        private String score1;
        private String sex;
        private String birthday;
        private String qq;
        private String login;
        private String reg_ip;
        private String reg_time;
        private String last_login_ip;
        private String last_login_time;
        private String status;
        private String last_login_role;
        private String show_role;
        private String signature;
        private String pos_province;
        private String pos_city;
        private String pos_district;
        private String pos_community;
        private String score2;
        private String score3;
        private String score4;
        private String con_check;
        private String total_check;
        private String fans;
        private String session_id;
        private String alive_line;
        private String id;
        private String username;
        private String password;
        private String pay_pwd;
        private String email;
        private String mobile;
        @SerializedName("from")
        private String fromX;
        private String imei;
        @SerializedName("agentgame")
        private String agentgameX;
        @SerializedName("app_id")
        private String app_idX;
        private String agent_id;
        private String update_time;
        private String truename;
        private String idcard;
        private String bindmobile;
        private String bindemail;
        private String portrait;
        private String regist_ip;
        private String parent_mem_id;
        private String p_mem_id;
        private String wh_user_id;
        private String wh_passwd;
        private String public_key;
        private String affiliation;
        private String real_nickname;
        private String avatar128;
        private String avatar512;
        public int is_follow;
        private List<?> rank_link;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getReg_ip() {
            return reg_ip;
        }

        public void setReg_ip(String reg_ip) {
            this.reg_ip = reg_ip;
        }

        public String getReg_time() {
            return reg_time;
        }

        public void setReg_time(String reg_time) {
            this.reg_time = reg_time;
        }

        public String getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLast_login_role() {
            return last_login_role;
        }

        public void setLast_login_role(String last_login_role) {
            this.last_login_role = last_login_role;
        }

        public String getShow_role() {
            return show_role;
        }

        public void setShow_role(String show_role) {
            this.show_role = show_role;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getPos_province() {
            return pos_province;
        }

        public void setPos_province(String pos_province) {
            this.pos_province = pos_province;
        }

        public String getPos_city() {
            return pos_city;
        }

        public void setPos_city(String pos_city) {
            this.pos_city = pos_city;
        }

        public String getPos_district() {
            return pos_district;
        }

        public void setPos_district(String pos_district) {
            this.pos_district = pos_district;
        }

        public String getPos_community() {
            return pos_community;
        }

        public void setPos_community(String pos_community) {
            this.pos_community = pos_community;
        }

        public String getScore2() {
            return score2;
        }

        public void setScore2(String score2) {
            this.score2 = score2;
        }

        public String getScore3() {
            return score3;
        }

        public void setScore3(String score3) {
            this.score3 = score3;
        }

        public String getScore4() {
            return score4;
        }

        public void setScore4(String score4) {
            this.score4 = score4;
        }

        public String getCon_check() {
            return con_check;
        }

        public void setCon_check(String con_check) {
            this.con_check = con_check;
        }

        public String getTotal_check() {
            return total_check;
        }

        public void setTotal_check(String total_check) {
            this.total_check = total_check;
        }

        public String getFans() {
            return fans;
        }

        public void setFans(String fans) {
            this.fans = fans;
        }

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public String getAlive_line() {
            return alive_line;
        }

        public void setAlive_line(String alive_line) {
            this.alive_line = alive_line;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPay_pwd() {
            return pay_pwd;
        }

        public void setPay_pwd(String pay_pwd) {
            this.pay_pwd = pay_pwd;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFromX() {
            return fromX;
        }

        public void setFromX(String fromX) {
            this.fromX = fromX;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getAgentgameX() {
            return agentgameX;
        }

        public void setAgentgameX(String agentgameX) {
            this.agentgameX = agentgameX;
        }

        public String getApp_idX() {
            return app_idX;
        }

        public void setApp_idX(String app_idX) {
            this.app_idX = app_idX;
        }

        public String getAgent_id() {
            return agent_id;
        }

        public void setAgent_id(String agent_id) {
            this.agent_id = agent_id;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public String getBindmobile() {
            return bindmobile;
        }

        public void setBindmobile(String bindmobile) {
            this.bindmobile = bindmobile;
        }

        public String getBindemail() {
            return bindemail;
        }

        public void setBindemail(String bindemail) {
            this.bindemail = bindemail;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getRegist_ip() {
            return regist_ip;
        }

        public void setRegist_ip(String regist_ip) {
            this.regist_ip = regist_ip;
        }

        public String getParent_mem_id() {
            return parent_mem_id;
        }

        public void setParent_mem_id(String parent_mem_id) {
            this.parent_mem_id = parent_mem_id;
        }

        public String getP_mem_id() {
            return p_mem_id;
        }

        public void setP_mem_id(String p_mem_id) {
            this.p_mem_id = p_mem_id;
        }

        public String getWh_user_id() {
            return wh_user_id;
        }

        public void setWh_user_id(String wh_user_id) {
            this.wh_user_id = wh_user_id;
        }

        public String getWh_passwd() {
            return wh_passwd;
        }

        public void setWh_passwd(String wh_passwd) {
            this.wh_passwd = wh_passwd;
        }

        public String getPublic_key() {
            return public_key;
        }

        public void setPublic_key(String public_key) {
            this.public_key = public_key;
        }

        public String getAffiliation() {
            return affiliation;
        }

        public void setAffiliation(String affiliation) {
            this.affiliation = affiliation;
        }

        public String getReal_nickname() {
            return real_nickname;
        }

        public void setReal_nickname(String real_nickname) {
            this.real_nickname = real_nickname;
        }

        public String getAvatar128() {
            return avatar128;
        }

        public void setAvatar128(String avatar128) {
            this.avatar128 = avatar128;
        }

        public String getAvatar512() {
            return avatar512;
        }

        public void setAvatar512(String avatar512) {
            this.avatar512 = avatar512;
        }

        public int getIs_follow() {
            return is_follow;
        }

        public void setIs_follow(int is_follow) {
            this.is_follow = is_follow;
        }

        public List<?> getRank_link() {
            return rank_link;
        }

        public void setRank_link(List<?> rank_link) {
            this.rank_link = rank_link;
        }
    }

    public static class CoversBean {
        /**
         * id : 582
         * url : http://download.mgc-games.com/picture/2/83120/5b596a1c50bff.jpg
         */

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

    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    private ShareBean share;
}
