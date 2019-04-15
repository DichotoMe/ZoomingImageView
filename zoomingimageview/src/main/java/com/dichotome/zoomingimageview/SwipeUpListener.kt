package com.dichotome.zoomingimageview

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View


class SwipeUpListener<T: View> (
    private val view: T,
    private val action: ((it: T) -> Unit)?
) : GestureDetector.SimpleOnGestureListener() {

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

        if (velocityY <= 0)
            action?.invoke(view)

        return true
    }

    override fun onDown(e: MotionEvent?) = true
}