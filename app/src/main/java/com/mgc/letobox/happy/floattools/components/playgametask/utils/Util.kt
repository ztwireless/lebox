package com.mgc.letobox.happy.floattools.components.playgametask.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.drawable.Drawable
import android.net.Uri.fromFile
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N
import android.support.v4.content.FileProvider.getUriForFile
import android.support.v4.view.ViewCompat
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mgc.leto.game.base.utils.GlideUtil
import com.mgc.letobox.happy.R
import java.io.File


fun Context.installApk(file: File) {
    val intent = Intent(ACTION_VIEW)
    val authority = "$packageName.rxdownload.demo.provider"
    val uri = if (SDK_INT >= N) {
        getUriForFile(this, authority, file)
    } else {
        fromFile(file)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(intent)
}

fun View.click(block: () -> Unit) {
    setOnClickListener {
        block()
    }
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
interface LoadStatus {
    fun onStatus(success: Boolean)
}
fun ImageView.load(url: String,loadStatus: LoadStatus = object :LoadStatus{
    override fun onStatus(success: Boolean) {
    }
}) {
    Glide.with(this).load(url).error(R.drawable.leto_mgc_game_default_pic).listener(object :RequestListener<Drawable>{
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            loadStatus?.onStatus(false);
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            loadStatus?.onStatus(true);
            return false
        }

    }).into(this)
}
fun ImageView.loadRoundedCorner(url: String,corner:Int = 13) {
    GlideUtil.loadRoundedCorner(this.context,url,this,corner,R.drawable.leto_mgc_game_default_pic)
}

fun View.background(drawable: Drawable) {
    ViewCompat.setBackground(this, drawable)
}

fun Activity.start(clazz: Class<*>) {
    startActivity(Intent(this, clazz))
}