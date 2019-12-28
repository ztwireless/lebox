package com.mgc.letobox.happy.floattools.tools

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.ledong.lib.leto.LetoConst
import com.leto.game.base.login.LoginManager
import com.leto.game.base.util.BaseAppUtil
import com.mgc.letobox.happy.floattools.BaseFloatTool
import com.mgc.letobox.happy.floattools.FloatViewManager
import com.mgc.letobox.happy.floattools.MGCService
import com.mgc.letobox.happy.floattools.components.PlayGameTaskActivity
import com.mgc.letobox.happy.model.FloatToolsConfig.Data.Playgametask
import com.mgc.letobox.happy.model.PlayGameResult
import com.mgc.letobox.happy.view.PlayGameView
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executors

class PlayGameFloatTool(activity: Activity, gameId: String, val palygameConfig: Playgametask) : BaseFloatTool(activity, gameId) {
    private val TAG = PlayGameFloatTool::class.java.simpleName

    override fun isGameEnabled(): Boolean {
        if (TEST_ENV) return true
        val gameIdInt = toInt(gameId)
        if (palygameConfig.is_open == 1 && palygameConfig.game_ids != null) {
            return palygameConfig.game_ids.contains(gameIdInt)
        }
        return false
    }

    override fun init() {
        Log.i(TAG, "init")
        if (wrActivity.get() != null) {
            initPlayGameView(wrActivity.get()!!)
        }
    }

    override fun clean() {
        super.clean()
        FloatViewManager.getInstance().removePlayGameView(wrActivity.get())
    }

    private var lastShakeTime: Long = 0

    private fun initPlayGameView(activity: Activity) {
        val playGameCofig = palygameConfig
        val palygameview: PlayGameView = FloatViewManager.getInstance().showPlayGameView(activity, palygameConfig.default_x, palygameConfig.default_y)
        lastShakeTime = System.currentTimeMillis()
        palygameview.setOnClickListener {
                Executors.newSingleThreadExecutor().submit { playGameIt(activity) }
        }
    }

    private fun playGameIt(activity: Activity) {
        val service: MGCService = buildRetrofit().create(MGCService::class.java)
        val playGameResultCall: Call<PlayGameResult> = service.obtainPlayGameResult(toInt(BaseAppUtil.getChannelID(activity)),LoginManager.getUserId(activity), LetoConst.SDK_OPEN_TOKEN)
        try {
            val playgameResultResponse: Response<PlayGameResult>? = playGameResultCall.execute()
            if (playgameResultResponse != null) {
                val playgameResult: PlayGameResult? = playgameResultResponse.body()
                activity.runOnUiThread {
                    if(playgameResult == null){
                        Toast.makeText(activity, "获取数据失败", Toast.LENGTH_SHORT).show()
                        return@runOnUiThread
                    }
                    PlayGameTaskActivity.start(activity,playgameResult)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}