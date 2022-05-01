package com.rivaldo.screeningtestsuitmedia.api

import com.rivaldo.screeningtestsuitmedia.model.Guest
import com.rivaldo.screeningtestsuitmedia.model.GuestResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Api {
    @GET("users")
    fun getUsers(
        @QueryMap parameters : HashMap<String, String>
    ): Call<GuestResponse>

}