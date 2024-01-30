package com.pnt.nattive_image_flutter

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.IOException


class AssetsImageLoader(private val context: Context) : ImageLoader() {

    init {
        this.appContext = context
    }

    companion object {
        private const val TAG = "AssetsImageLoader"
    }

    fun loadImage(path: String): ByteArray? {
        var buffer: ByteArray? = null
        val assetManager: AssetManager = context.assets

        try {
            val inputStream = assetManager.open(path)
            buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
        } catch (exception: IOException) {
            Log.e(TAG, "exceptionMessage: ${exception.message}")
        }

        return buffer
    }
}

