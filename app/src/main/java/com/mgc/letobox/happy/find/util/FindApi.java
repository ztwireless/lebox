package com.mgc.letobox.happy.find.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by liu hong liang on 2016/11/9.
 */

public class FindApi {
    private static final String TAG = FindApi.class.getSimpleName();
    public static String requestUrl;
//    private static final String BASE_URL = "http://miniapi_dev.mgc-games.com/api/v7/";
    private static final String BASE_URL = "http://kx.mgc-games.com/api/v7/";

    private static String getRequestUrl() {
        if (!TextUtils.isEmpty(requestUrl)) {
            return requestUrl;
        } else {
            requestUrl = BASE_URL;
        }
        return requestUrl;
    }

    public static String getStartup() {
        printUrl("system/startup");
        return getRequestUrl() + "system/startup";
    }

    // 获取更多小程序
    public static String getMinigameMore() {
        printUrl("charge/more");
        return getRequestUrl() + "charge/more";
    }

    // 获取小程序游戏首页
    public static String getMinigameList() {
        printUrl("charge/groups");
        return getRequestUrl() + "charge/groups";
    }

    // 获取引导页信息
    public static String getPersonalGuidance() {
        printUrl("personal/guidance");
        return getRequestUrl() + "personal/guidance";
    }

    // 获取首页是否显示
    public static String getIsShowHome() {
        printUrl("charge/homeShow");
        return getRequestUrl() + "charge/homeShow";
    }

    // 获取引导页信息
    public static String getPersonalSetGuidance() {
        printUrl("personal/setGuidance");
        return getRequestUrl() + "personal/setGuidance";
    }

    //一键登陆or注册
    public static String getLoginRegister() {
        printUrl("user/loadMgcByMobile");
        return getRequestUrl() + "user/loadMgcByMobile";
    }

    public static String getLoginMobile() {
        printUrl("user/loginmobile");
        return getRequestUrl() + "user/loginmobile";
    }

    public static String getRegisterMobile() {
        printUrl("user/registermobile");
        return getRequestUrl() + "user/registermobile";
    }

    public static String getSmsSend() {
        printUrl("sms/send");
        return getRequestUrl() + "sms/send";
    }

    public static String getLoginSmsSend() {
        printUrl("wallet/send");
        return getRequestUrl() + "wallet/send";
    }

    public static String getMgcTokenList() {
        printUrl("mgc/tokenList");
        return getRequestUrl() + "mgc/tokenList";
    }

    public static String getMgcTokens() {
        printUrl("mgc/tokens");
        return getRequestUrl() + "mgc/tokens";
    }

    public static String getTransferHistory() {
        printUrl("mgc/transferHistory");
        return getRequestUrl() + "mgc/transferHistory";
    }

    public static String getTransactionHistory() {
        printUrl("mgc/allHistory");
        return getRequestUrl() + "mgc/allHistory";
    }


    public static String getTransferDetail() {
        printUrl("mgc/transaction");
        return getRequestUrl() + "mgc/transaction";
    }

    public static String getTransfer() {
        printUrl("mgc/transfer");
        return getRequestUrl() + "mgc/transfer";
    }

    public static String getUpdateAvatar() {
        printUrl("mgc/portrait/update");
        return getRequestUrl() + "mgc/portrait/update";
    }

    public static String getUpdateUserInfo() {
        printUrl("user/info/update");
        return getRequestUrl() + "user/info/update";
    }

    //修改密码
    public static String getModPasswd() {
        printUrl("mod/modpwd");
        return getRequestUrl() + "mod/modpwd";
    }

    //验证手机号
    public static String verifyPhone() {
        printUrl("user/phone/verify");
        return getRequestUrl() + "user/phone/verify";
    }

    //绑定手机号
    public static String bindPhone() {
        printUrl("mod/modMobile");
        return getRequestUrl() + "mod/modMobile";
    }


    //创建红包
    public static String createRedEnvelop() {
        printUrl("envelope/create");
        return getRequestUrl() + "envelope/create";
    }

