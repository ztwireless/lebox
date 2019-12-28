package com.mgc.letobox.happy.floattools.components.playgametask

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mgc.letobox.happy.floattools.components.playgametask.utils.mock_json
import zlc.season.yasha.YashaDataSource
import zlc.season.yasha.YashaItem

class PlayGameDataSource : YashaDataSource() {
    override fun loadInitial(loadCallback: LoadCallback<YashaItem>) {
        val type = object : TypeToken<List<GameListItem>>() {}.type
        val mockData = Gson().fromJson<List<GameListItem>>(mock_json, type)
        loadCallback.setResult(mockData)
    }
}