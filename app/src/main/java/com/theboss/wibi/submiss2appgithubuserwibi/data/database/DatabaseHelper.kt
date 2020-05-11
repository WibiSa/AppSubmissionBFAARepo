package com.theboss.wibi.submiss2appgithubuserwibi.data.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.theboss.wibi.submiss2appgithubuserwibi.data.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{

        private const val DATABASE_NAME = "dbuserfavorite"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_USER_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.UserFavoriteColumns.ID} INTEGER PRIMARY KEY," +
                "${DatabaseContract.UserFavoriteColumns.LOGIN} TEXT NOT NULL," +
                "${DatabaseContract.UserFavoriteColumns.AVATAR_URL} TEXT NOT NULL," +
                "${DatabaseContract.UserFavoriteColumns.TYPE} TEXT NOT NULL," +
                "${DatabaseContract.UserFavoriteColumns.FAVORITE} INTEGER NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("SQLite Database", "$DATABASE_NAME create")
        db.execSQL(SQL_CREATE_TABLE_USER_FAVORITE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


}