    //获得红包信息
    public static String getRedEnvelopInfo() {
        printUrl("envelope/get");
        return getRequestUrl() + "envelope/get";
    }

    //领取一个红包
    public static String getRedEnvelop() {
        printUrl("envelope/claim");
        return getRequestUrl() + "envelope/claim";
    }

    //获取MGCT余额
    public static String getMGCTBalance() {
        printUrl("mgc/balance");
        return getRequestUrl() + "mgc/balance";
    }

    //获取渠道token
    public static String getChannelToken() {
        printUrl("mgc/channelToken");
        return getRequestUrl() + "mgc/channelToken";
    }

    //游戏评论列表
    public static String getGameCommentList() {
        printUrl("game/comment_list");
        return getRequestUrl() + "game/comment_list";
    }

    //游戏评论
    public static String getGameComment() {
        printUrl("game/comment_put");
        return getRequestUrl() + "game/comment_put";
    }
    //游戏评论-图文混排
    public static String getGameCommentTwhp() {
        printUrl("game/comment_put_twhp");
        return getRequestUrl() + "game/comment_put_twhp";
    }


    //获取游戏列表
    public static String getGameList() {
        return getRequestUrl() + "game/list";
    }

    //获取游戏详情信息
    public static String getGameDetail() {
        return getRequestUrl() + "game/detail";
    }

    //获取游戏下载地址
    public static String getGameDown() {
        return getRequestUrl() + "game/down";
    }

    public static String getRegisterOne() {
        return getRequestUrl() + "user/registerone";
    }

    public static String getRegister() {
        return getRequestUrl() + "user/register";
    }

    public static String getUproleinfo() {
        return getRequestUrl() + "user/uproleinfo";
    }

    public static String getNotice() {
        return getRequestUrl() + "system/notice";
    }

    public static String getLogout() {
        return getRequestUrl() + "user/logout";
    }


    public static String getQueryorder() {
        return getRequestUrl() + "pay/queryorder";
    }

    public static String getWebSdkPayNew() {
        return getRequestUrl() + "pay/vertical";
    }

    //下面的都是网页请求
    public static String getWebSdkPay() {
        return getRequestUrl() + "pay/sdkpay";
    }

    public static String getWebUser() {
        return getRequestUrl() + "web/user/index";
    }

    public static String getWebIdentify() {
        return getRequestUrl() + "web/indentify/index";
    }

    public static String getWallet() {
        return getRequestUrl() + "pay/wallet/download";
    }

    //获取挖矿节点
    public static String getNode() {
        printUrl("mine/getnode");
        return getRequestUrl() + "mine/getnode";
    }

    //获取挖矿红包奖励
    public static String getRedpacket() {
        printUrl("mine/award");
        return getRequestUrl() + "mine/award";
    }

    //获取挖矿红包奖励
    public static String isWalletUser() {
        printUrl("mine/isWalletUser");
        return getRequestUrl() + "mine/isWalletUser";
    }

    //获取挖矿信息
    public static String getMiningTick() {
        printUrl("mine/tick");
        return getRequestUrl() + "mine/tick";
    }
    //获取首页资产数据
    public static String getMgcProperty() {
        printUrl("mgc/property");
        return getRequestUrl() + "mgc/property";
    }


    //获取资产交易平台banner列表
    public static String getPropertyBanner() {
        printUrl("property/pics");
        return getRequestUrl() + "property/pics";
    }

    //获取资产交易平台游戏列表
    public static String getTrandeGameList() {
        printUrl("property/gameTrans");
        return getRequestUrl() + "property/gameTrans";
    }

    //获取我的资产游戏列表
    public static String getMyPropertyGame() {
        printUrl("property/mygames");
        return getRequestUrl() + "property/mygames";
    }

    //获取可售游戏列表
    public static String getMyTradeGames() {
        printUrl("property/games");
        return getRequestUrl() + "property/games";
    }

    // 获取游戏账号列表
    public static String getSallingAccount() {
        printUrl("property/sellingAccounts");
        return getRequestUrl() + "property/sellingAccounts";
    }

