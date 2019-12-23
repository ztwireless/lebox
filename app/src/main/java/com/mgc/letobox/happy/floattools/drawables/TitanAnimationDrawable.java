package com.mgc.letobox.happy.floattools.drawables;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

public class TitanAnimationDrawable extends AnimationDrawable {

    /**
     * Handles the animation callback.
     */
    Handler mAnimationHandler;

    public TitanAnimationDrawable(AnimationDrawable aniDrawable) {
        /* Add each frame to our animation drawable */
        super();
        for (int i = 0; i < aniDrawable.getNumberOfFrames(); i++) {
            this.addFrame(aniDrawable.getFrame(i), aniDrawable.getDuration(i));
        }
        setOneShot(aniDrawable.isOneShot());
    }

    @Override
    public void start() {
        super.start();
        /*
         * Call super.start() to call the base class start animation method.
         * Then add a handler to call onAnimationFinish() when the total
         * duration for the animation has passed
         */
        mAnimationHandler = new Handler();
        mAnimationHandler.postDelayed(new Runnable() {

            public void run() {
                onAnimationFinished();
            }
        }, getTotalDuration());

    }

    /**
     * Gets the total duration of all frames.
     *
     * @return The total duration.
     */
    public int getTotalDuration() {

        int iDuration = 0;

        for (int i = 0; i < this.getNumberOfFrames(); i++) {
            iDuration += this.getDuration(i);
        }

        return iDuration;
    }

    /**
     * Called when the animation finishes.
     */
    void onAnimationFinished() {
        if (animationListener != null) animationListener.onAnimationFinished();
    }

    private AnimationListener animationListener;
    public void setAnimationListener(AnimationListener listener) {
        animationListener = listener;
    }

    public interface AnimationListener {
        void onAnimationFinished();
    }
}