package com.dichotome.zoomingimageviewutils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.View
import androidx.core.util.Pair

abstract class AnimationHelper(
    protected val view: View,
    private val interp: TimeInterpolator?,
    protected val duration: Long
) {
    private var animators = ArrayList<ObjectAnimator>()
    fun animateFloat(
        propertyName: String,
        from: Float,
        to: Float,
        animInterpolator: TimeInterpolator? = interp,
        dur: Long = duration,
        returnTo: Float? = null,
        autoStart: Boolean = true
    ): ObjectAnimator? {
        var path = floatArrayOf(from, to)
        returnTo?.let {
            path += returnTo
        }
        return ObjectAnimator.ofFloat(view, propertyName, *path)
            .apply {
                interpolator = animInterpolator
                duration = dur
                if (autoStart) start()
            }.addTo(animators)

    }

    fun animateInt(
        propertyName: String,
        from: Int,
        to: Int,
        animInterpolator: TimeInterpolator? = interp,
        dur: Long = duration,
        returnTo: Int? = null,
        autoStart: Boolean = true
    ): ObjectAnimator? {
        var path = intArrayOf(from, to)
        returnTo?.let { path += returnTo }
        return ObjectAnimator.ofInt(view, propertyName, *path)
            .apply {
                interpolator = animInterpolator
                duration = dur
                if (autoStart) start()
            }.addTo(animators)

    }

    fun end() {
        val endable = animators
        animators = arrayListOf()
        endable.forEach {
            it.end()
        }
    }

    fun cancel() {
        val cancellable = animators
        animators = arrayListOf()
        cancellable.forEach {
            it.cancel()
        }
    }
}


abstract class LinearAnimationHelper(
    view: View,
    interpolator: TimeInterpolator?,
    duration: Long
) : AnimationHelper(view, interpolator, duration) {
    abstract fun evaluate(): Animator?
}

abstract class PlainAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : AnimationHelper(target, interpolator, duration) {
    abstract fun evaluateXY(): Pair<out Animator?, out Animator?>
}

abstract class SimpleAnimationHelper(view: View, interpolator: TimeInterpolator, duration: Long) :
    AnimationHelper(view, interpolator, duration) {
    abstract fun evaluate(): Animator?
}