    //修改交易商品信息
    public static String getPropertyUpdate() {
        printUrl("property/update");
        return getRequestUrl() + "property/update";
    }

    //修改交易商品价格
    public static String getPropertyUpdatePrice() {
        printUrl("property/updatePrice");
        return getRequestUrl() + "property/updatePrice";
    }

    //下架
    public static String getPropertySoldOut() {
        printUrl("property/soldOut");
        return getRequestUrl() + "property/soldOut";
    }

    //获取用户资产交易列表
    public static String getMyPropertyList() {
        printUrl("property/propertys");
        return getRequestUrl() + "property/propertys";
    }

    // 获取我的游戏小号地址
    public static String getMyGameRole() {
        printUrl("property/myroles");
        return getRequestUrl() + "property/myroles";
    }


    //申请交易上架
    public static String getSaleApply() {
        printUrl("property/applyForSale");
        return getRequestUrl() + "property/applyForSale";
    }

    //申请交易重新上架
    public static String getSaleApplyAgain() {
        printUrl("property/applyForSaleAgain");
        return getRequestUrl() + "property/applyForSaleAgain";
    }

    //购买资产
    public static String getPropertyPurchase() {
        printUrl("property/trade");
        return getRequestUrl() + "property/trade";
    }

    // 获取游戏小号列表
    public static String getSaleAccount() {
        printUrl("property/child");
        return getRequestUrl() + "property/child";
    }

    // 获取游戏区服
    public static String getGameServer() {
        printUrl("property/game_server");
        return getRequestUrl() + "property/game_server";
    }

    // 获取游戏区服
    public static String getGameAccountRole() {
        printUrl("property/role");
        return getRequestUrl() + "property/role";
    }

    //获取最新版本
    public static String getLatestVersion() {
        printUrl("upgrade/up");
        return getRequestUrl() + "upgrade/up";
    }

    //获取分享的钱包地址
    public static String getWalletAddress() {
        printUrl("mgc/walletAddress");
        return getRequestUrl() + "mgc/walletAddress";
    }

    //创建圈子
    public static String getCreateCircle() {
        printUrl("circle/create");
        return getRequestUrl() + "circle/create";
    }

    //圈子详情
    public static String getCircleDetail() {
        printUrl("circle/info");
        return getRequestUrl() + "circle/info";
    }

    //圈子列表
    public static String getCircleGroups() {
        printUrl("circle/groups");
        return getRequestUrl() + "circle/groups";
    }

    //换一批圈子
    public static String getHotGroups() {
        printUrl("circle/groups_hot");
        return getRequestUrl() + "circle/groups_hot";
    }

    //用户签到信息
    public static String getSignList() {
        printUrl("sign/list");
        return getRequestUrl() + "sign/list";
    }

    //个人任务列表
    public static String getTaskList() {
        printUrl("task/list");
        return getRequestUrl() + "task/list";
    }

    //用户签到
    public static String getSignIn() {
        printUrl("sign/sign");
        return getRequestUrl() + "sign/sign";
    }

    //加QQ群
    public static String getQQInfo() {
        printUrl("sign/getQQInfo");
        return getRequestUrl() + "sign/getQQInfo";
    }

    //我的圈子列表
    public static String getMyGuoups() {
        printUrl("circle/mygroups");
        return getRequestUrl() + "circle/mygroups";
    }

    //奖品列表
    public static String getLotteryList() {
        printUrl("lottery/list");
        return getRequestUrl() + "lottery/list";
    }

    //抽奖
    public static String getLottery() {
        printUrl("lottery/lottery");
        return getRequestUrl() + "lottery/lottery";
    }

    // 所有用户的中奖记录
    public static String getUsersLog() {
        printUrl("lottery/users_log");
        return getRequestUrl() + "lottery/users_log";
    }

    // 我的中奖记录
    public static String getMyLog() {
        printUrl("lottery/my_log");
        return getRequestUrl() + "lottery/my_log";
    }

    // 今日福利
    public static String getWalfares() {
        printUrl("lottery/welfares");
        return getRequestUrl() + "lottery/welfares";
    }


