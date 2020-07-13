package com.mgc.letobox.happy.floattools.tools

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.mgc.leto.game.base.LetoConst
import com.mgc.leto.game.base.api.constant.Constant
import com.mgc.leto.game.base.login.LoginManager
import com.mgc.leto.game.base.mgc.bean.BenefitSettings_playgametask
import com.mgc.leto.game.base.statistic.GameStatisticManager
import com.mgc.leto.game.base.statistic.StatisticEvent
import com.mgc.leto.game.base.utils.BaseAppUtil
import com.mgc.leto.game.base.utils.ToastUtil
import com.mgc.letobox.happy.floattools.BaseFloatTool
import com.mgc.letobox.happy.floattools.FloatViewManager
import com.mgc.letobox.happy.floattools.MGCService
import com.mgc.letobox.happy.floattools.components.PlayGameTaskActivity
import com.mgc.letobox.happy.model.PlayGameResult
import com.mgc.letobox.happy.util.LeBoxSpUtil
import com.mgc.letobox.happy.view.PlayGameView
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executors

class PlayGameFloatTool(activity: Activity, gameId: String, val palygameConfig: BenefitSettings_playgametask) : BaseFloatTool(activity, gameId) {
    private val TAG = PlayGameFloatTool::class.java.simpleName

    override fun isGameEnabled(): Boolean {
        if (TEST_ENV) return true
        if (palygameConfig != null && palygameConfig.is_open == 1 && palygameConfig.game_ids != null) {
            return palygameConfig.game_ids.contains(gameId)
        }
        return false
    }

    override fun init() {
        Log.i(TAG, "init")
        if (wrActivity.get() != null) {
            initPlayGameView(wrActivity.get()!!)
        }
    }

    override fun show(activity: Activity) {
        FloatViewManager.getInstance().showPlayGameView(activity, palygameConfig.default_x, palygameConfig.default_y)
    }

    override fun clean() {
        super.clean()
        FloatViewManager.getInstance().removePlayGameView(wrActivity.get())
    }

    private var lastShakeTime: Long = 0

    private fun initPlayGameView(activity: Activity) {
        val playGameCofig = palygameConfig
        val palygameview: PlayGameView = FloatViewManager.getInstance().getPlayGameView(activity, palygameConfig.default_x, palygameConfig.default_y)
        lastShakeTime = System.currentTimeMillis()
        palygameview.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastShakeTime < 600) {
                // do nothing
            } else {
                //点击上报
                GameStatisticManager.statisticBenefitLog(activity, gameId, StatisticEvent.LETO_BENEFITS_ENTER_CLICK.ordinal, 0, 0, 0, 0, Constant.BENEFITS_TYPE_PLAY_GAME_TASK, 0)

                lastShakeTime = System.currentTimeMillis()
                Executors.newSingleThreadExecutor().submit { playGameIt(activity) }
            }
        }
    }

    private fun playGameIt(activity: Activity) {
        var isEnter = false;
        try {
            var resultJson = LeBoxSpUtil.getString(LoginManager.getUserId(activity));
            if (!TextUtils.isEmpty(resultJson)) {
                val playgameResult: PlayGameResult? = Gson().fromJson(resultJson, PlayGameResult::class.java)
                if (playgameResult != null) {
                    isEnter = true
                    PlayGameTaskActivity.start(activity, playgameResult, gameId)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isEnter = false
        }
        val service: MGCService = buildRetrofit().create(MGCService::class.java)
        val playGameResultCall: Call<PlayGameResult> = service.obtainPlayGameResult(toInt(BaseAppUtil.getChannelID(activity)), LoginManager.getUserId(activity), LetoConst.SDK_OPEN_TOKEN)
        try {
            val playgameResultResponse: Response<PlayGameResult>? = playGameResultCall.execute()
            if (playgameResultResponse != null) {
                val playgameResult: PlayGameResult? = playgameResultResponse.body()
                val json = Gson().toJson(playgameResult)
                LeBoxSpUtil.saveString(LoginManager.getUserId(activity), json);
                if (!isEnter) {
                    activity?.runOnUiThread {
                        if (playgameResult == null) {
                            ToastUtil.s(activity, "获取数据失败")
                            return@runOnUiThread
                        }
                        PlayGameTaskActivity.start(activity, playgameResult, gameId)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}