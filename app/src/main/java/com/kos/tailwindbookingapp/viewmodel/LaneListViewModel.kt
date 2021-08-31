package com.kos.tailwindbookingapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.tailwind.kos.api.LaneListResponse
import com.tailwind.kos.repositories.LaneRepository

class LaneListViewModel(application: Application) : AndroidViewModel(application) {
    private var laneRepository: LaneRepository? = null
    var laneListResponseLiveData: LiveData<LaneListResponse>? = null

    fun init() {
        laneRepository = LaneRepository()
        laneListResponseLiveData = laneRepository!!.getLanesResponseLiveData() as LiveData<LaneListResponse>
    }

    fun getLanes() {
        laneRepository!!.getLanes()
    }
}