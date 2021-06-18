package com.example.mynote.model


data class DataLibrary(var id: Int, var Title: String?, var detail: String?) {

    companion object {

        val COL_ID = "id"
        val COL_TITLE = "title"
        val COL_DETAIL = "detail"

        val TABLE_NAME = "library"
        val TABLE_CREATE = "create table $TABLE_NAME ($COL_ID integer primary key autoincrement," +
                "$COL_TITLE text not null, $COL_DETAIL text not null)"

    }


}