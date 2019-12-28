package com.mgc.letobox.happy.floattools.tools

import android.app.Activity
import android.util.Log
import com.ledong.lib.leto.LetoConst
import com.leto.game.base.login.LoginManager
import com.leto.game.base.util.BaseAppUtil
import com.mgc.letobox.happy.floattools.BaseFloatTool
import com.mgc.letobox.happy.floattools.FloatViewManager
import com.mgc.letobox.happy.floattools.MGCService
import com.mgc.letobox.happy.floattools.components.PlayGameTaskActivity
import com.mgc.letobox.happy.model.FloatToolsConfig.Data.Playgametask
import com.mgc.letobox.happy.model.PlayGameResult
import com.mgc.letobox.happy.model.PlayGameResult.Data
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
//            val todayTimes = LeBoxSpUtil.todayShakeTimes(gameId)
//            if (System.currentTimeMillis() - lastShakeTime < 600) {
//                // do nothing
//            } else if (todayTimes >= palygameConfig.max_times) {
//                lastShakeTime = System.currentTimeMillis()
//                Toast.makeText(activity, R.string.shake_time_used_out, Toast.LENGTH_SHORT).show()
//            } else {
//                lastShakeTime = System.currentTimeMillis()
//                LeBoxSpUtil.shakeOnce(gameId)
                Executors.newSingleThreadExecutor().submit { playGameIt(activity) }
//            }
        }
    }

    private fun playGameIt(activity: Activity) {
        PlayGameTaskActivity.start(activity)
        val service: MGCService = buildRetrofit().create(MGCService::class.java)
        val playGameResultCall: Call<PlayGameResult> = service.obtainPlayGameResult(toInt(BaseAppUtil.getChannelID(activity)),LoginManager.getUserId(activity), LetoConst.SDK_OPEN_TOKEN)
        try {
            val playgameResultResponse: Response<PlayGameResult>? = playGameResultCall.execute()
            if (playgameResultResponse != null) {
                Log.e("dongxt", "======run:1== " + playgameResultResponse.message())
                Log.e("dongxt", "======run:2== " + playgameResultResponse.raw().toString())
                val playgameResult: PlayGameResult? = playgameResultResponse.body()
                activity.runOnUiThread {
                    val playgameData: Data? = playgameResult?.data
//                    if (shakeData == null || shakeData.add_coins == 0) {
//                        val apiContainer = ApiContainer(activity)
//                        apiContainer.presentInterstitialAd(object : IApiResultListener {
//                            override fun onApiSuccess(apiName: ApiName?, o: Any?) {
//                                Log.i(TAG, "onApiSuccess")
//                                apiContainer.destroy()
//                            }
//
//                            override fun onApiFailed(apiName: ApiName?, b: Boolean) {
//                                Log.i(TAG, "onApiFailed")
//                                apiContainer.destroy()
//                                Toast.makeText(activity, R.string.obtain_ad_failed, Toast.LENGTH_SHORT).show()
//                            }
//                        })
//                    } else {
//                        MGCDialogUtil.showMGCCoinDialog(activity, "", shakeData.add_coins, shakeData.add_coins_multiple, CoinDialogScene.SHAKE) { b, i -> }
//                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}