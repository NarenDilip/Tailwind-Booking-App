package com.kos.tailwindbookingapp.adapter

import android.content.Context
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
import com.kos.tailwindbookingapp.model.LaneSession
import java.util.concurrent.TimeUnit


class LaneListAdapter internal constructor(
    private val context: Context,
    private var callback: Callback
) :
    RecyclerView.Adapter<LaneListAdapter.ViewHolder>() {
    private var laneList: ArrayList<LaneSession> = ArrayList()

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
            }
            if(holder.timer != null){
                holder.timer?.cancel()
            }
            var ss = Util.getTimeInMilliSeconds(lane)
            holder.timer = object : CountDownTimer(600000, 1000) {
                // 500 means, onTick function will be called at every 500 milliseconds
                override fun onTick(leftTimeInMilliseconds: Long) {
                    /* val seconds = leftTimeInMilliseconds / 1000
                     val barVal: Int = 100 - ((seconds / 60 * 100).toInt() + (seconds % 60).toInt())*/
                    // barTimer.setProgress
                    val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(leftTimeInMilliseconds)
                   // var ss = Util.getTimeInMilliSeconds(lane)
                    var progress = (ss / 100).toInt()
                  //  Log.v("seconds","==="+progress+"=="+ss)
                    holder.progressBar.progress = seconds.toInt()
                    // format the textview to show the easily readable format
                }

                override fun onFinish() {

                }
            }.start()
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
            return ContextCompat.getColor(context,R.color.app_lite_grey)
        } else if (lane.status == "ACTIVE" && lane.startedOn != null) {
            return ContextCompat.getColor(context,R.color.burnt_sienna)
        } else if (lane.status == "TIMEOUT") {
            return ContextCompat.getColor(context,R.color.pink)
        }
        return ContextCompat.getColor(context,R.color.app_lite_green)
    }



    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var laneNameView: AppCompatTextView = v.findViewById(R.id.laneNameView)
        private var laneTimeView: AppCompatTextView = v.findViewById(R.id.laneTimeView)
         var progressBar:ProgressBar = v.findViewById(R.id.progressBar)
        var timer:CountDownTimer?=null
        var increment:Int= 0
        var bool= false
        fun renderView(lane: LaneSession) {
            try {
                laneNameView.text = lane.laneName
                laneTimeView.setTextColor(getColor(lane))
                if (lane.isOccupied) {
                    when (lane.status) {
                        "ACTIVE" -> {
                            laneNameView.setBackgroundResource(R.drawable.circle_occupied)
                        }
                        "TIMEOUT" -> {
                            laneNameView.setBackgroundResource(R.drawable.circle_completed)
                        }
                        else -> {
                            laneNameView.setBackgroundResource(R.drawable.circle_assigned)
                        }
                    }
                } else {
                    laneNameView.setBackgroundResource(R.drawable.circle_available)
                }
                laneNameView.setOnClickListener {
                    timer?.cancel()
                    if(bool){
                        timer?.start()
                        notifyDataSetChanged()
                    }
                    bool = true

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