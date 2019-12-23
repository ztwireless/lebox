package com.mgc.letobox.happy.floattools.skymodels

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.mgc.letobox.happy.floattools.drawables.Model
import com.mgc.letobox.happy.floattools.drawables.TitanAnimationDrawable

class AnimDrawableModel(val drawable: TitanAnimationDrawable, position: PointF) : Model(position), Drawable.Callback, Animatable {
    override fun isRunning(): Boolean {
        return drawable.isRunning
    }

    override fun stop() {
        if (drawable.isRunning) drawable.stop()
    }

    init {
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.callback = this
    }

    override fun move(delta: Int) {
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.translate(position.x, position.y)
        drawable.draw(canvas)
        canvas.restore()
    }

    override fun start() {
        drawable.setAnimationListener {
            onAnimFinished()
        }
        drawable.start()
    }

    var onAnimFinishedListener: (() -> Unit)? = null
    fun onAnimFinished() {
        onAnimFinishedListener?.let {
            it()
        }
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
        handler.removeCallbacks(what)
    }

    override fun invalidateDrawable(who: Drawable) {
    }

    val handler = Handler(Looper.getMainLooper())
    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
        val delay = `when` - SystemClock.uptimeMillis()
        handler.postDelayed(what, delay)
    }
}