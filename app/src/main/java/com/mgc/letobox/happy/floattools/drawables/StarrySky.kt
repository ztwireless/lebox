package com.mgc.letobox.happy.floattools.drawables

import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
//import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.math.cos
import kotlin.math.sin

/*

class StarrySky(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    constructor(context: Context): this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet): this(context, attrs, 0)


}*/
class StarrySky(val width: Int, val height: Int) : Drawable(), Animatable {
    val models = HashSet<Model>()
    var backgroundColor: Int = Color.TRANSPARENT

    private val LOCK = Any()
    fun addModel(model: Model) {
        synchronized(LOCK) {
            models.add(model)
        }
    }

    fun removeModel(model: Model) {
        synchronized(LOCK) {
            models.remove(model)
        }
    }

    fun copyModel(): HashSet<Model> {
        synchronized(LOCK) {
            val set = HashSet<Model>()
            set.addAll(models)
            return set
        }
    }

    fun addRandomStar() {
        addModel(Star(
                PointF(random(0, width), random(0, height)),
                random(0, MAX_SPEED).toInt(),
                random(0, 360).toInt(),
                Paint().apply {
                    color = Color.parseColor("#ADFFC107")
                }
        ))
    }

    private var onModelOutListener: ((model: Model) -> Unit)? = null
    fun setOnModelOutListener(listener: ((model: Model) -> Unit)) {
        onModelOutListener = listener
    }

    companion object {
        const val MAX_SPEED = 40
        fun random(start: Float, end: Float): Float {
            return (Math.random() * (end - start) + start).toFloat()
        }

        fun random(start: Int, end: Int): Float {
            return (Math.random() * (end - start) + start).toFloat()
        }
    }

    override fun draw(canvas: Canvas) {
//        Timber.i("draw ${stars.size}")
        canvas.drawColor(backgroundColor)
        val currentModels = copyModel()
        for (model in currentModels) {
            model.draw(canvas)
        }
    }

    override fun setAlpha(alpha: Int) {}

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {}
    override fun getIntrinsicWidth(): Int {
        return width
    }

    override fun getIntrinsicHeight(): Int {
        return height
    }

    override fun isRunning(): Boolean {
        return isRunning
    }

    fun update(delta: Int) {
        val outSiteList = ArrayList<Model>()
        val currentModels = copyModel()
        for (model in currentModels) {
            model.move(delta)
            if (isOutSite(model)) {
                outSiteList.add(model)
            }
        }
        for (model in outSiteList) {
            onModelOutListener?.let {
                it(model)
            }
        }
        invalidateSelf()
    }

    fun onTouch(x: Float, y: Float) {
        val models = copyModel()
        for (model in models.reversed()) {
            if (model.touchBounds.contains(x, y)) {
                model.onTouch(x, y)
                break
            }
        }
    }

    private var timer = Timer()
    private var isRunning = false
    private fun isOutSite(model: Model): Boolean {
        return model.position.x < 0 || model.position.x > width || model.position.y < 0 || model.position.y > height
    }

    private var lastTime = 0L
    val handler = Handler(Looper.getMainLooper())
    override fun start() {
        isRunning = true
        lastTime = System.currentTimeMillis()
        update(0)

        timer.cancel()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    val currentTime = System.currentTimeMillis()
                    update((currentTime - lastTime).toInt())
                    lastTime = currentTime
                }
            }
        }, 16, 16)
    }

    override fun stop() {
        timer.cancel()
        isRunning = false
    }
}

abstract class Model(
        var position: PointF
) {
    val touchBounds = RectF()
    abstract fun move(delta: Int)
    abstract fun draw(canvas: Canvas)
    fun onTouch(x: Float, y: Float) {
        onTouchListener?.onTouch(x, y)
    }

    interface OnTouchListener {
        fun onTouch(x: Float, y: Float) {}
    }
    var onTouchListener: OnTouchListener? = null
}

class Star(position: PointF, val speed: Int, val direction: Int, val paint: Paint) : Model(position) {
    override fun move(delta: Int) {
        position.x += speed * delta / 1000f * cos(direction.toFloat())
        position.y += speed * delta / 1000f * sin(direction.toFloat())
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(position.x, position.y, 2f, paint)
    }

    private val id: Int = getStarId()

    companion object {
        private var starId: Int = 0
        fun getStarId(): Int {
            return starId++
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Star && other.id == id
    }

    override fun hashCode(): Int {
        return id
    }
}