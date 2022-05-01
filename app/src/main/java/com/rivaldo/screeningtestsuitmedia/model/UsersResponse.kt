package com.rivaldo.screeningtestsuitmedia.model

import com.google.gson.annotations.SerializedName

data class GuestResponse(
    val page: Int,
    @SerializedName("total_pages")
    val totalPage: Int,
    val data : ArrayList<Guest>
)
