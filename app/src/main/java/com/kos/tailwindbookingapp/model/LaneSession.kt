package com.kos.tailwindbookingapp.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tailwind.kos.model.Response
import java.io.Serializable

@Entity(tableName = "laneSession")
class LaneSession : Response(), Serializable {

    @SerializedName("id")
    var id: String? = null

    @PrimaryKey
    @SerializedName("lane_id")
    var laneId = 0

    @SerializedName("lane_name")
    var laneName: String? = null

    @SerializedName("is_occupied")
    var isOccupied = false

    @SerializedName("created_by")
    var createdBy: String? = null

    @SerializedName("created_on")
    var createdOn: String? = null

    @SerializedName("duration")
    var duration: Int = 0

    @SerializedName("extra_time")
    var extraTime: Int = 0

    @SerializedName("pause_time")
    var pauseTime: Int = 0

    @SerializedName("pass_code")
    var passCode: String? = null

    @SerializedName("started_on")
    var startedOn: String? = null

    @SerializedName("no_of_players")
    var noOfPlayers: Int = 0

    @SerializedName("status")
    var status: String? = null

    @Ignore
    @SerializedName("players")
    var players: ArrayList<String>? = null

    @SerializedName("is_call_request_raised")
    var isCallRequestRaised = false

}