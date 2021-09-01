package com.tailwind.kos.api

import com.google.gson.JsonObject
import com.kos.tailwindbookingapp.model.LaneSession
import com.tailwind.kos.model.Response
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("login/")
    fun getLogin(@Body jsonString: JsonObject): Call<LaneSession>

    @GET("lanes")
    fun getLaneList(): Call<LaneListResponse>

    @GET("session/{path_variable}/")
    fun getLaneSession(@Path("path_variable") laneId: String): Call<LaneSession>


    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("session/{path_variable}/")
    fun updateLaneSession(@Body jsonString: JsonObject, @Path("path_variable") laneId: String): Call<LaneSession>


}