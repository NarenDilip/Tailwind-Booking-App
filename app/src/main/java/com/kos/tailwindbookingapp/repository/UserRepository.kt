package com.kos.tailwindbookingapp.repository

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.kos.tailwindbookingapp.Constants
import com.tailwind.kos.api.ApiInterface
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {
    private val apiInterface: ApiInterface
    private val loginResponseLiveData: MutableLiveData<com.tailwind.kos.model.Response?> = MutableLiveData<com.tailwind.kos.model.Response?>()

    fun getLogin(jsonObject: JsonObject) {
        apiInterface.getLogin(jsonObject)
            .enqueue(object : Callback<com.tailwind.kos.model.Response?> {
                override fun onResponse(
                    call: Call<com.tailwind.kos.model.Response?>,
                    response: Response<com.tailwind.kos.model.Response?>
                ) {
                    if (response.body() != null) {
                        loginResponseLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<com.tailwind.kos.model.Response?>, t: Throwable) {
                    loginResponseLiveData.postValue(null)
                }
            })
    }

    fun getLoginResponseLiveData(): MutableLiveData<com.tailwind.kos.model.Response?> {
        return loginResponseLiveData
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