package com.theboss.wibi.submiss2appgithubuserwibi.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.theboss.wibi.submiss2appgithubuserwibi.data.database.DatabaseContract.AUTHORITY
import com.theboss.wibi.submiss2appgithubuserwibi.data.database.DatabaseContract.UserFavoriteColumns.Companion.CONTENT_URI
import com.theboss.wibi.submiss2appgithubuserwibi.data.database.DatabaseContract.UserFavoriteColumns.Companion.TABLE_NAME
import com.theboss.wibi.submiss2appgithubuserwibi.data.database.UserFavoriteHelper

class UserFavoriteProvider : ContentProvider() {

    companion object{
        // nilai Int digunakan sebagai indentifier antara select all saam select by id
        private const val USER_FAVORITE = 1
        private const val USER_FAVORITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userFavoriteHelper: UserFavoriteHelper
    }

    /*
        Uri matcher untuk mempermudah identifier dengan menggunakan integer
        misal
        uri com.theboss.wibi.submiss2appgithubuserwibi/user_favorite dicocokan dengan integer 1
        uri com.theboss.wibi.submiss2appgithubuserwibi/user_favorite/# dicocokan dengan integer 2
    */
    init {
        //  content://com.theboss.wibi.submiss2appgithubuserwibi/user_favorite
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER_FAVORITE)

        //  content://com.theboss.wibi.submiss2appgithubuserwibi/user_favorite/id
        sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_FAVORITE_ID)
    }

    override fun onCreate(): Boolean {
        userFavoriteHelper = UserFavoriteHelper.getInstance(context as Context)
        userFavoriteHelper.open()
        return true
    }

    //mambaca semua data
    override fun query(uri: Uri, strings: Array<String>?, s: String?, string1: Array<String>?, s1: String?): Cursor? {
        return when(sUriMatcher.match(uri)){
            USER_FAVORITE -> userFavoriteHelper.queryAll()
            //USER_FAVORITE_ID -> cursor = userFavoriteHelper   //Ps: saya gak buat query by id
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    //memasukkan data
    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (USER_FAVORITE){
            sUriMatcher.match(uri) -> userFavoriteHelper.insert(contentValues)
            else ->0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
       return 0
    }

    //menghapus data berdasarkan id
    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when(USER_FAVORITE_ID){
            sUriMatcher.match(uri) -> userFavoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}









