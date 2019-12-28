package com.mgc.letobox.happy.floattools.components

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.ledong.lib.leto.mgc.bean.CoinDialogScene
import com.ledong.lib.leto.mgc.util.MGCDialogUtil
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.floattools.components.playgametask.GameListItem
import com.mgc.letobox.happy.floattools.components.playgametask.PlayGameDataSource
import com.mgc.letobox.happy.floattools.components.playgametask.utils.click
import com.mgc.letobox.happy.floattools.components.playgametask.utils.load
import com.mgc.letobox.happy.model.PlayGameResult
import com.mgc.letobox.happy.util.LeBoxSpUtil
import kotlinx.android.synthetic.main.activity_playgame_task.*
import kotlinx.android.synthetic.main.game_list_item.*
import kotlinx.android.synthetic.main.leto_demo_common_header.*
import zlc.season.yasha.linear


class PlayGameTaskActivity : Activity() {
    val TAG = PlayGameTaskActivity::class.java.simpleName
    lateinit var playGameResult: PlayGameResult
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playgame_task)
        playGameResult = intent.getSerializableExtra("gameResult") as PlayGameResult
        iv_back.setOnClickListener { v: View? -> finish() }
        tv_title.text = "玩游戏赚红包"
        banner.load(playGameResult.data.banners)
        var count = LeBoxSpUtil.getInt("count")
        updateText(count)
        tv_status.setOnClickListener {v: View? ->
            if(playGameResult.data != null && count > 1){
                var count = LeBoxSpUtil.getInt("count")
                LeBoxSpUtil.saveInt("count",count - 2)
                updateText(count - 2)
                MGCDialogUtil.showMGCCoinDialog(this@PlayGameTaskActivity, "", playGameResult.data.coins, playGameResult.data.coins_multiple, CoinDialogScene.PLAY_APK_GAME) { b, i -> }
            }
        }
        loge("path "+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
        getPackNames()
        registerInstallAppBroadcastReceiver()
        renderList()
    }
    fun updateText(count : Int){
//        var count = LeBoxSpUtil.getInt("count")
        if(count > 1){
            tv_status.text = "可领取"
        }else{
            tv_status.text = String.format("下载(%d/2)",count)
        }
    }
    private val dataSource by lazy { PlayGameDataSource(playGameResult) }
    fun loge(msg:String){
        Log.e("dongxt","gametask  "+msg);
    }

    override fun onResume() {
        super.onResume()
        if(recycler_view.adapter != null){
            recycler_view.adapter.notifyDataSetChanged()
        }
    }
    private fun renderList() {
        recycler_view.linear(dataSource) {

            renderItem<GameListItem> {
                res(R.layout.game_list_item)

                onBind {
                    game_name.text = data.name
                    game_desc.text = data.desc

                    game_icon.load(data.icon)
                    btn_pause.click {
                        data.action(this@PlayGameTaskActivity,btn_pause)
                    }
                }

                onAttach {
                    data.setPacknames(packageNames)
                    data.subscribe(btn_pause, this@PlayGameTaskActivity)
                }

                onDetach {
                    data.dispose()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mInstallAppBroadcastReceiver)
    }
    private fun registerInstallAppBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addDataScheme("package")
        registerReceiver(mInstallAppBroadcastReceiver, intentFilter)
    }

    private val mInstallAppBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && TextUtils.equals(Intent.ACTION_PACKAGE_ADDED, intent.action)) {
                if (intent.data != null) {
                    val packageName = intent.data.schemeSpecificPart
//                    loge("安装的app的包名是-------->$packageName")
                    if(!packageNames.contains(packageName)) {
                        packageNames.add(packageName)
                    }

                    if(!TextUtils.isEmpty(LeBoxSpUtil.getString(packageName))){
                        LeBoxSpUtil.saveInt("count",LeBoxSpUtil.getInt("count")+1)
                        var count = LeBoxSpUtil.getInt("count")
                        updateText(count)
                    }
                }
            }
        }
    }
    val packageNames: MutableList<String> = ArrayList()
    fun getPackNames(){
        val packageManager = packageManager
        val packageInfos = packageManager.getInstalledPackages(0)
        if (packageInfos != null) {
            for (i in packageInfos.indices) {
                val packName = packageInfos[i].packageName
//                loge("已经安装的app的包名是-------->$packName")
                packageNames.add(packName)
            }
        }
    }

    companion object {
        fun start(activity: Activity,playGameResult: PlayGameResult) {
            val intent = Intent(activity, PlayGameTaskActivity::class.java)
            intent.putExtra("gameResult",playGameResult)
            activity.startActivity(intent)
        }
    }
}