    // 我的评论-文章
    public static String getNewsEvaluates() {
        printUrl("personal/newsEvaluates");
        return getRequestUrl() + "personal/newsEvaluates";
    }

    // 我的帖子
    public static String getPersonal() {
        printUrl("personal/group_posts");
        return getRequestUrl() + "personal/group_posts";
    }

    //获取默认头像列表
    public static String getAvatarList() {
        printUrl("personal/portraits");
        return getRequestUrl() + "personal/portraits";
    }

    //选择默认头像
    public static String setAvatarDefault() {
        printUrl("personal/setPortrait");
        return getRequestUrl() + "personal/setPortrait";
    }

    // 游戏列表
    public static String getRankGame() {
        printUrl("rank/game");
        return getRequestUrl() + "rank/game";
    }

    // 分类列表
    public static String getRankCategory() {
        printUrl("rank/category");
        return getRequestUrl() + "rank/category";
    }

    // 厂商列表
    public static String getRankCp() {
        printUrl("rank/cp");
        return getRequestUrl() + "rank/cp";
    }

    // 我的评论-帖子
    public static String getPostEvaluates() {
        printUrl("personal/postEvaluates");
        return getRequestUrl() + "personal/postEvaluates";
    }

    //圈子帖子列表
    public static String getCirclePostList() {
        printUrl("circle/post_list");
        return getRequestUrl() + "circle/post_list";
    }

    //加入圈子
    public static String getCircleJoin() {
        printUrl("circle/join");
        return getRequestUrl() + "circle/join";
    }

    //退出圈子
    public static String getCircleQuit() {
        printUrl("circle/quit");
        return getRequestUrl() + "circle/quit";
    }

    //上传图片
    public static String postImage() {
        printUrl("picture/setPicture");
        return getRequestUrl() + "picture/setPicture";
    }

    //圈子发帖
    public static String postPut() {
        printUrl("circle/post_put");
        return getRequestUrl() + "circle/post_put";
    }
    //圈子发帖
    public static String postPut_twhp() {
        printUrl("circle/post_put_new");
        return getRequestUrl() + "circle/post_put_new";
    }

    //圈子编辑帖字
    public static String edit_TieZi() {
        printUrl("circle/post_edit");
        return getRequestUrl() + "circle/post_edit";
    }

    //圈子编辑帖子-图文混排
    public static String edit_TieZi_twhp() {
        printUrl("circle/post_edit_twhp");
        return getRequestUrl() + "circle/post_edit_twhp";
    }


    //获取游戏专辑列表
    public static String getGameSubjectList() {
        printUrl("get_index");
        return getRequestUrl() + "get_index";
    }

    //获取游戏专辑详情
    public static String getIssueDetail() {
        printUrl("issue/detail");
        return getRequestUrl() + "issue/detail";
    }
    //获取游戏专辑评论列表
    public static String getIssueCommentList() {
        printUrl("issue/comment_list");
        return getRequestUrl() + "issue/comment_list";
    }

    //获取游戏分享页面
    public static String getGameShareUrl() {
        printUrl("/index.php?s=mgcshare/game/game_id/");
        return BASE_URL + "/index.php?s=mgcshare/game/game_id/";
    }

    //收藏专辑
    public static String getIssueFoverite() {
        printUrl("issue/collect");
        return getRequestUrl() + "issue/collect";
    }

    //评论专辑
    public static String getIssueComment() {
        printUrl("issue/comment");
        return getRequestUrl() + "issue/comment";
    }

    //评论点赞
    public static String getSupportComment() {
        printUrl("publics/support");
        return getRequestUrl() + "publics/support";
    }

    //发布文章
    public static String getArticlePublish() {
        printUrl("news/article_put");
        return getRequestUrl() + "news/article_put";
    }
    //发布文章-图文混排
    public static String getArticlePublish_twhp() {
        printUrl("news/article_put_twhp");
        return getRequestUrl() + "news/article_put_twhp";
    }

    //发布组图文章
    public static String getPicsPublish() {
        printUrl("crowd/pics_put");
        return getRequestUrl() + "crowd/pics_put";
    }

