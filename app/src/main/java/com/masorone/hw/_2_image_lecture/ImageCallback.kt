package com.masorone.hw._2_image_lecture

import android.graphics.Bitmap

interface ImageCallback {
    fun success(bitmap: Bitmap)
    fun failed()
}