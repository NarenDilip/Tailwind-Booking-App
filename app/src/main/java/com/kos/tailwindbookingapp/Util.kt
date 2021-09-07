package com.kos.tailwindbookingapp

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.kos.tailwindbookingapp.model.LaneSession
import java.text.SimpleDateFormat
import java.util.*


object Util {

    fun hideKeyboard(view: View, context: Context) {
        val inputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getProgress(endTime: Long, startTime: Long): Double {
        val total: Long = (endTime - System.currentTimeMillis()) * 100 / (endTime - startTime)
        val percentage: Double = (100 - total).toDouble()
        if (percentage > 90) {
            return 95.0
        }
        return percentage
    }

    fun getActiveProgress(endTime: Long, startTime: Long): Double {
        return (System.currentTimeMillis() - endTime) * 100 / (startTime - endTime).toDouble()
    }

    fun getEndTime(createdTime: Long): Long {
        return createdTime + 60 * 60000
    }

    fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    fun getCreatedTime(lane: LaneSession): Long {
        return getMilliSecondsFromDate(lane.createdOn.toString())
    }

    fun getStartedTime(lane: LaneSession): Long {
        return getMilliSecondsFromDate(lane.startedOn.toString())
    }

    fun getMilliSecondsFromDate(utcTime: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val date1 = dateFormat.parse(utcTime)
        return date1!!.time
    }

    fun getTimeoutStartedTime(lane: LaneSession): Long {
        return getStartedTime(lane) + lane.duration * 60000
    }

    fun getEndActiveTime(lane: LaneSession): Long {
        return (getStartedTime(lane) + lane.duration * 60000 + lane.pauseTime)
    }

    fun getTimeoutTime(lane: LaneSession?): Int {
        return (getCurrentTime() - getTimeoutStartedTime(lane!!)).toInt()
    }

    fun getIdleTime(lane: LaneSession?): Long {
        return getCurrentTime() - getCreatedTime(lane!!)
    }

    fun getTimeInMilliSeconds(lane: LaneSession): Double {
        return if (lane.status == "IDLE") {
            getProgress(getEndTime(getCreatedTime(lane)), getCreatedTime(lane))
        } else if (lane.status == "ACTIVE" && lane.startedOn != null) {
            getActiveProgress(getEndActiveTime(lane), getStartedTime(lane))
        } else if (lane.status == "TIMEOUT") {
            getProgress(
                getEndTime(getTimeoutStartedTime(lane)), getTimeoutStartedTime(lane)
            )
        } else {
            0.0
        }
    }

    fun getRemainingTimeInMilliseconds(lane: LaneSession?): Long {
        return getEndActiveTime(lane!!) - System.currentTimeMillis()
    }

    fun getTimeMilliSeconds(lane: LaneSession): Long {
        return if (lane.status == "IDLE") {
            val addOnTime = getEndTime(getCreatedTime(lane))
            timeDifferenceCalc(addOnTime, System.currentTimeMillis())
        } else if (lane.status == "ACTIVE" && lane.startedOn != null) {
            val endTime = getEndActiveTime(lane)
            timeDifferenceCalc(endTime, System.currentTimeMillis())
        } else if (lane.status == "TIMEOUT") {
            val createdTime = getTimeoutStartedTime(lane)
            val addOnTime = getEndTime(createdTime)
            timeDifferenceCalc(addOnTime, System.currentTimeMillis())
        } else {
            0
        }
    }

    private fun timeDifferenceCalc(addOnTime: Long, currentTime: Long): Long {
        val calculatedOutput: Int = if (addOnTime > currentTime) {
            (addOnTime - currentTime).toInt()
        } else {
            (currentTime - addOnTime).toInt()
        }
        return calculatedOutput.toLong();
    }

    fun getTimeString(mins: Long): String? {
        return if (mins <= 2) "Just now" else String.format("%02d", mins) + " mins in"
    }
}