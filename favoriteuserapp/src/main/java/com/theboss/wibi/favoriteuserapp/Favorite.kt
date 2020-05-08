package com.theboss.wibi.favoriteuserapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite (
    var id: Int = 0,
    var login: String? = null,
    var avatarUrl: String? = null,
    var type: String? = null,
    var favorite: Int = 0
) : Parcelable