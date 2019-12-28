package com.mgc.letobox.happy.floattools.skymodels

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.Log
import com.mgc.letobox.happy.floattools.drawables.Model

class RedPacketModel(val redPacketDrawable: Drawable, val rotation: Float, val verticalSpeed: Float, position: PointF) : Model(position) {
    companion object {
        val TAG = RedPacketModel::class.java.simpleName
    }
    init {
        Log.i(TAG, "width height ${redPacketDrawable.intrinsicWidth} ${redPacketDrawable.intrinsicHeight}")
        redPacketDrawable.setBounds(0, 0, redPacketDrawable.intrinsicWidth, redPacketDrawable.intrinsicHeight)
        updateBounds()
    }
    override fun move(delta: Int) {
        position.y += verticalSpeed * delta / 1000f
        updateBounds()
    }
    fun updateBounds() {
        touchBounds.set(position.x, position.y, position.x + redPacketDrawable.intrinsicWidth, position.y + redPacketDrawable.intrinsicHeight)
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.translate(position.x, position.y)
        canvas.rotate(rotation)
        redPacketDrawable.draw(canvas)
        canvas.restore()
    }
}