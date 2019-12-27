package com.mgc.letobox.happy.floattools.components

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ledong.lib.leto.api.ApiContainer
import com.ledong.lib.leto.mgc.bean.CoinDialogScene
import com.ledong.lib.leto.mgc.util.MGCDialogUtil
import com.mgc.letobox.happy.R
import com.mgc.letobox.happy.floattools.components.playgametask.GameListItem
import com.mgc.letobox.happy.floattools.components.playgametask.PlayGameDataSource
import com.mgc.letobox.happy.floattools.components.playgametask.utils.click
import com.mgc.letobox.happy.floattools.components.playgametask.utils.load
import kotlinx.android.synthetic.main.activity_playgame_task.*
import kotlinx.android.synthetic.main.game_list_item.*
import kotlinx.android.synthetic.main.leto_demo_common_header.*
import zlc.season.yasha.linear

class PlayGameTaskActivity : Activity() {
    val TAG = PlayGameTaskActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playgame_task)
        iv_back.setOnClickListener { v: View? -> finish() }
        tv_title.text = "玩游戏赚红包"
        tv_status.text = "可领取"
        tv_status.setOnClickListener {v: View? ->
            MGCDialogUtil.showMGCCoinDialog(this@PlayGameTaskActivity, "", 1, 1, CoinDialogScene.PLAY_APK_GAME) { b, i -> }
        }
        renderList()
    }
    private val dataSource by lazy { PlayGameDataSource() }
    private fun renderList() {
        recycler_view.linear(dataSource) {

            renderItem<GameListItem> {
                res(R.layout.game_list_item)

                onBind {
                    game_name.text = data.name
                    game_desc.text = data.desc

                    game_icon.load(data.icon)

                    btn_pause.click {
                        data.action(containerView.context)
                    }
                }

                onAttach {
                    data.subscribe(btn_pause, containerView.context)
                }

                onDetach {
                    data.dispose()
                }
            }
        }
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, PlayGameTaskActivity::class.java)
            activity.startActivity(intent)
        }
    }
}