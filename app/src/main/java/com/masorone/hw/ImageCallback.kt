package com.masorone.hw

import android.graphics.Bitmap

interface ImageCallback {
    fun success(bitmap: Bitmap)
    fun failed()
}