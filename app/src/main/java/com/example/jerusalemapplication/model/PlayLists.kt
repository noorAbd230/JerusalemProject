package com.example.jerusalemapplication.model

data class PlayLists(var id:Int, var title:String,var vName:String) {

    companion object{
        val COL_ID = "id"
        val COL_TITLE = "title"
        val COL_VIDEO_NAME = "vName"



        val TABLE_NAME = "PlayLists"
        val TABLE_CREATE = "create table $TABLE_NAME ($COL_ID integer primary key autoincrement," +
                "$COL_TITLE text UNIQUE not null,$COL_VIDEO_NAME text not null)"

    }


}

