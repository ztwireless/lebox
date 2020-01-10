package com.mgc.letobox.happy.floattools.components

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.ledong.lib.leto.mgc.bean.CoinDialogScene
import com.ledong.lib.leto.mgc.util.MGCDialogUtil
import com.leto.game.base.statistic.GameStatisticManager
import com.leto.game.base.statistic.StatisticEvent
import com.leto.game.base.util.ColorUtil
import com.leto.game.base.util.StatusBarUtil
import com.leto.game.base.util.ToastUtil
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.floattools.components.playgametask.GameListItem
import com.mgc.letobox.happy.floattools.components.playgametask.PlayGameDataSource
import com.mgc.letobox.happy.floattools.components.playgametask.utils.*
import com.mgc.letobox.happy.model.PlayGameResult
import com.mgc.letobox.happy.util.LeBoxSpUtil
import kotlinx.android.synthetic.main.activity_playgame_task.*
import kotlinx.android.synthetic.main.game_list_item.*
import kotlinx.android.synthetic.main.leto_demo_common_header.*
import zlc.season.yasha.linear


class PlayGameTaskActivity : Activity() {
    val TAG = PlayGameTaskActivity::class.java.simpleName
    lateinit var playGameResult: PlayGameResult
    lateinit var _gameId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#ffffff"))
        }
        setContentView(R.layout.activity_playgame_task)
        playGameResult = intent.getSerializableExtra("gameResult") as PlayGameResult
        _gameId = intent.getStringExtra("gameId")
        iv_back.setOnClickListener { v: View? -> finish() }
        tv_title.text = "互推模块"
        banner.load(playGameResult.data.banners, object : LoadStatus {
            override fun onStatus(success: Boolean) {
                if (success) {
                    rl_status.visible()
                } else {
                    rl_status.gone()
                }
            }
        })
        var count = LeBoxSpUtil.getInt("count")
        updateBG(count)
        iv_status.setOnClickListener { v: View? ->
            var count = LeBoxSpUtil.getInt("count")
            if (playGameResult.data != null && count > 1) {
                LeBoxSpUtil.saveInt("count", count - 2)
                updateBG(count - 2)
                MGCDialogUtil.showMGCCoinDialog(this@PlayGameTaskActivity, "", playGameResult.data.coins, playGameResult.data.coins_multiple, CoinDialogScene.PLAY_APK_GAME) { b, i -> }

                // report enter
                GameStatisticManager.statisticBenefitLog(this, _gameId, StatisticEvent.LETO_BENEFITS_PLAY_GAME_CLICK_GET_COIN.ordinal,
                        0, 0, 0, 0, CoinDialogScene.getBenefitTypeByScene(CoinDialogScene.PLAY_APK_GAME), 0)
            } else {
                ToastUtil.s(this@PlayGameTaskActivity, "试玩两款游戏，立即获得一个红包")
            }
        }
        loge("path " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
        getPackNames()
        registerInstallAppBroadcastReceiver()
        renderList()
    }

    fun updateBG(count: Int) {
//        var count = LeBoxSpUtil.getInt("count")
        if (count == 0) {
            iv_status.setBackgroundResource(R.drawable.playgame_banner_0_2)
        } else if (count == 1) {
            iv_status.setBackgroundResource(R.drawable.playgame_banner_1_2)
        } else if (count > 1) {
            iv_status.setBackgroundResource(R.drawable.playgame_banner_get)
        }
    }

    private val dataSource by lazy { PlayGameDataSource(playGameResult) }
    fun loge(msg: String) {
        Log.e("leo", "gametask  " + msg);
    }

    override fun onResume() {
        super.onResume()
        if (recycler_view.adapter != null) {
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

                    game_icon.loadRoundedCorner(data.icon)
                    btn_pause.click {
                        data.action(this@PlayGameTaskActivity, btn_pause, _gameId)
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

    private fun getGameIdyPackageName(packageName: String): Int {
        var gameId: Int = 0;

        if (playGameResult.data == null || playGameResult.data.games == null) {
            return 0;
        }

        for (game in playGameResult.data.games) {

            if (game.packagename.equals(packageName)) {
                return game.game_id;
            }
        }

        return 0;
    }

    private val mInstallAppBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null && TextUtils.equals(Intent.ACTION_PACKAGE_ADDED, intent.action)) {
                if (intent.data != null) {
                    val packageName = intent.data.schemeSpecificPart
//                    loge("安装的app的包名是-------->$packageName")
                    if (!packageNames.contains(packageName)) {
                        packageNames.add(packageName)
                    }

                    if (!TextUtils.isEmpty(LeBoxSpUtil.getString(packageName))) {
                        LeBoxSpUtil.saveInt("count", LeBoxSpUtil.getInt("count") + 1)
                        var count = LeBoxSpUtil.getInt("count")
                        updateBG(count)
                    }
                    var gameId = getGameIdyPackageName(packageName);
                    if (gameId != 0) {
                        // report enter
                        GameStatisticManager.statisticBenefitLog(context, _gameId, StatisticEvent.LETO_BENEFITS_PLAY_GAME_INSTALL_FINISH.ordinal,
                                0, 0, 0, 0, CoinDialogScene.getBenefitTypeByScene(CoinDialogScene.PLAY_APK_GAME), gameId)
                    }

                }
            }
        }
    }
    val packageNames: MutableList<String> = ArrayList()
    fun getPackNames() {
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
        fun start(activity: Activity, playGameResult: PlayGameResult, gameId: String) {
            val intent = Intent(activity, PlayGameTaskActivity::class.java)
            intent.putExtra("gameResult", playGameResult)
            intent.putExtra("gameId", gameId)
            activity.startActivity(intent)
        }
    }
}