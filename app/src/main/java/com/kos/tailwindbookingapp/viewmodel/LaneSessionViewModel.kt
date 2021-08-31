package com.kos.tailwindbookingapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.tailwind.kos.repositories.LaneRepository
import com.kos.tailwindbookingapp.model.LaneSession

class LaneSessionViewModel(application: Application) : AndroidViewModel(application) {
    private var laneRepository: LaneRepository? = null
    var laneSessionResponseLiveData: LiveData<LaneSession>? = null

    fun init() {
        laneRepository = LaneRepository()
        laneSessionResponseLiveData = laneRepository!!.getLaneSessionResponseLiveData() as LiveData<LaneSession>
    }

    fun getLaneSession(laneId:String) {
        laneRepository!!.getLaneSession(laneId)
    }


    fun updateLaneSession(jsonObject: JsonObject, laneId:String) {
        laneRepository!!.updateLaneSession(jsonObject, laneId)
    }
}