package com.kos.tailwindbookingapp.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.Util
import com.kos.tailwindbookingapp.Util.getTimeString
import com.kos.tailwindbookingapp.Util.getTimeoutTime
import com.kos.tailwindbookingapp.model.LaneSession
import java.util.concurrent.TimeUnit


class LaneListAdapter internal constructor(
    private val context: Context,
    private var callback: Callback
) :
    RecyclerView.Adapter<LaneListAdapter.ViewHolder>() {
    private var laneList: ArrayList<LaneSession> = ArrayList()
    var bool = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_lane, parent, false)
        )
    }

    val laneListChangeObserver = androidx.lifecycle.Observer<List<LaneSession>> {
        try {
            laneList.clear()
            laneList.addAll(it!!)
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            val lane = laneList[holder.adapterPosition]
            holder.itemView.setOnClickListener {
                callback.viewLane(lane)
                if(lane.isOccupied){
                    holder.timer?.cancel()
                    if (bool) {
                        holder.timer?.start()
                        notifyDataSetChanged()
                    }
                    bool = true
                }

            }
            if(lane.isOccupied){
                if (holder.timer != null) {
                    holder.timer?.cancel()
                }
                val totalCalculatedtimeinMillis = Util.getTimeMilliSeconds(lane)
                Log.d("Time1", lane.laneId.toString())
                holder.timer = object : CountDownTimer(totalCalculatedtimeinMillis, 1000) {
                    override fun onTick(leftTimeInMilliseconds: Long) {
                        Log.d("Time2", lane.laneId.toString())
                        var totalCalculatedtimeinPercentage = Util.getTimeInMilliSeconds(lane)
                        holder.laneTimeView.text = getTimeLeft(lane = lane)
                        if (lane.status == "ACTIVE") {
                            var Callc = totalCalculatedtimeinPercentage.toString()
                            if (Callc.contains("E-")) {
                                val ups = Callc.replace("E-", "")

                                try {
                                    val parsedInt = ups.toDouble()
                                    val parsedI = parsedInt.toInt() + 1
                                    holder.progressBar.progress = 100 - parsedI
                                } catch (nfe: NumberFormatException) {
                                }

                            } else {
                                holder.progressBar.progress =
                                    100 - totalCalculatedtimeinPercentage.toInt()
                            }
                        } else {
                            holder.progressBar.progress = totalCalculatedtimeinPercentage.toInt()
                        }
                    }
                    override fun onFinish() {

                    }
                }.start()
            }
            holder.renderView(lane)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return laneList.size
    }

    fun getColor(lane: LaneSession): Int {
        if (lane.status == "IDLE") {
            return ContextCompat.getColor(context, R.color.app_lite_grey)
        } else if (lane.status == "ACTIVE" && lane.startedOn != null) {
            return ContextCompat.getColor(context, R.color.burnt_sienna)
        } else if (lane.status == "TIMEOUT") {
            return ContextCompat.getColor(context, R.color.pink)
        }
        return ContextCompat.getColor(context, R.color.app_lite_green)
    }

    fun getTimeLeft(lane: LaneSession): String? {
        if (lane.status == "IDLE") {
            return getTimeString(TimeUnit.MILLISECONDS.toMinutes(Util.getIdleTime(lane)))
        } else if (lane.status == "ACTIVE" && lane.startedOn != null) {
            val minutes: Long =
                TimeUnit.MILLISECONDS.toMinutes(Util.getRemainingTimeInMilliseconds(lane))
            if (minutes < 0) {
                return "0 min left"
            }
            return String.format("%02d", minutes) + " mins left"
        } else if (lane.status == "TIMEOUT") {
            return getTimeString((getTimeoutTime(lane) / 60000).toLong())
        }
        return ""
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var laneNameView: AppCompatTextView = v.findViewById(R.id.laneNameView)
        var laneTimeView: AppCompatTextView = v.findViewById(R.id.laneTimeView)
        var progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        var timer: CountDownTimer? = null
        fun renderView(lane: LaneSession) {
            try {
                laneNameView.text = lane.laneName
                laneTimeView.setTextColor(getColor(lane))
                if(lane.isOccupied){
                    progressBar.visibility = View.VISIBLE
                    laneTimeView.visibility = View.VISIBLE
                    when (lane.status) {
                        "ACTIVE" -> {
                            laneNameView.setBackgroundResource(R.drawable.circle_occupied)
                            progressBar.setProgressTintList(
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.burnt_sienna
                                    )
                                )
                            );
                        }
                        "TIMEOUT" -> {
                            laneNameView.setBackgroundResource(R.drawable.circle_completed)
                            progressBar.setProgressTintList(
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.app_lite_pink
                                    )
                                )
                            );
                        }
                        else -> {
                            laneNameView.setBackgroundResource(R.drawable.circle_assigned)
                            progressBar.setProgressTintList(
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        context,
                                        R.color.app_lite_grey
                                    )
                                )
                            );
                        }
                    }

                }else{
                    progressBar.visibility = View.GONE
                    laneTimeView.visibility = View.GONE
                    laneNameView.setBackgroundResource(R.drawable.circle_available)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    interface Callback {
        fun viewLane(lane: LaneSession)
    }


}