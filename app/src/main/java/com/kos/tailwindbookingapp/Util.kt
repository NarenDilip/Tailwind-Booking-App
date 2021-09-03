package com.kos.tailwindbookingapp

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.kos.tailwindbookingapp.model.LaneSession
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


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
            return 90.0;
        }
        return percentage;
    }

    fun getProgressinMilli(endTime: Long, startTime: Long): Long {
        val total: Long = (System.currentTimeMillis() - endTime) * 100 / (endTime - startTime)
//        val percentage: Double = (100 - total).toDouble()
//        if (percentage > 90) {
//            return 90.0;
//        }
        return total;
    }


    fun getActiveProgress(endTime: Long, startTime: Int): Double {
        return (System.currentTimeMillis() - endTime) * 100 / (startTime - endTime).toDouble()
    }

    fun getEndTime(createdTime: Long): Long {
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(createdTime)
        Log.v("created time", "====" + createdTime + "====" + seconds)
        return createdTime + 60 * 60000
    }

    fun getCreatedTime(lane: LaneSession): Long {
        return getMilliSecondsFromDate(lane.createdOn.toString())
    }

    fun getStartedTime(lane: LaneSession): Long {
        return getMilliSecondsFromDate(lane.startedOn.toString())
    }

    fun getMilliSecondsFromDate(utcTime: String?): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val date1 = dateFormat.parse(utcTime)
        return date1.time
    }

    fun getTimeoutStartedTime(lane: LaneSession): Long {
        return getStartedTime(lane) + lane.duration * 60000
    }

    fun getEndActiveTime(lane: LaneSession): Long {
        return (getStartedTime(lane) + lane.duration * 60000 + lane.pauseTime)
    }

    fun getTimeInMilliSeconds(lane: LaneSession): Double {
        if (lane.status == "IDLE") {
            return getProgress(getEndTime(getCreatedTime(lane)), getCreatedTime(lane))
        } else if (lane.status == "ACTIVE" && lane.startedOn != null) {
            return getActiveProgress(getEndActiveTime(lane), getStartedTime(lane).toInt())
        } else if (lane.status == "TIMEOUT") {
            return getProgress(
                getEndTime(getTimeoutStartedTime(lane)), getTimeoutStartedTime(lane)
            )
        } else {
            return 0.0
        }
    }

    fun getTimeMilliSeconds(lane: LaneSession): Long {
        if (lane.status == "IDLE") {
            var calculatedOutput = 0
            //First Step
            val createdTime = getCreatedTime(lane)
            //Secons Step
            val addOnTime = getEndTime(getCreatedTime(lane))
            //Third Step
            val curretTime = System.currentTimeMillis()
            //Fourth Step
            if (addOnTime > curretTime) {
                calculatedOutput = (addOnTime - curretTime).toInt()
            } else {
                calculatedOutput = 0
            }
            return calculatedOutput.toLong();


//            return getProgressinMilli(getEndTime(getCreatedTime(lane)), getCreatedTime(lane))
        } else if (lane.status == "ACTIVE" && lane.startedOn != null) {
            return getActiveProgress(getEndActiveTime(lane), getStartedTime(lane).toInt()).toLong()
        } else if (lane.status == "TIMEOUT") {
            return getProgressinMilli(
                getEndTime(getTimeoutStartedTime(lane)),
                getTimeoutStartedTime(lane)
            )
        } else {
            return 0
        }
    }
}