    //获取帖子详情
    public static String getPostDetail() {
        printUrl("circle/post_detail");
        return getRequestUrl() + "circle/post_detail";
    }

    //获取文章详情
    public static String getNewsDetail() {
        printUrl("news/news_detail");
        return getRequestUrl() + "news/news_detail";
    }

    //获取用户详情
    public static String getKoLDetail() {
        printUrl("user/kol_detail");
        return getRequestUrl() + "user/kol_detail";
    }

    //获取用户推荐游戏和玩过的游戏
    public static String getUserGame() {
        printUrl("game/recommand_game");
        return getRequestUrl() + "game/recommand_game";
    }

    // 获取用户文章列表
    public static String getNewsList() {
        printUrl("news/kol_news_list");
        return getRequestUrl() + "news/kol_news_list";
    }

    //关注|取消关注
    public static String getUserFollow() {
        printUrl("user/follow");
        return getRequestUrl() + "user/follow";
    }
    //删除帖子
    public static String getPostDel() {
        printUrl("circle/post_del");
        return getRequestUrl() + "circle/post_del";
    }

    //上传图片
    public static String getUploadImage() {
        printUrl("picture/setPicture");
        return getRequestUrl() + "picture/setPicture";
    }

    //获取推荐和关注列表
    public static String getArticleList() {
        printUrl("news/news_list");
        return getRequestUrl() + "news/news_list";
    }

    //举报圈子
    public static String getInformCircle() {
        printUrl("circle/inform");
        return getRequestUrl() + "circle/inform";
    }

    //获取资讯评论列表
    public static String getNewsCommentList() {
        printUrl("news/news_comment_list");
        return getRequestUrl() + "news/news_comment_list";
    }

    //获取帖子评论列表
    public static String getPostCommentList() {
        printUrl("circle/post_comment_list");
        return getRequestUrl() + "circle/post_comment_list";
    }

    //圈子帖子评论
    public static String getPostComment() {
        printUrl("circle/post_comment");
        return getRequestUrl() + "circle/post_comment";
    }
    //圈子帖子评论-图文混排
    public static String getPostComment_twhp() {
        printUrl("circle/post_comment");
        return getRequestUrl() + "circle/post_comment_twhp";
    }

    //文章评论
    public static String getNewsComment() {
        printUrl("news/news_comment");
        return getRequestUrl() + "news/news_comment";
    }

    //文章评论-图文混排
    public static String getNewsComment_twhp() {
        printUrl("news/news_comment_twhp");
        return getRequestUrl() + "news/news_comment_twhp";
    }

    //发布专辑评论
    public static String putIssueComment() {
        printUrl("issue/comment_put");
        return getRequestUrl() + "issue/comment_put";
    }
    //发布专辑评论-图文混排
    public static String putIssueComment_twhp() {
        printUrl("issue/comment_put_twhp");
        return getRequestUrl() + "issue/comment_put_twhp";
    }




    //获取奖励
    public static String getStimulate() {
        printUrl("publics/stimulate");
        return getRequestUrl() + "publics/stimulate";
    }


    //个人中心-好友列表，关注列表，粉丝列表
    public static String getPersonalFriend() {
        printUrl("personal/friends");
        return getRequestUrl() + "personal/friends";
    }
    //个人中心-玩过的游戏
    public static String getPersonalGame() {
        printUrl("personal/games");
        return getRequestUrl() + "personal/games";
    }
    //个人中心-我的专辑收藏
    public static String getPersonalCollects() {
        printUrl("personal/collects");
        return getRequestUrl() + "personal/collects";
    }
    //个人中心-我的游戏收藏
    public static String getPersonalGameCollects() {
        printUrl("personal/gamesCollect");
        return getRequestUrl() + "personal/gamesCollect";
    }



    //个人中心-我的游戏评价
    public static String getPersonalGameEvaluates() {
        printUrl("personal/gameEvaluates");
        return getRequestUrl() + "personal/gameEvaluates";
    }

