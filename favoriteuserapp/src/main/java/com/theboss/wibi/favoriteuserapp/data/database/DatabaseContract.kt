package com.theboss.wibi.favoriteuserapp.data.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    //mendefinisikan Content URI agar dapat digunakan provider
    const val AUTHORITY = "com.theboss.wibi.submiss2appgithubuserwibi"
    const val SCHEME = "content"

    internal class UserFavoriteColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "user_favorite"
            const val ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
            const val TYPE = "type"
            const val FAVORITE = "favorite"

            //membuat URI content://com.theboss.wibi.submiss2appgithubuserwibi/user_favorite
            val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}