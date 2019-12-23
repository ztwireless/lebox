package com.mgc.letobox.happy.floattools.tools

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.ledong.lib.leto.LetoConst
import com.ledong.lib.leto.api.ApiContainer
import com.ledong.lib.leto.api.ApiContainer.ApiName
import com.ledong.lib.leto.api.ApiContainer.IApiResultListener
import com.ledong.lib.leto.mgc.bean.CoinDialogScene
import com.ledong.lib.leto.mgc.util.MGCDialogUtil
import com.leto.game.base.login.LoginManager
import com.leto.game.base.util.BaseAppUtil
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.floattools.BaseFloatTool
import com.mgc.letobox.happy.floattools.FloatViewManager
import com.mgc.letobox.happy.floattools.MGCService
import com.mgc.letobox.happy.model.FloatToolsConfig.Data.Shake
import com.mgc.letobox.happy.model.ShakeResult
import com.mgc.letobox.happy.model.ShakeResult.Data
import com.mgc.letobox.happy.util.LeBoxSpUtil
import com.mgc.letobox.happy.view.ShakeShakeView
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executors

class ShakeFloatTool(activity: Activity, gameId: String, val shakeConfig: Shake) : BaseFloatTool(activity, gameId) {
    private val TAG = ShakeFloatTool::class.java.simpleName

    override fun isGameEnabled(): Boolean {
        if (TEST_ENV) return true
        val gameIdInt = toInt(gameId)
        if (shakeConfig.is_open == 1 && shakeConfig.game_ids != null) {
            return shakeConfig.game_ids.contains(gameIdInt)
        }
        return false
    }

    override fun init() {
        Log.i(TAG, "init")
        if (wrActivity.get() != null) {
            initShakeView(wrActivity.get()!!)
        }
    }

    override fun clean() {
        super.clean()
        FloatViewManager.getInstance().removeShakeView(wrActivity.get())
    }

    private var lastShakeTime: Long = 0

    private fun initShakeView(activity: Activity) {
        val shake = shakeConfig
        val shakeView: ShakeShakeView = FloatViewManager.getInstance().showShakeShake(activity, shake.default_x, shake.default_y)
        lastShakeTime = System.currentTimeMillis()
        shakeView.setOnClickListener {
            val todayTimes = LeBoxSpUtil.todayShakeTimes(gameId)
            if (System.currentTimeMillis() - lastShakeTime < 600) {
                // do nothing
            } else if (todayTimes >= shake.max_times) {
                lastShakeTime = System.currentTimeMillis()
                Toast.makeText(activity, R.string.shake_time_used_out, Toast.LENGTH_SHORT).show()
            } else {
                lastShakeTime = System.currentTimeMillis()
                LeBoxSpUtil.shakeOnce(gameId)
                Executors.newSingleThreadExecutor().submit { shakeIt(activity) }
            }
        }
    }

    private fun shakeIt(activity: Activity) {
        val service: MGCService = buildRetrofit().create(MGCService::class.java)
        val shakeResultCall: Call<ShakeResult> = service.obtainShakeResult(toInt(BaseAppUtil.getChannelID(activity)), toInt(gameId), LoginManager.getUserId(activity), LetoConst.SDK_OPEN_TOKEN)
        try {
            val shakeResultResponse: Response<ShakeResult>? = shakeResultCall.execute()
            if (shakeResultResponse != null) {
                val shakeResult: ShakeResult = shakeResultResponse.body()
                activity.runOnUiThread {
                    val shakeData: Data? = shakeResult.data
                    if (shakeData == null || shakeData.add_coins == 0) {
                        val apiContainer = ApiContainer(activity)
                        apiContainer.presentInterstitialAd(object : IApiResultListener {
                            override fun onApiSuccess(apiName: ApiName?, o: Any?) {
                                Log.i(TAG, "onApiSuccess")
                                apiContainer.destroy()
                            }

                            override fun onApiFailed(apiName: ApiName?, b: Boolean) {
                                Log.i(TAG, "onApiFailed")
                                apiContainer.destroy()
                                Toast.makeText(activity, R.string.obtain_ad_failed, Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        MGCDialogUtil.showMGCCoinDialog(activity, "", shakeData.add_coins, shakeData.add_coins_multiple, CoinDialogScene.SHAKE) { b, i -> }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}