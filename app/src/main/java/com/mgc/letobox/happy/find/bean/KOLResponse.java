package com.mgc.letobox.happy.find.bean;

/**
 * Created by DELL on 2018/7/27.
 */

public class KOLResponse extends BaseResultBean {

    /**
     * kol_id : 83209
     * nickname : 大菠萝
     * fans : 0
     * signature :
     * portrait :
     * is_follow : 0
     * mobile : 17701002000
     * is_kol : 1
     * follow_count : 1
     */

    private int kol_id;
    private String nickname;
    private int fans;
    private String signature;
    private String portrait;
    public int is_follow;
    private String mobile;
    private int is_kol;
    private int follow_count;
    private String share_url;

    public String getLevel_pic() {
        return level_pic;
    }

    public void setLevel_pic(String level_pic) {
        this.level_pic = level_pic;
    }

    private String level_pic;

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getKol_id() {
        return kol_id;
    }

    public void setKol_id(int kol_id) {
        this.kol_id = kol_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIs_kol() {
        return is_kol;
    }

    public void setIs_kol(int is_kol) {
        this.is_kol = is_kol;
    }

    public int getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }


    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    private ShareBean share;
}
