package com.mgc.letobox.happy.find.bean;

import java.util.List;

/**
 * Created by liu hong liang on 2016/11/14.
 */

public class ArticleCommentListResultBean {


    /**
     * id : 4
     * uid : 82833
     * app : News
     * mod : index
     * row_id : 2
     * parse : 1
     * content : 不错
     * create_time : 07月23日 19:54
     * pid : 0
     * status : 1
     * ip : 0
     * area :
     * is_support : 1
     * user : {"uid":"82833","title":"Lv1 实习","nickname":"18519135802","score1":"0","sex":"0","birthday":"0000-00-00","qq":"","login":"7","reg_ip":"0","reg_time":"1526628449","last_login_ip":"2088633010","last_login_time":"1532071137","status":"2","last_login_role":"1","show_role":"1","signature":"","pos_province":"0","pos_city":"0","pos_district":"0","pos_community":"0","score2":"0","score3":"0","score4":"0","con_check":"0","total_check":"0","fans":"0","session_id":"","alive_line":"0","id":"82833","username":"154432","password":"C8TxHP","pay_pwd":"C8TxHP","email":"","mobile":"18519135812","from":"6","imei":"867391039456384","agentgame":"","app_id":"362679","agent_id":"0","update_time":"1527235748","truename":"赵志辉","idcard":"430521198209074955","bindmobile":"18519135812","bindemail":"","portrait":"/upload/20180619/5b28af0a20c13.png","regist_ip":"115.171.245.8","parent_mem_id":"0","p_mem_id":"0","wh_user_id":"0","wh_passwd":"12345678","public_key":"MGC5pe78AVYEZ3bGGECL6KtmaUfQjasTqmp9Qe4nZEqLK3BUpTs8t","affiliation":"1","real_nickname":"18519135802","avatar128":"http://sns.mgc-games.com/Uploads/AvatarPublic/images/default_avatar_128_128.jpg","avatar512":"http://sns.mgc-games.com/Uploads/AvatarPublic/images/default_avatar_512_512.jpg"}
     * is_landlord : 0
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

    public String getIs_landlord() {
        return is_landlord;
    }

    public void setIs_landlord(String is_landlord) {
        this.is_landlord = is_landlord;
    }

    public static class UserBean {
        /**
         * uid : 82833
         * title : Lv1 实习
         * nickname : 18519135802
         * score1 : 0
         * sex : 0
         * birthday : 0000-00-00
         * qq :
         * login : 7
         * reg_ip : 0
         * reg_time : 1526628449
         * last_login_ip : 2088633010
         * last_login_time : 1532071137
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
         * fans : 0
         * session_id :
         * alive_line : 0
         * id : 82833
         * username : 154432
         * password : C8TxHP
         * pay_pwd : C8TxHP
         * email :
         * mobile : 18519135812
         * from : 6
         * imei : 867391039456384
         * agentgame :
         * app_id : 362679
         * agent_id : 0
         * update_time : 1527235748
         * truename : 赵志辉
         * idcard : 430521198209074955
         * bindmobile : 18519135812
         * bindemail :
         * portrait : /upload/20180619/5b28af0a20c13.png
         * regist_ip : 115.171.245.8
         * parent_mem_id : 0
         * p_mem_id : 0
         * wh_user_id : 0
         * wh_passwd : 12345678
         * public_key : MGC5pe78AVYEZ3bGGECL6KtmaUfQjasTqmp9Qe4nZEqLK3BUpTs8t
         * affiliation : 1
         * real_nickname : 18519135802
         * avatar128 : http://sns.mgc-games.com/Uploads/AvatarPublic/images/default_avatar_128_128.jpg
         * avatar512 : http://sns.mgc-games.com/Uploads/AvatarPublic/images/default_avatar_512_512.jpg
         */

        private String uid;
        private String title;
        private String nickname;
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
        private String from;
        private String imei;
        private String agentgame;
        private String app_id;
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

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getAgentgame() {
            return agentgame;
        }

        public void setAgentgame(String agentgame) {
            this.agentgame = agentgame;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
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


        public String getLevel_pic() {
            return level_pic;
        }

        public void setLevel_pic(String level_pic) {
            this.level_pic = level_pic;
        }

        public String level_pic;
    }
}
