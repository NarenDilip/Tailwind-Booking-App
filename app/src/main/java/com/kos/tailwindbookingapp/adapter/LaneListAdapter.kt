package com.kos.tailwindbookingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.kos.tailwindbookingapp.R
import com.tailwind.kos.model.LaneSession


class LaneListAdapter internal constructor(
    private val context: Context,
    private var callback: Callback
) :
    RecyclerView.Adapter<LaneListAdapter.ViewHolder>() {
    private var laneList: List<LaneSession> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_lane, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val lane = laneList[holder.adapterPosition]
            holder.itemView.setOnClickListener {
                callback.viewLane(lane)
            }
            holder.renderView(lane)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return laneList.size
    }

    fun setResults(results: List<LaneSession>) {
        laneList = results
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var laneNameView: AppCompatTextView = v.findViewById(R.id.laneNameView)
        fun renderView(lane: LaneSession) {
            try {
                laneNameView.text = lane.laneName
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    interface Callback {
        fun viewLane(lane: LaneSession)
    }


}