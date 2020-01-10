package com.mgc.letobox.happy.floattools.components.playgametask

import com.mgc.letobox.happy.model.PlayGameResult
import zlc.season.yasha.YashaDataSource
import zlc.season.yasha.YashaItem

class PlayGameDataSource(playGameResult: PlayGameResult) : YashaDataSource() {
    var playGameResult = playGameResult
    override fun loadInitial(loadCallback: LoadCallback<YashaItem>) {
        var mockData = ArrayList<GameListItem>()
        val games = playGameResult.data.games
        for(game in games){
            mockData.add(GameListItem(game.game_id,game.game_name,game.game_icon,game.game_desc,game.packagename,game.download_url))
        }

        loadCallback.setResult(mockData)
    }
}