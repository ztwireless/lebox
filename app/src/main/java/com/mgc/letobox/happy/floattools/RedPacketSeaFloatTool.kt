package com.mgc.letobox.happy.floattools

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.ledong.lib.leto.api.ApiContainer
import com.ledong.lib.leto.api.ApiContainer.ApiName
import com.ledong.lib.leto.api.ApiContainer.IApiResultListener
import com.mgc.letobox.happy.model.FloatToolsConfig
import com.mgc.letobox.happy.view.FloatRedPacketSea

class RedPacketSeaFloatTool(activity: Activity, gameId: String, val shakeConfig: FloatToolsConfig.Data.Shake) : BaseFloatTool(activity, gameId) {
    private val TAG = RedPacketSeaFloatTool::class.java.simpleName

    override fun isGameEnabled(): Boolean {
        return true
    }

    override fun init() {
        if (wrActivity.get() == null) return

        val redPacketSea: FloatRedPacketSea = FloatViewManager.getInstance().showRedPacket(wrActivity.get())
        redPacketSea.setOnClickListener {
            if (wrActivity.get() == null) return@setOnClickListener
            val activity = wrActivity.get()
            val apiContainer = ApiContainer(activity)
            apiContainer.showVideo(object : IApiResultListener {
                override fun onApiSuccess(apiName: ApiName?, o: Any?) {
                    if (wrActivity.get() == null) return

                    Log.i(TAG, "onApiSuccess")
                    val intent = Intent(activity, RedPacketSeaActivity::class.java)
                    wrActivity.get()?.startActivity(intent)
                    apiContainer.destroy()
                }

                override fun onApiFailed(apiName: ApiName?, b: Boolean) {
                    Log.i(TAG, "onApiFailed")
                    apiContainer.destroy()
                }
            })
        }
    }

    override fun clean() {
        super.clean()
        if (wrActivity.get() != null) {
            FloatViewManager.getInstance().removeRedPacketView(wrActivity.get())
        }
    }
}