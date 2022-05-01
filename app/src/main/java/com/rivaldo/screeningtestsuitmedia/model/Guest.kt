package com.rivaldo.screeningtestsuitmedia.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Guest(
    @SerializedName("id")
    var id : String,
    @SerializedName("email")
    var email: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    @SerializedName("avatar")
    var avatar : String
)
