package com.example.webserviceass

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.example.jerusalemapplication.network.LruBitmapCache

class MySingleton:Application() {
    val TAG ="no"
    private var mRequestQueue:RequestQueue?=null
    private var mImageLoader:ImageLoader?=null
    companion object{
        private var mInstance:MySingleton?=null

        fun getInstance():MySingleton?{
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
         mInstance=this
    }

     fun getRequestQueue():RequestQueue?{
        if (mRequestQueue==null){
            mRequestQueue=Volley.newRequestQueue(this)
        }
        return mRequestQueue
    }

    fun <T> addRequestQueue(req:Request<T>,tag: String?){
        req.tag=if (TextUtils.isEmpty(tag)) TAG else tag
        getRequestQueue()!!.add(req)
    }

    fun <T> addRequestQueue(req:Request<T>){
        req.tag=TAG
        getRequestQueue()!!.add(req)
    }


    fun getImageLoader():ImageLoader?{
        getRequestQueue()
        if(mImageLoader==null){
        mImageLoader= ImageLoader(mRequestQueue, LruBitmapCache())
        }
        return mImageLoader
    }

}