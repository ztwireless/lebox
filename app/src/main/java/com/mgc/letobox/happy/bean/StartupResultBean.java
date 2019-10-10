package com.mgc.letobox.happy.bean;

import android.support.annotation.Keep;

/**
 * Created by liu hong liang on 2016/11/11.
 */
@Keep
public class StartupResultBean {

    private String check;//	STRING	是否切换用户 0 不更换 1 更换支付 2 重新初始化
    private String ip;//	STRING	玩家公网IP
    private String user_token;//	STRING	玩家此次连接token
    private String up_status;//	STRING	是否更新 0 无版本更新 1 版本更新
    private String up_url;//	STRING	更新地址
    private String agentgame;//	STRING	玩家游戏渠道编号
    private long timestamp;//	STRING	服务器时间戳
    private HelpBean help;//	JSON二维	客服信息
    private int is_login; // 1: 已登陆  2: 未登陆

    public int getIs_login() { return is_login; }

    public void setIs_login(int is_login) { this.is_login = is_login; }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getUp_status() {
        return up_status;
    }

    public void setUp_status(String up_status) {
        this.up_status = up_status;
    }

    public String getUp_url() {
        return up_url;
    }

    public void setUp_url(String up_url) {
        this.up_url = up_url;
    }

    public String getAgentgame() {
        return agentgame;
    }

    public void setAgentgame(String agentgame) {
        this.agentgame = agentgame;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public HelpBean getHelp() {
        return help;
    }

    public void setHelp(HelpBean help) {
        this.help = help;
    }

    public static class HelpBean {
        private String qq;//	STRING	客服qq
        private String qqgroup;//	STRING	客服qq群号
        private String wx;//	STRING	客服微信
        private String tel;//	STRING	客服电话

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getQqgroup() {
            return qqgroup;
        }

        public void setQqgroup(String qqgroup) {
            this.qqgroup = qqgroup;
        }

        public String getWx() {
            return wx;
        }

        public void setWx(String wx) {
            this.wx = wx;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }


}