    //个人中心-我的投稿
    public static String getPersonalNews() {
        printUrl("personal/news");
        return getRequestUrl() + "personal/news";
    }
    //个人中心-我的签名
    public static String getPersonalSignature() {
        printUrl("personal/signature");
        return getRequestUrl() + "personal/signature";
    }
    //个人中心-数据统计
    public static String getPersonalStatistics() {
        printUrl("personal/statistics");
        return getRequestUrl() + "personal/statistics";
    }
    //个人中心-等级列表信息
    public static String getPersonalGradeList() {
        printUrl("personal/gradeList");
        return getRequestUrl() + "personal/gradeList";
    }
    //个人中心-用户的等级信息
    public static String getPersonalUserGrade() {
        printUrl("personal/userGrade");
        return getRequestUrl() + "personal/userGrade";
    }

    //我的邀请统计数据
    public static String getInviteStatistic() {
        printUrl("personal/shareStuCount");
        return getRequestUrl() + "personal/shareStuCount";
    }
    //我的邀请记录
    public static String getMyInvite() {
        printUrl("personal/shareMem");
        return getRequestUrl() + "personal/shareMem";
    }


    //提现-支付宝
    public static String getDrawCashAlipayInfo() {
        printUrl("wx/getAlipayInfo");
        return getRequestUrl() + "wx/getAlipayInfo";
    }
    //提现-微信
    public static String getDrawCashWeixinInfo() {
        printUrl("wx/getWxInfo");
        return getRequestUrl() + "wx/getWxInfo";
    }

    //提现-绑定支付宝
    public static String getDrawCashBindAlipay() {
        printUrl("wx/bindAlipay");
        return getRequestUrl() + "wx/bindAlipay";
    }

    //提现-绑定微信
    public static String getDrawCashBindWeixin() {
        printUrl("wx/setWxInfo");
        return getRequestUrl() + "wx/setWxInfo";
    }

    //提现-查询提现状态
    public static String getDrawCashStatus() {
        printUrl("wx/isDraw");
        return getRequestUrl() + "wx/isDraw";
    }

    //提现-查询提现记录
    public static String getDrawCashLog() {
        printUrl("wx/cashLog");
        return getRequestUrl() + "wx/cashLog";
    }
    //提现-申请提现
    public static String getDrawCashApply() {
        printUrl("wx/apply");
        return getRequestUrl() + "wx/apply";
    }

    //提现-实名认证
    public static String getIDCardCheck() {
        printUrl("idcard/check");
        return getRequestUrl() + "idcard/check";
    }

    //提现-获取TT余额
    public static String getDrawCashTTBalance() {
        printUrl("wx/ttBalance");
        return getRequestUrl() + "wx/ttBalance";
    }
    //提现- 常见说明
    public static String getDrawCashQR() {
        printUrl(BASE_URL + "/index.php?s=aboutProblems/index");
        return BASE_URL + "/index.php?s=aboutProblems/index";
    }

    //提现- 认证识别
    public static String getIdcardFace() {
        printUrl("idcard/face");
        return getRequestUrl() + "idcard/face";
    }

    //服务协议
    public static String getServiceAgreement() {
        Log.e(TAG, "http_url=" + BASE_URL + "/index.php?s=index/mgcqb_user_agreement.html");
        return BASE_URL + "/index.php?s=index/mgcqb_user_agreement.html";
    }

    //游戏收藏
    public static String getFavoriteGame() {
        printUrl("game/collect");
        return getRequestUrl() + "game/collect";
    }

    //获取资讯的类别
    public static String getNewsCategory() {
        printUrl("news/category");
        return getRequestUrl() + "news/category";
    }

    public static String getWebBbs() {
        return getRequestUrl() + "web/bbs/index";
    }

    public static String getWebGift() {
        return getRequestUrl() + "web/gift/index";
    }

    public static String getWebHelp() {
        return getRequestUrl() + "web/help/index";
    }

    public static String getWebForgetpwd() {
        return getRequestUrl() + "web/forgetpwd/index";
    }

    private static void printUrl(String path) {
        Log.e(TAG, "http_url=" + getRequestUrl() + path);
    }
}
