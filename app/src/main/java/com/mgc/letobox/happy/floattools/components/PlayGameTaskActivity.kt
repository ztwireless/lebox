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
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import com.mgc.leto.game.base.mgc.bean.CoinDialogScene
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil
import com.mgc.leto.game.base.statistic.GameStatisticManager
import com.mgc.leto.game.base.statistic.StatisticEvent
import com.mgc.leto.game.base.utils.ColorUtil
import com.mgc.leto.game.base.utils.StatusBarUtil
import com.mgc.leto.game.base.utils.ToastUtil
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.floattools.components.playgametask.GameListItem
import com.mgc.letobox.happy.floattools.components.playgametask.PlayGameDataSource
import com.mgc.letobox.happy.floattools.components.playgametask.utils.*
import com.mgc.letobox.happy.model.PlayGameResult
import com.mgc.letobox.happy.util.LeBoxSpUtil
import zlc.season.yasha.linear


class PlayGameTaskActivity : Activity() {
    val TAG = PlayGameTaskActivity::class.java.simpleName
    lateinit var playGameResult: PlayGameResult
    lateinit var _gameId: String
    private lateinit var backButton: ImageView
    private lateinit var titleView: TextView
    private lateinit var bannerView: ImageView
    private lateinit var statusContainer: RelativeLayout
    private lateinit var statusView: ImageView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#ffffff"))
        }
        setContentView(R.layout.activity_playgame_task)
        backButton = findViewById(R.id.iv_back)
        titleView = findViewById(R.id.tv_title)
        bannerView = findViewById(R.id.banner)
        statusContainer = findViewById(R.id.rl_status)
        statusView = findViewById(R.id.iv_status)
        recyclerView = findViewById(R.id.recycler_view)
        playGameResult = intent.getSerializableExtra("gameResult") as PlayGameResult
        _gameId = intent.getStringExtra("gameId") ?: ""
        backButton.setOnClickListener { _: View? -> finish() }
        titleView.text = "互推模块"
        bannerView.load(playGameResult.data.banners, object : LoadStatus {
            override fun onStatus(success: Boolean) {
                if (success) {
                    statusContainer.visible()
                } else {
                    statusContainer.gone()
                }
            }
        })
        var count = LeBoxSpUtil.getInt("count")
        updateBG(count)
        statusView.setOnClickListener { _: View? ->
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
            statusView.setBackgroundResource(R.drawable.playgame_banner_0_2)
        } else if (count == 1) {
            statusView.setBackgroundResource(R.drawable.playgame_banner_1_2)
        } else if (count > 1) {
            statusView.setBackgroundResource(R.drawable.playgame_banner_get)
        }
    }

    private val dataSource by lazy { PlayGameDataSource(playGameResult) }
    fun loge(msg: String) {
        Log.e("leo", "gametask  " + msg);
    }

    override fun onResume() {
        super.onResume()
        if (recyclerView.adapter != null) {
            recyclerView.adapter!!.notifyDataSetChanged()
        }
    }

    private fun renderList() {
        recyclerView.linear(dataSource) {

            renderItem<GameListItem> {
                res(R.layout.game_list_item)

                onBind {
                    val gameName = containerView.findViewById<TextView>(R.id.game_name)
                    val gameDesc = containerView.findViewById<TextView>(R.id.game_desc)
                    val gameIcon = containerView.findViewById<ImageView>(R.id.game_icon)
                    val actionButton = containerView.findViewById<DownloadProgressButton>(R.id.btn_pause)
                    gameName.text = data.name
                    gameDesc.text = data.desc

                    gameIcon.loadRoundedCorner(data.icon)
                    actionButton.click {
                        data.action(this@PlayGameTaskActivity, actionButton, _gameId)
                    }
                }

                onAttach {
                    val actionButton = containerView.findViewById<DownloadProgressButton>(R.id.btn_pause)
                    data.setPacknames(packageNames)
                    data.subscribe(actionButton, this@PlayGameTaskActivity)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(mInstallAppBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(mInstallAppBroadcastReceiver, intentFilter)
        }
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
                    val packageName = intent.data!!.schemeSpecificPart
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
