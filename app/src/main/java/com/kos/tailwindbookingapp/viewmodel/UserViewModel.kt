package com.kos.tailwindbookingapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.kos.tailwindbookingapp.repository.UserRepository
import com.tailwind.kos.api.LaneListResponse
import com.tailwind.kos.model.Response
import com.tailwind.kos.repositories.LaneRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var laneRepository: LaneRepository? = null
    var laneListResponseLiveData: LiveData<LaneListResponse>? = null

    private var userRepository: UserRepository? = null
    var loginResponseLiveData: LiveData<Response>? = null
    fun init() {
        laneRepository = LaneRepository()
        laneListResponseLiveData =
            laneRepository!!.getLanesResponseLiveData() as LiveData<LaneListResponse>
        userRepository = UserRepository()
        loginResponseLiveData = userRepository!!.getLoginResponseLiveData() as LiveData<Response>
    }

    fun getLogin() {
        laneRepository!!.getLanes()
    }

    fun getLogin(jsonObject: JsonObject) {
        userRepository!!.getLogin(jsonObject)
    }
}