package com.dichotome.zoomingimageviewutils

import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.util.Pair
import com.dichotome.roundedimageview.RoundedImageView
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class DetachFromFrameAnimationHelper(
    private val targetView: RoundedImageView,
    private val detachFrom: RoundedImageView,
    timeInterpolator: TimeInterpolator,
    animDuration: Long
) : LinearAnimationHelper(targetView, timeInterpolator, animDuration) {

    private val init = targetView.cornerRadius
    override fun evaluate() = (view.isDisplayed).let { visible ->
        animateInt(
            "cornerRadius",
            targetView.cornerRadius,
            if (visible) init else 0
        )?.apply {
            doOnStart {
                if (!visible) {
                    detachFrom.isDisplayed = false
                    view.isDisplayed = true
                }
            }
            doOnEnd {
                if (visible && targetView.cornerRadius == init) {
                    view.isDisplayed = false
                    detachFrom.isDisplayed = true
                }
            }
            start()
        }
    }
}

class ZoomAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : PlainAnimationHelper(target, interpolator, duration) {
    override fun evaluateXY() = (view.scaleX == 1f).let {
        val zoomWidth = view.getZoomWidth()
        val zoomHeight = view.getZoomHeight()

        val ratio = (min(zoomWidth, zoomHeight).toFloat()) / view.layoutParams.width
        val roundedRatio = ceil(ratio * 10) / 10

        val endValue = if (it) roundedRatio else 1f

        Pair(
            animateFloat("scaleX", view.scaleX, endValue),
            animateFloat("scaleY", view.scaleY, endValue)
        )
    }
}

class ZoomTranslationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long
) : PlainAnimationHelper(target, interpolator, duration) {

    private var initX = view.x
    private var initY = view.y

    private val width = view.getZoomWidth()
    private val height = view.getZoomHeight()

    private val dimensionX = view.layoutParams.width
    private val dimensionY = view.layoutParams.height

    override fun evaluateXY() = (view.scaleX == 1f).let {
        val direction = if (it) 1 else -1

        val viewCenterEndX = if (it) width / 2f else view.x + dimensionX / 2f
        val viewCenterEndY = if (it) height / 2f else view.y + dimensionY / 2f

        val offsetX = viewCenterEndX - initX - dimensionX / 2
        val offsetY = viewCenterEndY - initY - dimensionY / 2

        Pair(
            animateFloat(
                "translationX",
                view.x,
                view.x + offsetX * direction
            ),
            animateFloat(
                "translationY",
                view.y,
                view.y + offsetY * direction
            )
        )
    }
}

class ZoomCircularRevealHelper(
    photoView: RoundedImageView,
    private val viewToReveal: View,
    private val TimeInterpolator: TimeInterpolator,
    private var startRadius: Int,
    private var stopRadius: Int,
    private val animDuration: Long
) : SimpleAnimationHelper(photoView, TimeInterpolator, animDuration) {

    private val centerX = (photoView.x + photoView.layoutParams.width.toFloat() / 2).toInt()
    private val centerY = (photoView.y + photoView.layoutParams.height.toFloat() / 2).toInt()

    override fun evaluate() = (!viewToReveal.isDisplayed).let { visible ->

        val beginRadius = if (visible) startRadius else stopRadius
        val endRadius = if (visible) stopRadius else startRadius

        ViewAnimationUtils.createCircularReveal(
            viewToReveal,
            centerX,
            centerY,
            beginRadius.toFloat(),
            endRadius.toFloat()
        ).apply {
            doOnStart {
                if (visible) viewToReveal.isDisplayed = true
            }
            doOnEnd {
                if (!visible) viewToReveal.isDisplayed = false
            }
            interpolator = TimeInterpolator
            duration = animDuration
            start()
        }
    }
}


class AlphaAnimationHelper(
    target: View,
    interpolator: TimeInterpolator,
    duration: Long,
    private val startValue: Float = 1f,
    private val endValue: Float
) : LinearAnimationHelper(target, interpolator, duration) {

    override fun evaluate() = (view.alpha < max(startValue, endValue)).let {
        animateFloat(
            "alpha",
            if (it) startValue else endValue,
            if (it) endValue else startValue
        )
    }
}