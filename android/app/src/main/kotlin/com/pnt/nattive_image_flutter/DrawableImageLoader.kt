package com.pnt.nattive_image_flutter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream


class DrawableImageLoader(private val context: Context) : ImageLoader() {
    init {
        appContext = context
    }


    companion object {
        private const val TAG = "DrawableImageLoader"
    }

    @SuppressLint("DiscouragedApi")
    fun loadBitmapDrawable(name: String, quality: Int): ByteArray? {
        var buffer: ByteArray? = null
        var drawable: Drawable? = null
        try {
            val type = "drawable"
            val id: Int = context.resources.getIdentifier(name, type, context.packageName)

            if (id <= 0) {
                return null
            }

            drawable = ContextCompat.getDrawable(context, id)
        } catch (exception: Exception) {
            Log.e(TAG, "exceptionMessage: ${exception.message}")
        }

        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            if (bitmap != null) {
                val stream = ExposedByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
                buffer = stream.buffer()
            }
        }

        return buffer
    }

    internal class ExposedByteArrayOutputStream : ByteArrayOutputStream() {
        fun buffer(): ByteArray {
            return buf
        }
    }
}

