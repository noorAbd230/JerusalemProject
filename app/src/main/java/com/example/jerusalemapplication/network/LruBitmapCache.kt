package com.example.jerusalemapplication.network

import android.graphics.Bitmap
import com.android.volley.toolbox.ImageLoader.ImageCache;
import android.util.LruCache

class LruBitmapCache(sizeInKiloBytes: Int = defaultLruCacheSize) :
    LruCache<String?, Bitmap?>(sizeInKiloBytes), ImageCache {

    private fun sizeOf(key: String?, value: Bitmap): Int {
        return value.rowBytes * value.height / 1024
    }

    override fun getBitmap(url: String?): Bitmap? {
        return get(url)
    }

    override fun putBitmap(url: String?, bitmap: Bitmap?) {
        put(url, bitmap)
    }

    companion object {
        val defaultLruCacheSize: Int
            get() {
                val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
                return maxMemory / 8
            }
    }
}