package com.example.jerusalemapplication.datebase

import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mynote.model.DataLibrary


class LibraryDH(activity: Activity) :
    SQLiteOpenHelper(activity, DATABASE_NAME, null, DATABASE_VERSION) {

    private val db: SQLiteDatabase = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(DataLibrary.TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("Drop table if exists ${DataLibrary.TABLE_NAME}")
        onCreate(db)
    }


    //DML

    fun insertLibraryItem(
        title:String,
         details : String
    ): Boolean {

        val cv = ContentValues()
        cv.put(DataLibrary.COL_TITLE, title)
        cv.put(DataLibrary.COL_DETAIL, details)

//        cv.put(Song.COL_SONG_DETAILS, details)

        return db.insert(DataLibrary.TABLE_NAME, null, cv) > 0
    }





    fun getLibraryData(): ArrayList<DataLibrary> {
        val data = ArrayList<DataLibrary>()
        val c =
            db.rawQuery("select * from ${DataLibrary.TABLE_NAME}", null)
        c.moveToFirst()
        while (!c.isAfterLast) {
            val s = DataLibrary(c.getInt(0),c.getString(1),c.getString(2))
            data.add(s)
            c.moveToNext()
        }
        c.close()
        return data
    }









    companion object {
        val DATABASE_NAME = "LibraryApp"
        val DATABASE_VERSION = 2

    }
}