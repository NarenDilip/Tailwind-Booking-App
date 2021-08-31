package com.tailwind.kos.api

import com.google.gson.annotations.SerializedName
import com.tailwind.kos.model.LaneSession
import com.tailwind.kos.model.Response

class LaneListResponse : Response() {
    @SerializedName("lanes")
    var lanes: ArrayList<LaneSession>? = null
}