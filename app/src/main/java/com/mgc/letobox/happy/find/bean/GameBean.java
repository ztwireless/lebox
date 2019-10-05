package com.mgc.letobox.happy.find.bean;

import com.mgc.letobox.happy.find.bean.ShareBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu hong liang on 2016/9/18.
 */
public class GameBean implements Serializable {
    private String gameid;
    private String icon;
    private String gamename;
    private String type;
    private String runtime;
    private String category;
    private String packagename;
    private String hot;
    private String downcnt;
    private float score;
    private String distype; //0 无折扣 1 折扣 2 返利
    private String discount;//折扣；牛刀：续充折扣
    private float first_discount;//牛刀：首充折扣
    private String rebate;//返利比例，是小数
    private String likecnt;
    private String sharecnt;
    private String downlink;
    private String oneword;
    private String size;
    private String lang;
    private String sys;
    private String desc;
    private String verid;
    private String version;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    private String company_name;
    private long create_time;
    private String giftcnt;//礼包数量
    private String newscnt;
    private int is_own;//判断充值是否显示
    private int servercnt;
    private int rank; // 排行
    private float star_cnt; // 评分

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    private String  share_url; //分享链接

    public int getServercnt() {
        return servercnt;
    }

    public void setServercnt(int servercnt) {
        this.servercnt = servercnt;
    }

    public List<GameServer> getSerlist() {
        return serlist;
    }

    public void setSerlist(List<GameServer> serlist) {
        this.serlist = serlist;
    }

    private List<GameServer> serlist;
//    2016.10.26 欧忠富沟通的那个用户需要显示的充值按钮是否显示折扣返利
//    benefit_type跟rate，benefit_type为0表示没有充值跟折扣，1为折扣，2为返利，rate为比率

    private String benefit_type;
    private String rate;

    public float getStar_cnt() {
        return star_cnt;
    }

    public void setStar_cnt(float star_cnt) {
        this.star_cnt = star_cnt;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    private String url;
    private ArrayList<String> image;

    private int itemPosition;//列表中的位置，用于判别图标

    public float getFirst_discount() {
        return first_discount;
    }

    public void setFirst_discount(float first_discount) {
        this.first_discount = first_discount;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public String getNewscnt() {
        return newscnt;
    }

    public void setNewscnt(String newscnt) {
        this.newscnt = newscnt;
    }

    public String getGameid() {
        return gameid;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getDowncnt() {
        return downcnt;
    }

    public void setDowncnt(String downcnt) {
        this.downcnt = downcnt;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getDistype() {
        return distype;
    }

    public void setDistype(String distype) {
        this.distype = distype;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRebate() {
        return rebate;
    }

    public void setRebate(String rebate) {
        this.rebate = rebate;
    }

    public String getLikecnt() {
        return likecnt;
    }

    public void setLikecnt(String likecnt) {
        this.likecnt = likecnt;
    }

    public String getSharecnt() {
        return sharecnt;
    }

    public void setSharecnt(String sharecnt) {
        this.sharecnt = sharecnt;
    }

    public String getDownlink() {
        return downlink;
    }

    public void setDownlink(String downlink) {
        this.downlink = downlink;
    }

    public String getOneword() {
        return oneword;
    }

    public void setOneword(String oneword) {
        this.oneword = oneword;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVerid() {
        return verid;
    }

    public void setVerid(String verid) {
        this.verid = verid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String vername) {
        this.version = vername;
    }

    public String getGiftcnt() {
        return giftcnt;
    }

    public void setGiftcnt(String giftcnt) {
        this.giftcnt = giftcnt;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBenefit_type() {
        return benefit_type;
    }

    public void setBenefit_type(String benefit_type) {
        this.benefit_type = benefit_type;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public int getIs_own() {
        return is_own;
    }

    public void setIs_own(int is_own) {
        this.is_own = is_own;
    }


    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }




    public class GameServer{
        public int serid;
        public  String sername;
        public  String serdesc;
        public  int status;
        public  int starttime;
    }

    public String adid;  // Androidid
    public String iosid;  //iosid

    public String getAdid() {
        return adid;
    }

    public void setAdid(String adid) {
        this.adid = adid;
    }

    public String getIosid() {
        return iosid;
    }

    public void setIosid(String iosid) {
        this.iosid = iosid;
    }

    public String getAndroidurl() {
        return androidurl;
    }

    public void setAndroidurl(String androidurl) {
        this.androidurl = androidurl;
    }

    public String androidurl;  //android 下载地址

    public int getIs_mine() {
        return is_mine;
    }

    public void setIs_mine(int is_mine) {
        this.is_mine = is_mine;
    }

    public int is_mine;

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public List<CommentStar> getStar_list() {
        return star_list;
    }

    private int comment;

    private List<CommentStar> star_list;

    public void setStar_list(List<CommentStar> star_list) {
        this.star_list = star_list;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    private UserBean user;

    public class UserBean{
        private int star;

        public String getLevel_pic() {
            return level_pic;
        }

        public void setLevel_pic(String level_pic) {
            this.level_pic = level_pic;
        }

        private String level_pic;

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }
    }

    public class CommentStar{
        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        int star;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        int count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }

    public int getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(int is_collect) {
        this.is_collect = is_collect;
    }

    private int collect_count;
    private int is_collect;

    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    private ShareBean share;

}
