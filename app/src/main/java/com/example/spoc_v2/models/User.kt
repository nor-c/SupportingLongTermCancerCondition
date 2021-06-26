package com.example.spoc_v2.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize



@Parcelize      //Used to pass (intent.putExtra(USER_KEY,userItem.user) in NewMessageActivity
class User(val uid: String, val username: String, val profileImageUrl: String): Parcelable {
    constructor(): this("", "","")

}


