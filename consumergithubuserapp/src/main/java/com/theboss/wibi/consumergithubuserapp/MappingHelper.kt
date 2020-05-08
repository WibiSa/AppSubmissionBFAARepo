package com.theboss.wibi.consumergithubuserapp

import android.database.Cursor

/**Kelas pembantu konversi dari Cursor ke Arraylist.
 * db -> adapter */
object MappingHelper {

    fun mapCursorToArrayList(userFavoriteCursor: Cursor?) : ArrayList<Favorite>{
        val favoriteList = ArrayList<Favorite>()
        userFavoriteCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.AVATAR_URL))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.TYPE))
                val favorite = getInt(getColumnIndexOrThrow(DatabaseContract.UserFavoriteColumns.FAVORITE))

                favoriteList.add(Favorite(id, login, avatarUrl, type, favorite))
            }
        }
        return favoriteList
    }
}