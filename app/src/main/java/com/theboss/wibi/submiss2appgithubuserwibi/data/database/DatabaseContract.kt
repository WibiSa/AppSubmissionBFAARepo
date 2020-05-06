package com.theboss.wibi.submiss2appgithubuserwibi.data.database

import android.provider.BaseColumns

object DatabaseContract {

    internal class UserFavoriteColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "user_favorite"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
            const val TYPE = "type"
            const val FAVORITE = "favorite"
        }
    }
}