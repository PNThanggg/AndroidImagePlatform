package com.pnt.nattive_image_flutter

import android.os.Handler
import io.flutter.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : FlutterActivity() {
    private var methodChannel: MethodChannel? = null

    companion object {
        private const val METHOD_CHANNEL_NAME = "android_platform_images"

        // method name
        private const val DRAWABLE = "drawable"
        private const val ASSETS = "assets"

        private const val TAG = "MainActivity"

        // key for Flutter
        // name for drawable&mipmap or full path for assets images.
        private const val ARG_ID = "id"
        private const val ARG_QUALITY = "quality"

        private const val THREAD_POOL_SIZE = 5
    }

    private var drawableImageLoader: DrawableImageLoader? = null
    private var assetsImageLoader: AssetsImageLoader? = null

    private var fixedThreadPool: ExecutorService? = null
    private var mainHandler: Handler? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        drawableImageLoader = DrawableImageLoader(context)
        assetsImageLoader = AssetsImageLoader(context)

        mainHandler = Handler(applicationContext.mainLooper)
        fixedThreadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE)

        methodChannel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            METHOD_CHANNEL_NAME,
        )
        methodChannel?.setMethodCallHandler { call, result ->
            fixedThreadPool?.submit {
                val id: String? = call.argument(ARG_ID)
                val quality: Int? = call.argument(ARG_QUALITY)
                val bytes: ByteArray?

                val start = System.currentTimeMillis()

                when (call.method) {
                    DRAWABLE -> {
                        if (id == null) {
                            result.error("101", "Id null", null)
                        }

                        if (quality == null) {
                            result.error("102", "Quality null", null)
                        }

                        bytes = drawableImageLoader?.loadBitmapDrawable(id!!, quality!!)

                        if (bytes != null) {
                            val builder = "Image Size:" + bytes.size / 1000 + "kb\t" +
                                    "Time Used:" + (System.currentTimeMillis() - start) + "ms\t" +
                                    "Image Info:" + call.method + '/' + call.arguments

                            Log.d(TAG, builder)

                            mainHandler?.post { result.success(bytes) }
                        } else {
                            Log.d(TAG, "load fail:" + call.method + "/" + call.arguments);
                            result.error("103", "ByteArray null", null)
                        }
                    }

                    ASSETS -> {
                        if (id == null) {
                            result.error("101", "Id null", null)
                        }

                        if (quality == null) {
                            result.error("102", "Quality null", null)
                        }

                        bytes = assetsImageLoader?.loadImage(id!!)

                        if (bytes != null) {
                            val builder = "Image Size:" + bytes.size / 1000 + "kb\t" +
                                    "Time Used:" + (System.currentTimeMillis() - start) + "ms\t" +
                                    "Image Info:" + call.method + '/' + call.arguments

                            Log.d(TAG, builder)

                            mainHandler?.post { result.success(bytes) }
                        } else {
                            Log.d(TAG, "load fail:" + call.method + "/" + call.arguments);
                            result.error("103", "ByteArray null", null)
                        }
                    }

                    else -> result.notImplemented()
                }
            }
        }
    }

    override fun onDestroy() {
        methodChannel?.setMethodCallHandler(null)
        methodChannel = null

        drawableImageLoader?.dispose()
        assetsImageLoader?.dispose()

        fixedThreadPool?.shutdown()
        fixedThreadPool = null
        mainHandler = null

        super.onDestroy()
    }
}
