package com.mgc.letobox.happy.floattools.components.playgametask

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.ledong.lib.leto.mgc.bean.CoinDialogScene
import com.leto.game.base.statistic.GameStatisticManager
import com.leto.game.base.statistic.StatisticEvent
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.floattools.components.playgametask.utils.DownloadProgressButton
import com.mgc.letobox.happy.floattools.components.playgametask.utils.DownloadProgressButton.*
import com.mgc.letobox.happy.floattools.components.playgametask.utils.DownloadUtil
import com.mgc.letobox.happy.floattools.components.playgametask.utils.installApk
import com.mgc.letobox.happy.util.LeBoxSpUtil
import zlc.season.yasha.YashaItem
import java.io.File


class GameListItem(
        val id: Int,
        val name: String,
        val icon: String,
        val desc: String,
        val packName: String,
        val url: String
) : YashaItem {

    private var tag: Any? = null
    fun loge(msg:String){
        Log.e("leo","gameitem  "+msg);
    }

    fun action(context: Activity,btn_action: DownloadProgressButton, gameId: String) {
        context?.runOnUiThread {


            when(btn_action.state){
                STATE_NORMAL ->{
                    btn_action.setCurrentText(context.getString(R.string.start_text))
                    download(context,btn_action, gameId)

                    // report enter
                    GameStatisticManager.statisticBenefitLog(context, gameId, StatisticEvent.LETO_BENEFITS_PLAY_GAME_CLICK_DOWNLOAD.ordinal,
                            0, 0, 0, 0, CoinDialogScene.getBenefitTypeByScene(CoinDialogScene.PLAY_APK_GAME), id)
                }
                STATE_DOWNLOADING ->{
                    btn_action.setProgressText("",btn_action.progress)
                }
                STATE_FINISH ->{
                    LeBoxSpUtil.saveString(packName,packName)

                    context.installApk(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path, packName))

                    // report enter
                    GameStatisticManager.statisticBenefitLog(context, gameId, StatisticEvent.LETO_BENEFITS_PLAY_GAME_INSTALL.ordinal,
                            0, 0, 0, 0, CoinDialogScene.getBenefitTypeByScene(CoinDialogScene.PLAY_APK_GAME), id)
                }
                STATE_FAILED->{
                    btn_action.setCurrentText(context.getString(R.string.retry_text))
                    download(context,btn_action, gameId)

                    // report enter
                    GameStatisticManager.statisticBenefitLog(context, gameId, StatisticEvent.LETO_BENEFITS_PLAY_GAME_CLICK_DOWNLOAD.ordinal,
                            0, 0, 0, 0, CoinDialogScene.getBenefitTypeByScene(CoinDialogScene.PLAY_APK_GAME), id)
                }
                else ->{
                    btn_action.setCurrentText(context.getString(R.string.open_text))

                    val packageManager: PackageManager = context.getPackageManager()
                    var intent  = packageManager.getLaunchIntentForPackage(packName)
                    if (intent == null) {
                        download(context,btn_action, gameId)

                        // report enter
                        GameStatisticManager.statisticBenefitLog(context, gameId, StatisticEvent.LETO_BENEFITS_PLAY_GAME_CLICK_DOWNLOAD.ordinal,
                                0, 0, 0, 0, CoinDialogScene.getBenefitTypeByScene(CoinDialogScene.PLAY_APK_GAME), id)

                    } else {
                        context.startActivity(intent)

                        // report enter
                        GameStatisticManager.statisticBenefitLog(context, gameId, StatisticEvent.LETO_BENEFITS_PLAY_GAME_OPEN_APP.ordinal,
                                0, 0, 0, 0, CoinDialogScene.getBenefitTypeByScene(CoinDialogScene.PLAY_APK_GAME), id)
                    }
                }
            }
        }


    }
    fun download(context: Activity,btn_action: DownloadProgressButton, gameId: String){
        DownloadUtil().download(url,packName,object :DownloadUtil.OnDownloadListener{
            override fun onDownloading(progress: Int) {
                context?.runOnUiThread {
                    btn_action.state = STATE_DOWNLOADING
                    btn_action.setProgressText("",progress.toFloat())
                }
            }

            override fun onDownloadFailed() {
                loge("onDownloadFailed")
                context?.runOnUiThread {
                    btn_action.state = STATE_FAILED
                    btn_action.setCurrentText(context.getString(R.string.retry_text))
                }
            }

            override fun onDownloadSuccess(file: File) {
                loge("onDownloadSuccess " +packName)
                context?.runOnUiThread {
                    btn_action.state = STATE_FINISH
                    btn_action.setCurrentText(context.getString(R.string.install_text))
                    LeBoxSpUtil.saveString(packName,packName)

                    // report enter
                    GameStatisticManager.statisticBenefitLog(context, gameId, StatisticEvent.LETO_BENEFITS_PLAY_GAME_DOWNLOAD_FINISH.ordinal,
                            0, 0, 0, 0, CoinDialogScene.getBenefitTypeByScene(CoinDialogScene.PLAY_APK_GAME), id)

                    context.installApk(file)

                    GameStatisticManager.statisticBenefitLog(context, gameId, StatisticEvent.LETO_BENEFITS_PLAY_GAME_INSTALL.ordinal,
                            0, 0, 0, 0, CoinDialogScene.getBenefitTypeByScene(CoinDialogScene.PLAY_APK_GAME), id)
                }
            }

        })
    }
    fun isExist(name: String):Boolean{
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path, name)
        if(file.exists()){
            return true
        }
        return false
    }
    fun subscribe(btn_action: DownloadProgressButton, context: Activity) {
//        loge("subscribe "+name +" status "+btn_action.state+"/ "+packName)
        context?.runOnUiThread {
            if(packageNames.contains(packName) && !TextUtils.isEmpty(LeBoxSpUtil.getString(packName))){
                btn_action.state = STATE_OPEN
                btn_action.setCurrentText(context.getString(R.string.open_text))
                return@runOnUiThread
            }
            if(isExist(packName)){
                btn_action.state = STATE_FINISH
                btn_action.setCurrentText(context.getString(R.string.install_text))
            } else {
                //修正Button状态
                btn_action.setState(STATE_NORMAL);
            }
            when(btn_action.state){
                STATE_NORMAL ->{
                    btn_action.setCurrentText(context.getString(R.string.start_text))
                }
                STATE_DOWNLOADING ->{
                    btn_action.setProgressText("",btn_action.progress)
                }
                STATE_PAUSE ->{
                    btn_action.setCurrentText(context.getString(R.string.pause_text))
                }
                STATE_FINISH ->{
                    btn_action.setCurrentText(context.getString(R.string.install_text))
                }
                STATE_FAILED->{
                    btn_action.setCurrentText(context.getString(R.string.retry_text))
                }
                else ->{
                    btn_action.setCurrentText(context.getString(R.string.open_text))
                }
            }
        }
    }
    lateinit var  packageNames: MutableList<String>
    fun setPacknames(packageNames: MutableList<String>){
        this.packageNames = packageNames
    }


    fun dispose() {
        tag?.let {
        }
    }

    override fun cleanUp() {
        dispose()
    }
}