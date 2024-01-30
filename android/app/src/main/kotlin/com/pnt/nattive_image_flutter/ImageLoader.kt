package com.pnt.nattive_image_flutter

import android.content.Context

abstract class ImageLoader {
    protected var appContext: Context? = null

    fun dispose() {
        appContext = null
    }
}

