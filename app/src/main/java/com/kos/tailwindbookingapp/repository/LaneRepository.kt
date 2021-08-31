package com.tailwind.kos.repositories

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.kos.tailwindbookingapp.Constants
import com.tailwind.kos.api.ApiInterface
import com.tailwind.kos.api.LaneListResponse
import com.kos.tailwindbookingapp.model.LaneSession
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LaneRepository {
    private val apiInterface: ApiInterface
    private val laneListResponseLiveData: MutableLiveData<LaneListResponse?> = MutableLiveData<LaneListResponse?>()
    private val laneSessionResponseLiveData: MutableLiveData<LaneSession?> = MutableLiveData<LaneSession?>()
    fun getLanes() {
        apiInterface.getLaneList()
            .enqueue(object : Callback<LaneListResponse?> {
                override fun onResponse(
                    call: Call<LaneListResponse?>,
                    response: Response<LaneListResponse?>
                ) {
                    if (response.body() != null) {
                        laneListResponseLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<LaneListResponse?>, t: Throwable) {
                    laneListResponseLiveData.postValue(null)
                }
            })
    }


    fun getLaneSession(laneId:String) {
        apiInterface.getLaneSession(laneId = laneId)
            .enqueue(object : Callback<LaneSession?> {
                override fun onResponse(
                    call: Call<LaneSession?>,
                    response: Response<LaneSession?>
                ) {
                    if (response.body() != null) {
                        laneSessionResponseLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<LaneSession?>, t: Throwable) {
                    laneSessionResponseLiveData.postValue(null)
                }
            })
    }

    fun updateLaneSession(jsonObject: JsonObject, laneId:String) {
        apiInterface.updateLaneSession(jsonObject, laneId)
            .enqueue(object : Callback<LaneSession?> {
                override fun onResponse(
                    call: Call<LaneSession?>,
                    response: Response<LaneSession?>
                ) {
                    if (response.body() != null) {
                        laneSessionResponseLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<LaneSession?>, t: Throwable) {
                    laneSessionResponseLiveData.postValue(null)
                }
            })
    }

    fun getLaneSessionResponseLiveData(): MutableLiveData<LaneSession?> {
        return laneSessionResponseLiveData
    }



    fun getLanesResponseLiveData(): MutableLiveData<LaneListResponse?> {
        return laneListResponseLiveData
    }


    init {
        val client = OkHttpClient.Builder().build()
        apiInterface = Retrofit.Builder()
            .baseUrl(Constants.SERVER_PREFIX_RESTAURANT_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}