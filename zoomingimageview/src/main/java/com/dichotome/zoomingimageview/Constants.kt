package com.dichotome.zoomingimageview

import android.content.Context
import android.content.res.Resources

class Constants(val context: Context) {
    val DISPLAY_WIDTH: Int = Resources.getSystem().displayMetrics.widthPixels
    val DISPLAY_HEIGHT: Int = Resources.getSystem().displayMetrics.heightPixels
}