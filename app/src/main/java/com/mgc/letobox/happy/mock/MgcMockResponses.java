package com.mgc.letobox.happy.mock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/** Local responses for the retired miniapi_dev server. */
final class MgcMockResponses {
    static final String RETIRED_HOST = "miniapi_dev.mgc-games.com";

    private MgcMockResponses() {
    }

    static boolean handles(String url) {
        try {
            return RETIRED_HOST.equalsIgnoreCase(new URL(url).getHost());
        } catch (Exception ignored) {
            return false;
        }
    }

    static String responseFor(String url) {
        try {
            String path = new URL(url).getPath();
            path = path == null ? "" : path;
            JSONObject envelope = new JSONObject();
            envelope.put("code", responseCodeFor(path));
            envelope.put("msg", responseMessageFor(path));
            Object data = dataFor(path);
            envelope.put("data", data == null ? JSONObject.NULL : data);
            return envelope.toString();
        } catch (Exception e) {
            return "{\"code\":200,\"msg\":\"mock ok\",\"data\":null}";
        }
    }

    private static Object dataFor(String path) throws JSONException {
        // These callbacks only need acknowledgement. Returning an object makes
        // old Leto callbacks try to infer a generic type and throw internally.
        if (path.endsWith("/get_version_new")
                || path.endsWith("/record/game_report_up")) {
            return null;
        }
        if (path.endsWith("/app_conf")) {
            return appConfig();
        }
        if (path.endsWith("/benefits/setting")) {
            return benefitSettings();
        }
        if (path.endsWith("/wx/memcoin")) {
            return userCoin();
        }
        if (path.endsWith("/signforcoin/signlist")) {
            return signInStatus();
        }
        if (path.endsWith("/signforcoin/signstatus")) {
            return new JSONObject().put("status", 0);
        }
        if (path.endsWith("/upgrade/box_up")) {
            // Keep the installed build; this endpoint normally describes an APK update.
            return new JSONObject().put("version", "1");
        }
        if (path.endsWith("/charge/groupsbyid")) {
            // The game-center SDK wraps its list in a paging result object.
            return new JSONObject()
                    .put("gameCenterData", new JSONArray())
                    .put("has_more", false)
                    .put("data_version", 1);
        }
        if (path.endsWith("/wx/points")) {
            return new JSONObject()
                    .put("explain", "本地模式不提供提现")
                    .put("points", new JSONArray());
        }
        if (path.endsWith("/levelhb/getinfo")
                || path.endsWith("/passhb/getinfo")) {
            return new JSONArray();
        }
        if (path.endsWith("/mgc/privacy_content")) {
            return new JSONObject().put("info", "当前为本地演示环境，原测试服务器已停止服务。");
        }
        if (isListEndpoint(path)) {
            return new JSONArray();
        }
        if (path.endsWith("/sdk/add_coins")) {
            return new JSONObject()
                    .put("add_coins", 0)
                    .put("left_coins", 10033)
                    .put("total_coins", 10033);
        }
        return new JSONObject();
    }

    private static int responseCodeFor(String path) {
        // This legacy reward endpoint expects a signed/encrypted server payload.
        // It is disabled in app_conf, so report it unavailable instead of feeding
        // unsigned local JSON into the closed-source decoder.
        return path.endsWith("/inspire/yuliao") ? 410 : 200;
    }

    private static String responseMessageFor(String path) {
        return path.endsWith("/inspire/yuliao") ? "feature disabled in local mode" : "mock ok";
    }

    private static boolean isListEndpoint(String path) {
        return path.endsWith("/channel/tasklist")
                || path.endsWith("/channel/usertasklist")
                || path.endsWith("/task/lists")
                || path.endsWith("/task/user_status")
                || path.endsWith("/charge/groups")
                || path.endsWith("/charge/mygames")
                || path.endsWith("/charge/tablist")
                || path.endsWith("/benefits/playgametasklist")
                || path.endsWith("/invite/apprentices");
    }

    private static JSONObject appConfig() throws JSONException {
        return new JSONObject()
                .put("data_version", 1)
                .put("coin_gamecenter", 1)
                .put("is_showtimer", 0)
                .put("timer_time", 60)
                .put("ex_coins", 10000)
                .put("mintage", 0)
                .put("withdraw_cash", 0)
                .put("sec_coins", 0)
                .put("day_max_coins", 0)
                .put("coins_multiple", 1)
                .put("reckon_type", 1)
                .put("is_open_ad", 0)
                .put("open_ad_type", 0)
                .put("is_audit", 0)
                .put("show_exchange_btn", 0)
                .put("show_mycoins", 1)
                .put("is_show_mycoins", 1)
                .put("is_get_imei", 0)
                .put("is_getimei", 0)
                .put("is_show_privacy", 1)
                .put("open_type", 2)
                .put("is_show_hb", 0)
                .put("is_fcm", 0)
                .put("is_realname", 0)
                .put("is_show_task", 0)
                .put("is_show_titlebar", 1)
                .put("is_show_invate", 0)
                .put("is_game_center_show_invate", 0)
                .put("is_close_game", 0)
                .put("is_show_today_recommend", 0)
                .put("is_splash_showads", 0)
                .put("show_lockscreen", 0)
                .put("is_show_awaken", 0)
                .put("coin_name", "金币")
                .put("cs_wechat", "");
    }

    private static JSONObject benefitSettings() throws JSONException {
        return new JSONObject()
                .put("bubble", disabledFeature())
                .put("shake", disabledFeature())
                .put("openhb", disabledFeature())
                .put("task", disabledFeature())
                .put("newmemhb", disabledFeature())
                .put("levelhb", disabledFeature())
                .put("hbrain", disabledFeature())
                .put("playgametask", disabledFeature())
                .put("todayhb", disabledFeature())
                .put("invite", disabledFeature())
                .put("yuliao", disabledFeature())
                .put("video_task_config", new JSONArray());
    }

    private static JSONObject disabledFeature() throws JSONException {
        return new JSONObject().put("is_open", 0);
    }

    private static JSONObject userCoin() throws JSONException {
        return new JSONObject()
                .put("coins", 10033)
                .put("today_coins", 0)
                .put("history_coins", 10033)
                .put("receivable_coins", 0)
                .put("today_receivable_coin", 0)
                .put("video_max_num", 0)
                .put("video_num", 0)
                .put("live_day", 1);
    }

    private static JSONObject signInStatus() throws JSONException {
        JSONArray signs = new JSONArray();
        for (int day = 1; day <= 7; day++) {
            signs.put(new JSONObject()
                    .put("day", day)
                    .put("status", 0)
                    .put("sign_coins", day * 10)
                    .put("multiple_reward", 1)
                    .put("is_get", 0)
                    .put("is_today", day == 1 ? 1 : 0)
                    .put("coins_pic", ""));
        }
        return new JSONObject().put("coinslist", userCoin()).put("signlist", signs);
    }
}
