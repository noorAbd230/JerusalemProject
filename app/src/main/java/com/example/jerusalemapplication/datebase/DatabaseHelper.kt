package com.example.jerusalemapplication.datebase

import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.jerusalemapplication.model.PlayLists


class DatabaseHelper(activity: Activity) :
    SQLiteOpenHelper(activity, DATABASE_NAME, null, DATABASE_VERSION) {

    private val db: SQLiteDatabase = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(PlayLists.TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("Drop table if exists ${PlayLists.TABLE_NAME}")
        onCreate(db)
    }


    //DML

    fun insertPlayListName(
        title:String,
         videosName : String
    ): Boolean {

        val cv = ContentValues()
        cv.put(PlayLists.COL_TITLE, title)
        cv.put(PlayLists.COL_VIDEO_NAME, videosName)

//        cv.put(Song.COL_SONG_DETAILS, details)

        return db.insert(PlayLists.TABLE_NAME, null, cv) > 0
    }



    fun getThreePlayList(): ArrayList<PlayLists> {
        val data = ArrayList<PlayLists>()
        val c =
            db.rawQuery("select * from ${PlayLists.TABLE_NAME} limit 3", null)
        c.moveToFirst()
        while (!c.isAfterLast) {
        val s = PlayLists(c.getInt(0),c.getString(1),c.getString(2))
            data.add(s)
            c.moveToNext()
        }
        c.close()
        return data
    }


    fun getAllPlayList(): ArrayList<PlayLists> {
        val data = ArrayList<PlayLists>()
        val c =
            db.rawQuery("select * from ${PlayLists.TABLE_NAME}", null)
        c.moveToFirst()
        while (!c.isAfterLast) {
            val s = PlayLists(c.getInt(0),c.getString(1),c.getString(2))
            data.add(s)
            c.moveToNext()
        }
        c.close()
        return data
    }


    fun getVideosName(name : String): ArrayList<PlayLists> {
        val data = ArrayList<PlayLists>()
        val c =
            db.rawQuery("select * from ${PlayLists.TABLE_NAME} where ${PlayLists.COL_TITLE} = '$name' ", null)
        c.moveToFirst()
        while (!c.isAfterLast) {
            val s = PlayLists(c.getInt(0),c.getString(1),c.getString(2))
            data.add(s)
            c.moveToNext()
        }
        c.close()
        return data
    }

//
//    fun getPlayList(search:String): ArrayList<PlayLists> {
//        val data = ArrayList<PlayLists>()
//        val c =
//            db.rawQuery("select * from ${PlayLists.TABLE_NAME} where ${PlayLists.COL_TITLE} like '$search%'  order by ${PlayLists.COL_ID} desc ", null)
//        c.moveToFirst()
//        while (!c.isAfterLast) {
//            val s = PlayLists(c.getInt(0),c.getString(1))
//            data.add(s)
//            c.moveToNext()
//        }
//        c.close()
//        return data
//    }






    companion object {
        val DATABASE_NAME = "PlayListApp"
        val DATABASE_VERSION = 2

    }
}